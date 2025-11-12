package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.register.RegisterInputData;
import com.motorbike.business.dto.register.RegisterOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.RegisterInputBoundary;
import com.motorbike.business.usecase.RegisterOutputBoundary;
import com.motorbike.domain.entities.User;
import com.motorbike.domain.exceptions.InvalidUserException;

/**
 * Register Use Case Implementation
 * Contains application-specific business rules for user registration
 * NO dependencies on frameworks or UI
 * 
 * Business Rules:
 * 1. Email phải unique (không trùng với tài khoản khác)
 * 2. Email phải đúng định dạng
 * 3. Mật khẩu phải đáp ứng yêu cầu bảo mật (độ dài tối thiểu)
 * 4. Mật khẩu phải được mã hóa trước khi lưu vào database
 * 5. Mặc định role là "customer"
 * 6. Các thông tin bắt buộc: email, password, tên, số điện thoại
 * 7. Password và confirmPassword phải khớp nhau
 */
public class RegisterUseCaseImpl implements RegisterInputBoundary {
    
    private final RegisterOutputBoundary outputBoundary;
    private final UserRepository userRepository;

    /**
     * Constructor with dependency injection
     * @param outputBoundary Presenter interface
     * @param userRepository User repository interface
     */
    public RegisterUseCaseImpl(RegisterOutputBoundary outputBoundary, 
                              UserRepository userRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(RegisterInputData inputData) {
        try {
            // Step 1: Validate input
            validateInput(inputData);
            
            // Step 2: Check password confirmation
            // Business Rule: Password và confirmPassword phải khớp
            if (!inputData.getPassword().equals(inputData.getConfirmPassword())) {
                outputBoundary.present(new RegisterOutputData(
                    "PASSWORD_MISMATCH",
                    "Mật khẩu xác nhận không khớp"
                ));
                return;
            }
            
            // Step 3: Check if email already exists
            // Business Rule: Email phải unique
            if (userRepository.existsByEmail(inputData.getEmail())) {
                outputBoundary.present(new RegisterOutputData(
                    "EMAIL_ALREADY_EXISTS",
                    "Email đã được sử dụng bởi tài khoản khác"
                ));
                return;
            }
            
            // Step 4: Hash password
            // Business Rule: Mật khẩu phải được mã hóa trước khi lưu
            String hashedPassword = hashPassword(inputData.getPassword());
            
            // Step 5: Create new user entity
            // Business Rule: Mặc định role là "customer"
            User newUser = new User(
                inputData.getEmail(),
                inputData.getUsername(),
                hashedPassword,
                inputData.getPhoneNumber()
            );
            
            // Step 6: Save user to database
            User savedUser = userRepository.save(newUser);
            
            // Step 7: Prepare success output
            RegisterOutputData outputData = new RegisterOutputData(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUsername(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
            );
            
            // Step 8: Present result to output boundary
            outputBoundary.present(outputData);
            
        } catch (InvalidUserException e) {
            // Handle validation errors
            outputBoundary.present(new RegisterOutputData(
                e.getErrorCode(),
                e.getMessage()
            ));
        } catch (Exception e) {
            // Handle unexpected errors
            outputBoundary.present(new RegisterOutputData(
                "UNEXPECTED_ERROR",
                "Đã xảy ra lỗi không mong muốn: " + e.getMessage()
            ));
        }
    }

    /**
     * Validate input data
     * @param inputData Input data to validate
     */
    private void validateInput(RegisterInputData inputData) {
        if (inputData == null) {
            throw new InvalidUserException("NULL_INPUT", "Dữ liệu đầu vào không được null");
        }
        
        // Validate email - Business Rule: Email phải đúng định dạng
        User.validateEmail(inputData.getEmail());
        
        // Validate username - Business Rule: Tên là bắt buộc
        User.validateUsername(inputData.getUsername());
        
        // Validate password - Business Rule: Mật khẩu phải đáp ứng yêu cầu bảo mật
        User.validatePassword(inputData.getPassword());
        
        // Validate phone number - Business Rule: Số điện thoại là bắt buộc và đúng định dạng
        if (inputData.getPhoneNumber() == null || inputData.getPhoneNumber().trim().isEmpty()) {
            throw new InvalidUserException("EMPTY_PHONE", "Số điện thoại không được để trống");
        }
        User.validatePhoneNumber(inputData.getPhoneNumber());
        
        // Validate confirm password
        if (inputData.getConfirmPassword() == null || inputData.getConfirmPassword().isEmpty()) {
            throw new InvalidUserException("EMPTY_CONFIRM_PASSWORD", "Xác nhận mật khẩu không được để trống");
        }
    }

    /**
     * Hash password before storing
     * In production, this should use proper password hashing (BCrypt, etc.)
     * For now, simple hashing for testing
     * 
     * @param plainPassword Plain text password
     * @return Hashed password
     */
    private String hashPassword(String plainPassword) {
        // TODO: Implement proper password hashing with BCrypt
        // For now, simple prefix for testing to distinguish from plain password
        // In production: return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        return "HASHED_" + plainPassword;
    }
}
