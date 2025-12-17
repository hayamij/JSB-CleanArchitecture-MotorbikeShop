package com.motorbike.business.dto.assignuserrole;

public class AssignUserRoleInputData {
    private final Long userId;
    private final String roleName;
    
    public AssignUserRoleInputData(Long userId, String roleName) {
        this.userId = userId;
        this.roleName = roleName;
    }
    
    // Constructor for tests with only roleName
    public AssignUserRoleInputData(String roleName) {
        this.userId = null;
        this.roleName = roleName;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public String getRoleName() {
        return roleName;
    }
}
