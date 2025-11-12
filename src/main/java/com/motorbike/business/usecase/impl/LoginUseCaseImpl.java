package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.login.LoginInputData;
import com.motorbike.business.dto.login.LoginOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.LoginInputBoundary;
import com.motorbike.business.usecase.LoginOutputBoundary;
import com.motorbike.domain.entities.Cart;
import com.motorbike.domain.entities.User;
import com.motorbike.domain.exceptions.AuthenticationException;
import com.motorbike.domain.exceptions.InvalidUserException;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Login Use Case Implementation
 * Contains application-specific business rules for user login
 * NO dependencies on frameworks or UI
 * 
 * Business Rules:
 * 1. Email phải tồn tại trong hệ thống
 * 2. Mật khẩu phải khớp với mật khẩu đã mã hóa trong database
 * 3. Sau khi đăng nhập thành công, tạo session/token cho user
 * 4. Phân biệt role: customer vs admin
 * 5. Giỏ hàng của guest (nếu có) phải được merge với giỏ hàng của user sau khi đăng nhập
 */
public class LoginUseCaseImpl implements LoginInputBoundary {
    
    private final LoginOutputBoundary outputBoundary;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    /**
     * Constructor with dependency injection
     * @param outputBoundary Presenter interface
     * @param userRepository User repository interface
     * @param cartRepository Cart repository interface
     */
    public LoginUseCaseImpl(LoginOutputBoundary outputBoundary, 
                           UserRepository userRepository,
                           CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void execute(LoginInputData inputData) {
        try {
            // Step 1: Validate input
            validateInput(inputData);
            
            // Step 2: Find user by email
            Optional<User> userOptional = userRepository.findByEmail(inputData.getEmail());
            if (userOptional.isEmpty()) {
                // Business Rule: Email phải tồn tại trong hệ thống
                outputBoundary.present(new LoginOutputData(
                    "EMAIL_NOT_FOUND",
                    "Email không tồn tại trong hệ thống"
                ));
                return;
            }
            
            User user = userOptional.get();
            
            // Step 3: Verify password
            // Business Rule: Mật khẩu phải khớp
            if (!verifyPassword(inputData.getPassword(), user.getPassword())) {
                outputBoundary.present(new LoginOutputData(
                    "INVALID_PASSWORD",
                    "Mật khẩu không chính xác"
                ));
                return;
            }
            
            // Step 4: Check if user is active
            if (!user.isActive()) {
                outputBoundary.present(new LoginOutputData(
                    "USER_INACTIVE",
                    "Tài khoản đã bị vô hiệu hóa"
                ));
                return;
            }
            
            // Step 5: Merge guest cart if exists
            // Business Rule: Giỏ hàng của guest phải được merge với giỏ hàng của user
            boolean cartMerged = false;
            int mergedItemsCount = 0;
            
            if (inputData.getGuestCartId() != null) {
                Optional<Cart> userCartOptional = cartRepository.findByUserId(user.getId());
                
                if (userCartOptional.isPresent()) {
                    // User already has a cart, merge guest cart into it
                    mergedItemsCount = cartRepository.mergeGuestCartToUserCart(
                        inputData.getGuestCartId(), 
                        userCartOptional.get().getId()
                    );
                    cartMerged = mergedItemsCount > 0;
                } else {
                    // User doesn't have a cart, assign guest cart to user
                    Optional<Cart> guestCartOptional = cartRepository.findById(inputData.getGuestCartId());
                    if (guestCartOptional.isPresent()) {
                        Cart guestCart = guestCartOptional.get();
                        guestCart.assignToUser(user.getId());
                        cartRepository.save(guestCart);
                        mergedItemsCount = guestCart.getItemCount();
                        cartMerged = true;
                    }
                }
            }
            
            // Step 6: Update last login timestamp
            userRepository.updateLastLogin(user.getId());
            
            // Step 7: Create session token (simplified version)
            // Business Rule: Tạo session/token cho user
            String sessionToken = generateSessionToken(user);
            
            // Step 8: Prepare success output
            LoginOutputData outputData = new LoginOutputData(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole(), // Business Rule: Phân biệt role customer vs admin
                LocalDateTime.now(),
                sessionToken,
                cartMerged,
                mergedItemsCount
            );
            
            // Step 9: Present result to output boundary
            outputBoundary.present(outputData);
            
        } catch (InvalidUserException e) {
            // Handle validation errors
            outputBoundary.present(new LoginOutputData(
                e.getErrorCode(),
                e.getMessage()
            ));
        } catch (AuthenticationException e) {
            // Handle authentication errors
            outputBoundary.present(new LoginOutputData(
                e.getErrorCode(),
                e.getMessage()
            ));
        } catch (Exception e) {
            // Handle unexpected errors
            outputBoundary.present(new LoginOutputData(
                "UNEXPECTED_ERROR",
                "Đã xảy ra lỗi không mong muốn: " + e.getMessage()
            ));
        }
    }

    /**
     * Validate input data
     * @param inputData Input data to validate
     */
    private void validateInput(LoginInputData inputData) {
        if (inputData == null) {
            throw new InvalidUserException("NULL_INPUT", "Dữ liệu đầu vào không được null");
        }
        
        // Validate email
        User.validateEmail(inputData.getEmail());
        
        // Validate password
        User.validatePassword(inputData.getPassword());
    }

    /**
     * Verify password against stored hash
     * In production, this should use proper password hashing (BCrypt, etc.)
     * For now, simple comparison for testing
     * 
     * @param plainPassword Plain text password from input
     * @param hashedPassword Hashed password from database
     * @return true if passwords match
     */
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        // TODO: Implement proper password verification with BCrypt
        // For now, simple string comparison for testing
        // In production: return BCrypt.checkpw(plainPassword, hashedPassword);
        return plainPassword.equals(hashedPassword);
    }

    /**
     * Generate session token for user
     * In production, this should use JWT or proper session management
     * 
     * @param user User entity
     * @return Session token
     */
    private String generateSessionToken(User user) {
        // TODO: Implement proper JWT token generation
        // For now, simple token for testing
        return "SESSION_" + user.getId() + "_" + System.currentTimeMillis();
    }
}
