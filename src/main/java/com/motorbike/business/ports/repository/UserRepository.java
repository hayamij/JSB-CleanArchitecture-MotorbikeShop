package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.User;
import java.util.Optional;

/**
 * Repository Interface (Port) for User Entity
 * Defines contract for user data access
 * Use case layer depends on this interface (Dependency Inversion)
 * Implementation will be in adapter/infrastructure layer
 */
public interface UserRepository {
    
    /**
     * Find user by email
     * @param email User's email
     * @return Optional containing user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by ID
     * @param id User's ID
     * @return Optional containing user if found
     */
    Optional<User> findById(Long id);
    
    /**
     * Check if email already exists in system
     * @param email Email to check
     * @return true if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Save or update user
     * @param user User entity to save
     * @return Saved user with ID
     */
    User save(User user);
    
    /**
     * Update last login timestamp
     * @param userId User's ID
     */
    void updateLastLogin(Long userId);
}
