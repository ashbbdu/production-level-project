package com.api_task_management.common.exception;

public class ResourceAlreadyExistsException extends RuntimeException{
   public ResourceAlreadyExistsException (String message) {
        super(message);
    }
}
