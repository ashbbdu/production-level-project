package com.api_task_management.project;

import com.api_task_management.project.dto.request.CreateProjectRequest;
import com.api_task_management.project.dto.response.ProjectResponse;
import com.api_task_management.project.dto.type.ProjectStatus;
import com.api_task_management.project.entity.ProjectEntity;
import com.api_task_management.project.repository.ProjectRepository;

import com.api_task_management.project.service.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;
    @Test
    void shouldCreateProjectSuccessfully() {

        // Arrange

        when(projectRepository.existsByName("Payment"))
                .thenReturn(false);

        when(projectRepository.save(any(ProjectEntity.class)))
                .thenAnswer(invocation -> {

                    ProjectEntity project =
                            invocation.getArgument(0);

                    project.setId(1L);

                    return project;
                });

        CreateProjectRequest request =
                new CreateProjectRequest();

        request.setName("Payment");

        request.setStartDate(
                LocalDateTime.of(2026, 9, 19, 10, 0)
        );

        // Act

        ProjectResponse response =
                projectService.createProject(request);

        // Assert

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Payment",
                response.getName()
        );

        assertEquals(
                ProjectStatus.PLANNED,
                response.getStatus()
        );
    }
}
