package com.motorbike.business.usecase;

import com.motorbike.business.entity.User;

/**
 * Input Port (Interface) for Login Use Case
 * This defines the contract for the use case from the perspective of the caller
 */
public interface LoginUseCase {
    
    /**
     * Execute the use case to login
     * @param request Input request containing email and password
     * @return Output response containing user details and authentication token
     * @throws InvalidCredentialsException if credentials are invalid
     * @throws UserNotActiveException if user account is not active
     */
    LoginResponse execute(LoginRequest request);
    
    /**
     * Input Data Transfer Object
     */
    class LoginRequest {
        private final String email;
        private final String password;
        
        public LoginRequest(String email, String password) {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }
            this.email = email.trim();
            this.password = password;
        }
        
        public String getEmail() {
            return email;
        }
        
        public String getPassword() {
            return password;
        }
    }
    
    /**
     * Output Data Transfer Object
     */
    class LoginResponse {
        private final Long userId;
        private final String email;
        private final String username;
        private final String role;
        private final String token; // For session or JWT
        private final boolean success;
        private final String message;
        
        public LoginResponse(User user, String token) {
            this.userId = user.getId();
            this.email = user.getEmail();
            this.username = user.getUsername();
            this.role = user.getRole();
            this.token = token;
            this.success = true;
            this.message = "Login successful";
        }
        
        public LoginResponse(boolean success, String message) {
            this.userId = null;
            this.email = null;
            this.username = null;
            this.role = null;
            this.token = null;
            this.success = success;
            this.message = message;
        }
        
        // Getters
        public Long getUserId() {
            return userId;
        }
        
        public String getEmail() {
            return email;
        }
        
        public String getUsername() {
            return username;
        }
        
        public String getRole() {
            return role;
        }
        
        public String getToken() {
            return token;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
