package com.api_task_management.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {
    @GetMapping("/api/test/admin")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    @PreAuthorize("hasRole('USER')")
    @PreAuthorize("hasAnyRole('ADMIN' , 'USER')")
    public String adminOnly() {
        return "Welcome, Admin!";
    }
}
