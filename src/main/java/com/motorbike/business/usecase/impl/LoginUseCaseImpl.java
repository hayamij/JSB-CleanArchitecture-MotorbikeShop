package com.motorbike.business.usecase.impl;

import com.motorbike.business.usecase.LoginUseCase;
import com.motorbike.business.entity.User;
import com.motorbike.business.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of Login Use Case
 * Contains the business logic for user authentication
 */
@Service
public class LoginUseCaseImpl implements LoginUseCase {
    
    private final UserRepository userRepository;
    
    public LoginUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public LoginResponse execute(LoginRequest request) {
        // Step 1: Validate input
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        // Step 2: Find user by email
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException(
                "Invalid email or password"
            ));
        
        // Step 3: Check if user is active
        if (!user.canLogin()) {
            throw new UserNotActiveException("User account is not active");
        }
        
        // Step 4: Verify password (in real app, use BCrypt or similar)
        // For now, we'll do simple comparison (NOT SECURE - just for demo)
        if (!verifyPassword(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        
        // Step 5: Update last login timestamp
        user.updateLastLogin();
        userRepository.save(user);
        
        // Step 6: Generate session token (in real app, use JWT or session manager)
        String token = generateToken(user);
        
        // Step 7: Return response
        return new LoginResponse(user, token);
    }
    
    /**
     * Verify password
     * TODO: In production, use BCrypt or similar password hashing
     */
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        // For demo purposes, we'll do simple comparison
        // In production, use: BCrypt.checkpw(plainPassword, hashedPassword)
        return plainPassword.equals(hashedPassword);
    }
    
    /**
     * Generate authentication token
     * TODO: In production, use JWT or proper session management
     */
    private String generateToken(User user) {
        // For demo purposes, generate a simple UUID token
        // In production, use JWT with proper claims and expiration
        return UUID.randomUUID().toString();
    }
}
