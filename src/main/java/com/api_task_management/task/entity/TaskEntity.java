package com.api_task_management.task.entity;

import com.api_task_management.project.entity.ProjectEntity;
import com.api_task_management.task.dto.type.TaskPriority;
import com.api_task_management.task.dto.type.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 200)
    private String title;

    @Column(length = 2000)
//    @Size(max = 2000 , message = "Description should not be greater than 2000 characters !")
    private String description;

    @Column(nullable = false , length = 20)
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TODO;

    @Column(nullable = false , length = 20)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.LOW;

    @Column
    private LocalDateTime dueDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    Mapping

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "project_id" , nullable = false)
    private ProjectEntity project;

}
