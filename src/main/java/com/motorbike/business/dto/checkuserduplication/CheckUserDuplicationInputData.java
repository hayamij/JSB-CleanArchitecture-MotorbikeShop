package com.motorbike.business.dto.checkuserduplication;

public class CheckUserDuplicationInputData {
    private final String email;
    private final String username;
    private final Long excludeUserId; // null nếu check cho create, có giá trị nếu check cho update

    public CheckUserDuplicationInputData(String email, String username, Long excludeUserId) {
        this.email = email;
        this.username = username;
        this.excludeUserId = excludeUserId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public Long getExcludeUserId() {
        return excludeUserId;
    }
}
