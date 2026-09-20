package com.api_task_management.project.entity;

import com.api_task_management.project.dto.type.ProjectStatus;
import com.api_task_management.task.entity.TaskEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "projects")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false , length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false , length = 20)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.PLANNED;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = true)
    private LocalDateTime endDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    Mapping

    @OneToMany(mappedBy = "project" ,
            orphanRemoval = true, // means if a tasks is removed/disassociated with a project delete the task
            cascade = CascadeType.REMOVE // means if a project is deleted , delete all the associated tasks
    )
    private List<TaskEntity> tasks;

}
