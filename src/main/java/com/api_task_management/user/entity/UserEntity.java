package com.api_task_management.user.entity;

import com.api_task_management.user.dto.type.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true , length = 250)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false , length = 100)
    private String firstName;

    @Column(nullable = false , length = 100)
    private String lastName;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private UserRole role ; // admin will assign user role after signup

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
