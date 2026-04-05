package com.motorbike.business.dto.user;

public class CheckUserDuplicationInputData {
    private final String email;
    private final String username;
    private final Long excludeUserId;
    
    public CheckUserDuplicationInputData(String email, String username, Long excludeUserId) {
        this.email = email;
        this.username = username;
        this.excludeUserId = excludeUserId;
    }
    
    public CheckUserDuplicationInputData(String email, String username) {
        this(email, username, null);
    }
    
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public Long getExcludeUserId() { return excludeUserId; }
}
