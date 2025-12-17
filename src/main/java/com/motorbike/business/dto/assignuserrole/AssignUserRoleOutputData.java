package com.motorbike.business.dto.assignuserrole;

public class AssignUserRoleOutputData {
    private final boolean success;
    private final String message;
    private final Long userId;
    private final String roleName;
    
    public AssignUserRoleOutputData(boolean success, String message, Long userId, String roleName) {
        this.success = success;
        this.message = message;
        this.userId = userId;
        this.roleName = roleName;
    }

    public AssignUserRoleOutputData(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.userId = null;
        this.roleName = null;
    }
    
    // Constructor with userId (message or roleName can be null)
    public AssignUserRoleOutputData(boolean success, Long userId, String secondParam) {
        this.success = success;
        this.userId = userId;
        // If secondParam looks like a message, assign to message; otherwise roleName
        if (secondParam != null && (secondParam.contains(" ") || secondParam.length() > 20)) {
            this.message = secondParam;
            this.roleName = null;
        } else {
            this.message = null;
            this.roleName = secondParam;
        }
    }
    
    // Constructor with roleName (message can be null)
    public AssignUserRoleOutputData(boolean success, String roleName, Long userId) {
        this.success = success;
        this.message = null;
        this.userId = userId;
        this.roleName = roleName;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public String getRoleName() {
        return roleName;
    }
}
