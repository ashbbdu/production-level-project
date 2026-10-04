package com.api_task_management.project.controller;

import com.api_task_management.project.service.TransactionServiceA;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionServiceA transactionServiceA;
    @PostMapping("/required")
    public void requiredTransaction () {
        transactionServiceA.methodA();
    }
}
