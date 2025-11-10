package com.motorbike.interfaceadapters.controller;

import com.motorbike.business.usecase.LoginUseCase;
import com.motorbike.business.usecase.LoginUseCase.LoginRequest;
import com.motorbike.business.usecase.LoginUseCase.LoginResponse;
import com.motorbike.business.usecase.impl.InvalidCredentialsException;
import com.motorbike.business.usecase.impl.UserNotActiveException;
import com.motorbike.interfaceadapters.dto.LoginRequestDTO;
import com.motorbike.interfaceadapters.dto.LoginResponseDTO;
import com.motorbike.interfaceadapters.mapper.LoginMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Authentication operations
 * Exposes the Login Use Case through HTTP endpoints
 */
@RestController
@RequestMapping(value = "/api/auth", produces = "application/json;charset=UTF-8")
public class AuthController {
    
    private final LoginUseCase loginUseCase;
    private final LoginMapper loginMapper;
    
    public AuthController(LoginUseCase loginUseCase, LoginMapper loginMapper) {
        this.loginUseCase = loginUseCase;
        this.loginMapper = loginMapper;
    }
    
    /**
     * Login endpoint
     * Endpoint: POST /api/auth/login
     * 
     * Use Case: Đăng nhập
     * Actor: Customer, Admin
     * 
     * @param requestDTO Login credentials
     * @return LoginResponseDTO with user details and token
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO) {
        try {
            // Step 1: Validate input DTO
            if (requestDTO == null || requestDTO.getEmail() == null || requestDTO.getPassword() == null) {
                LoginResponseDTO errorResponse = new LoginResponseDTO();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("Email and password are required");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Step 2: Create use case request
            LoginRequest request = new LoginRequest(
                requestDTO.getEmail(), 
                requestDTO.getPassword()
            );
            
            // Step 3: Execute use case
            LoginResponse response = loginUseCase.execute(request);
            
            // Step 4: Map to presentation DTO
            LoginResponseDTO responseDTO = loginMapper.toDTO(response);
            
            // Step 5: Return response
            return ResponseEntity.ok(responseDTO);
            
        } catch (InvalidCredentialsException e) {
            // Invalid credentials - return 401
            LoginResponseDTO errorResponse = new LoginResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            
        } catch (UserNotActiveException e) {
            // User not active - return 403
            LoginResponseDTO errorResponse = new LoginResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            
        } catch (IllegalArgumentException e) {
            // Invalid input - return 400
            LoginResponseDTO errorResponse = new LoginResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (Exception e) {
            // Internal error - return 500
            LoginResponseDTO errorResponse = new LoginResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
