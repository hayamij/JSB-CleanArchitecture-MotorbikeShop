package com.motorbike.business.usecase;

import com.motorbike.business.entity.User;

/**
 * Input Port (Interface) for Register Use Case
 * This defines the contract for the use case from the perspective of the caller
 */
public interface RegisterUseCase {
    
    /**
     * Execute the use case to register a new user
     * @param request Input request containing registration information
     * @return Output response containing registration result
     * @throws EmailAlreadyExistsException if email is already registered
     * @throws UsernameAlreadyExistsException if username is already taken
     */
    RegisterResponse execute(RegisterRequest request);
    
    /**
     * Input Data Transfer Object
     */
    class RegisterRequest {
        private final String email;
        private final String username;
        private final String password;
        private final String phoneNumber;
        
        public RegisterRequest(String email, String username, String password, String phoneNumber) {
            // Validation
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty");
            }
            if (password == null || password.length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters");
            }
            if (!isValidEmail(email)) {
                throw new IllegalArgumentException("Invalid email format");
            }
            
            this.email = email.trim().toLowerCase();
            this.username = username.trim();
            this.password = password;
            this.phoneNumber = phoneNumber != null ? phoneNumber.trim() : null;
        }
        
        private boolean isValidEmail(String email) {
            return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
        }
        
        public String getEmail() {
            return email;
        }
        
        public String getUsername() {
            return username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public String getPhoneNumber() {
            return phoneNumber;
        }
    }
    
    /**
     * Output Data Transfer Object
     */
    class RegisterResponse {
        private final Long userId;
        private final String email;
        private final String username;
        private final String role;
        private final boolean success;
        private final String message;
        
        public RegisterResponse(User user) {
            this.userId = user.getId();
            this.email = user.getEmail();
            this.username = user.getUsername();
            this.role = user.getRole();
            this.success = true;
            this.message = "Registration successful";
        }
        
        public RegisterResponse(boolean success, String message) {
            this.userId = null;
            this.email = null;
            this.username = null;
            this.role = null;
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
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
