package com.motorbike.business.usecase.control;

import java.util.Optional;

import com.motorbike.business.dto.accessory.DeleteAccessoryInputData;
import com.motorbike.business.dto.accessory.DeleteAccessoryOutputData;
import com.motorbike.business.ports.repository.AccessoryRepository;
import com.motorbike.business.usecase.input.DeleteAccessoryInputBoundary;
import com.motorbike.business.usecase.output.DeleteAccessoryOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;

public class DeleteAccessoryUseCaseControl implements DeleteAccessoryInputBoundary {

    private final DeleteAccessoryOutputBoundary outputBoundary;
    private final AccessoryRepository accessoryRepository;

    public DeleteAccessoryUseCaseControl(
            DeleteAccessoryOutputBoundary outputBoundary,
            AccessoryRepository accessoryRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.accessoryRepository = accessoryRepository;
    }

    @Override
    public void execute(DeleteAccessoryInputData input) {
        DeleteAccessoryOutputData outputData;

        try {
            if (input.id == null) {
                throw new IllegalArgumentException("ID cannot be null");
            }

            Optional<PhuKienXeMay> optionalAccessory = accessoryRepository.findById(input.id);
            if (optionalAccessory.isEmpty()) {
                throw new IllegalArgumentException("Accessory not found with ID: " + input.id);
            }

            accessoryRepository.deleteById(input.id);
            outputData = new DeleteAccessoryOutputData("Accessory deleted successfully");

        } catch (IllegalArgumentException e) {
            outputData = new DeleteAccessoryOutputData("VALIDATION_ERROR", e.getMessage());
        } catch (Exception e) {
            outputData = new DeleteAccessoryOutputData("SYSTEM_ERROR", e.getMessage());
        }

        outputBoundary.present(outputData);
    }
}
