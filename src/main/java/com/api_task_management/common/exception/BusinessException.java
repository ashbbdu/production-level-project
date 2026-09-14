package com.api_task_management.common.exception;

public class BusinessException extends RuntimeException {
    public BusinessException (String message) {
        super(message);
    }
}
