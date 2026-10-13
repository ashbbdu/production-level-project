package com.api_task_management.permission.service;

import org.springframework.transaction.annotation.Transactional;

public interface PermissionService {

    public void grantPermission (Long userId , String permissionName);
    public void revokePermission (Long userId , String permissionName);
}
