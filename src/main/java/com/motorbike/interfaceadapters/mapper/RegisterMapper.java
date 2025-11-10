package com.motorbike.interfaceadapters.mapper;

import com.motorbike.business.usecase.RegisterUseCase.RegisterResponse;
import com.motorbike.interfaceadapters.dto.RegisterResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert between Use Case Response and Presentation DTO
 */
@Component
public class RegisterMapper {
    
    /**
     * Convert from Use Case Response to Presentation DTO
     * @param response Use case response
     * @return RegisterResponseDTO for API response
     */
    public RegisterResponseDTO toDTO(RegisterResponse response) {
        if (response == null) {
            return null;
        }
        
        RegisterResponseDTO dto = new RegisterResponseDTO();
        dto.setUserId(response.getUserId());
        dto.setEmail(response.getEmail());
        dto.setUsername(response.getUsername());
        dto.setRole(response.getRole());
        dto.setSuccess(response.isSuccess());
        dto.setMessage(response.getMessage());
        
        return dto;
    }
}
