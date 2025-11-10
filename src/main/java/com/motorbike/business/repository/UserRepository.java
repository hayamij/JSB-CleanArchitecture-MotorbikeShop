package com.motorbike.business.repository;

import java.util.List;
import java.util.Optional;

import com.motorbike.business.entity.User;

/**
 * Domain Repository Interface: UserRepository
 * Defines the contract for User persistence operations
 * This is part of the domain layer and is framework-agnostic
 * Implementation will be in the persistence layer
 */
public interface UserRepository {
    //update method user
    boolean insert(User user);
    boolean update(User user);   // 👈 thêm
    boolean delete(int id);
    List<User> findAll();
    /**
     * Find a user by email
     * @param email User email
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find a user by username
     * @param username Username
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by ID
     * @param id User ID
     * @return Optional containing the user if found
     */
    Optional<User> findById(Long id);
    
    /**
     * Save a user (create or update)
     * @param user User to save
     * @return Saved user
     */
    User save(User user);
    
    /**
     * Check if email already exists
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if username already exists
     * @param username Username to check
     * @return true if exists, false otherwise
     */
    boolean existsByUsername(String username);
}
