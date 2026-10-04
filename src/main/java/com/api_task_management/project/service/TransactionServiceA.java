package com.api_task_management.project.service;

import com.api_task_management.project.entity.ProjectEntity;
import com.api_task_management.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceA {

    private final TransactionServiceB serviceB;
    private final ProjectRepository projectRepository;

    @Transactional()
    public void methodA() {


        System.out.println("A: saving project");
        ProjectEntity project = projectRepository.findById(1L).orElseThrow();
        project.setName("Ashish Project");

        serviceB.methodB();


        System.out.println("A: completed");
        throw new RuntimeException("Failure in A");
    }
}
