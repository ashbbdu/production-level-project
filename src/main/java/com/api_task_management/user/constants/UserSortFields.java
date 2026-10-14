package com.api_task_management.user.constants;

import java.util.Set;

public class UserSortFields {
    private UserSortFields () {};
    public static final Set<String> ALLOWED_FIELDS = Set.of(
            "id",
            "email",
            "firstName",
            "role",
            "createdAt"
    );
}
