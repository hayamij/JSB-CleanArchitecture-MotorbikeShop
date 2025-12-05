package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.DeleteMotorbikeInputData;
import com.motorbike.business.dto.motorbike.DeleteMotorbikeOutputData;
import com.motorbike.business.usecase.input.DeleteMotorbikeInputBoundary;
import com.motorbike.business.usecase.output.DeleteMotorbikeOutputBoundary;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.domain.entities.XeMay;

public class DeleteMotorbikeUseCaseControl implements DeleteMotorbikeInputBoundary {

    private final DeleteMotorbikeOutputBoundary outputBoundary;
    private final MotorbikeRepository motorbikeRepository;

    public DeleteMotorbikeUseCaseControl(
            DeleteMotorbikeOutputBoundary outputBoundary,
            MotorbikeRepository motorbikeRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.motorbikeRepository = motorbikeRepository;
    }

    @Override
    public void execute(DeleteMotorbikeInputData input) {

        DeleteMotorbikeOutputData output;

        try {
            XeMay existing = motorbikeRepository.findById(input.id).orElse(null);

            if (existing == null) {
                output = new DeleteMotorbikeOutputData("NOT_FOUND", "Motorbike not found");
                outputBoundary.present(output);
                return;
            }

            motorbikeRepository.deleteById(input.id);

            output = new DeleteMotorbikeOutputData(true);

        } catch (Exception e) {
            output = new DeleteMotorbikeOutputData("SYSTEM_ERROR", e.getMessage());
        }

        outputBoundary.present(output);
    }
}
