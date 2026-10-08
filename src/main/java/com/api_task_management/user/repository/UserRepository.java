package com.api_task_management.user.repository;

import com.api_task_management.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity , Long> {
    UserEntity findByEmail(String email);
    boolean existsByEmail(String email);
}
