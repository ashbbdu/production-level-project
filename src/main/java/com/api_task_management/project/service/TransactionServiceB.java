package com.api_task_management.project.service;

import com.api_task_management.task.entity.TaskEntity;
import com.api_task_management.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceB {
    private final TaskRepository taskRepository;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void methodB() {

        TaskEntity task = taskRepository.findById(1L).orElseThrow();
        task.setDescription("New updated description");
        System.out.println("B: saving task");
    }
}
