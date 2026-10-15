package com.api_task_management.user.service;

import com.api_task_management.common.exception.BusinessException;
import com.api_task_management.common.exception.ResourceAlreadyExistsException;
import com.api_task_management.common.exception.ResourceNotFoundException;
import com.api_task_management.common.response.PageResponse;
import com.api_task_management.user.constants.UserSortFields;
import com.api_task_management.user.dto.request.CreateUserRequest;
import com.api_task_management.user.dto.request.UserFilterRequest;
import com.api_task_management.user.dto.response.UserResponse;
import com.api_task_management.user.dto.type.UserRole;
import com.api_task_management.user.entity.UserEntity;
import com.api_task_management.user.repository.UserRepository;
import com.api_task_management.user.repository.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void validateSorting (Pageable pageable) {
       for(Sort.Order order : pageable.getSort()) {
           if(!UserSortFields.ALLOWED_FIELDS.contains(order.getProperty())) {
               throw new BusinessException("Sorting by '" + order.getProperty() + "' is not allowed");
           }
       }
    }

    public UserResponse toResponse (UserEntity user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole());
        response.setEnabled(user.isEnabled());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }



    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("User with email : " + request.getEmail() + " already exists !");
        }

        UserEntity newUser = new UserEntity();

        newUser.setEmail(request.getEmail());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        newUser.setPassword(hashedPassword);

        UserEntity savedUser = userRepository.save(newUser);

         return toResponse(savedUser);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse approveUser(Long userId, UserRole role) {

        if(role == null) {
            throw new BusinessException("Please provide user role !");
        }
       UserEntity user = userRepository.findById(userId).orElseThrow(() ->
               new ResourceNotFoundException("User with id : " + userId + " does not exists !"));



       if(user.isEnabled()) {
           throw new ResourceAlreadyExistsException("User is already enabled !");
       }
       user.setEnabled(true);
       user.setRole(role);

        return toResponse(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse disableUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with id : " + userId + " does not exists !"));

        if(!user.isEnabled()) {
            throw new ResourceAlreadyExistsException("User is already disabled !");
        }

        user.setEnabled(false);

        return toResponse(user);
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(Pageable pageable , UserFilterRequest filter) {

//        use two query approach here , TODO : 15 Oct
        if(pageable.getPageSize() > 100) {
            throw new BusinessException("Page size cannot exceed 100");
        }

        validateSorting(pageable);


        Specification<UserEntity> specification = UserSpecification.filter(filter);
        Page<UserEntity> users = userRepository.findAll(specification , pageable);

        List<UserResponse> allUsers = users.stream().map(this::toResponse).toList();

        return new PageResponse<>(
                allUsers,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.hasNext(),
                users.hasPrevious()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                    new ResourceNotFoundException("User with id : " + userId + " does not exist !")
                );

        return toResponse(user);
    }


}
