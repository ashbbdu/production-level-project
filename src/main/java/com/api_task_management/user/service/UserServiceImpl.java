package com.api_task_management.user.service;

import com.api_task_management.common.exception.ResourceAlreadyExistsException;
import com.api_task_management.user.dto.request.CreateUserRequest;
import com.api_task_management.user.dto.response.UserResponse;
import com.api_task_management.user.entity.UserEntity;
import com.api_task_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse toResponse (UserEntity user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setLastName(user.getLastName());

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


}
