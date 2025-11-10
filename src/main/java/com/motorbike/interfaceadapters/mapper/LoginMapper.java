package com.motorbike.interfaceadapters.mapper;

import com.motorbike.business.usecase.LoginUseCase.LoginResponse;
import com.motorbike.interfaceadapters.dto.LoginResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert between Use Case Response and Presentation DTO
 */
@Component
public class LoginMapper {
    
    /**
     * Convert from Use Case Response to Presentation DTO
     * @param response Use case response
     * @return LoginResponseDTO for API response
     */
    public LoginResponseDTO toDTO(LoginResponse response) {
        if (response == null) {
            return null;
        }
        
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setUserId(response.getUserId());
        dto.setEmail(response.getEmail());
        dto.setUsername(response.getUsername());
        dto.setRole(response.getRole());
        dto.setToken(response.getToken());
        dto.setSuccess(response.isSuccess());
        dto.setMessage(response.getMessage());
        
        return dto;
    }
}
