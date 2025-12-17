package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.assignuserrole.AssignUserRoleInputData;
import com.motorbike.business.dto.assignuserrole.AssignUserRoleOutputData;
import com.motorbike.business.usecase.input.AssignUserRoleInputBoundary;
import com.motorbike.business.usecase.output.AssignUserRoleOutputBoundary;
import com.motorbike.business.ports.repository.TaiKhoanRepository;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.DomainException;
import java.util.Optional;

public class AssignUserRoleUseCaseControl implements AssignUserRoleInputBoundary {
    
    private final AssignUserRoleOutputBoundary outputBoundary;
    private final TaiKhoanRepository taiKhoanRepository;
    
    public AssignUserRoleUseCaseControl(AssignUserRoleOutputBoundary outputBoundary, TaiKhoanRepository taiKhoanRepository) {
        this.outputBoundary = outputBoundary;
        this.taiKhoanRepository = taiKhoanRepository;
    }
    
    // Constructor for tests without repository
    public AssignUserRoleUseCaseControl(AssignUserRoleOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.taiKhoanRepository = null;
    }
    
    @Override
    public void execute(AssignUserRoleInputData inputData) {
        AssignUserRoleOutputData outputData = assignInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    public AssignUserRoleOutputData assignInternal(AssignUserRoleInputData inputData) {
        AssignUserRoleOutputData outputData = null;
        Exception errorException = null;
        
        try {
            Long userId = inputData.getUserId();
            String roleName = inputData.getRoleName();
            
            Optional<TaiKhoan> userOpt = taiKhoanRepository.findById(userId);
            if (userOpt.isEmpty()) {
                throw DomainException.userNotFound();
            }
            
            TaiKhoan user = userOpt.get();
            VaiTro role = VaiTro.fromString(roleName);
            
            user.setVaiTro(role);
            taiKhoanRepository.save(user);
            
            outputData = new AssignUserRoleOutputData(true, null, "User role assigned successfully");
            
        } catch (Exception e) {
            errorException = e;
            outputData = new AssignUserRoleOutputData(false, e.getMessage(), null);
        }
        
        return outputData;
    }
}
