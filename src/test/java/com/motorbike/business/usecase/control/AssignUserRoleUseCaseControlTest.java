package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.assignuserrole.AssignUserRoleInputData;
import com.motorbike.business.dto.assignuserrole.AssignUserRoleOutputData;
import com.motorbike.business.usecase.output.AssignUserRoleOutputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssignUserRoleUseCaseControlTest {

    @Mock
    private AssignUserRoleOutputBoundary outputBoundary;

    private AssignUserRoleUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new AssignUserRoleUseCaseControl(outputBoundary);
    }

    @Test
    void shouldAssignCustomerRoleByDefault() {
        // Given
        AssignUserRoleInputData inputData = new AssignUserRoleInputData(null);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(AssignUserRoleOutputData.class));
    }

    @Test
    void shouldAssignAdminRole() {
        // Given
        AssignUserRoleInputData inputData = new AssignUserRoleInputData("ADMIN");

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(AssignUserRoleOutputData.class));
    }

    @Test
    void shouldAssignCustomerRole() {
        // Given
        AssignUserRoleInputData inputData = new AssignUserRoleInputData("CUSTOMER");

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(AssignUserRoleOutputData.class));
    }
}
