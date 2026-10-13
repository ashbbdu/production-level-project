package com.api_task_management.permission.service;

import com.api_task_management.common.exception.ResourceAlreadyExistsException;
import com.api_task_management.common.exception.ResourceNotFoundException;
import com.api_task_management.permission.entity.PermissionEntity;
import com.api_task_management.permission.repository.PermissionRepository;
import com.api_task_management.user.entity.UserEntity;
import com.api_task_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public void grantPermission(Long userId, String permissionName) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                       new ResourceNotFoundException("User with id : " + userId + " not found !")
                );

        PermissionEntity permission = permissionRepository.findByName(permissionName).orElseThrow(() ->
                        new ResourceNotFoundException("Permission with name : " + permissionName + " not found !")
                );

        if(user.getPermissions().contains(permission)) {
            throw new ResourceAlreadyExistsException("Permission already exists !");
        }
        user.getPermissions().add(permission);

    }

    @Override
    @Transactional
    public void revokePermission(Long userId, String permissionName) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with id : " + userId + " not found !")
        );

        PermissionEntity permission = permissionRepository.findByName(permissionName).orElseThrow(() ->
                new ResourceNotFoundException("Permission with name : " + permissionName + " not found !")
        );

        user.getPermissions().remove(permission);
    }
}
