package com.api_task_management.permission.repository;

import com.api_task_management.permission.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity , Long> {
    Optional<PermissionEntity> findByName(String name);
}
