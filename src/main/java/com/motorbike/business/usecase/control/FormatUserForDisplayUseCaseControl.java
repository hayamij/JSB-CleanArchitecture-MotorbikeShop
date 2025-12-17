package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatuserfordisplay.FormatUserForDisplayInputData;
import com.motorbike.business.dto.formatuserfordisplay.FormatUserForDisplayOutputData;
import com.motorbike.business.usecase.input.FormatUserForDisplayInputBoundary;
import com.motorbike.business.usecase.output.FormatUserForDisplayOutputBoundary;
import com.motorbike.domain.entities.NguoiDung;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.ValidationException;

import java.time.format.DateTimeFormatter;

public class FormatUserForDisplayUseCaseControl implements FormatUserForDisplayInputBoundary {
    private final FormatUserForDisplayOutputBoundary outputBoundary;

    public FormatUserForDisplayUseCaseControl(FormatUserForDisplayOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(FormatUserForDisplayInputData inputData) {
        FormatUserForDisplayOutputData outputData = formatInternal(inputData);
        outputBoundary.present(outputData);
    }

    public FormatUserForDisplayOutputData formatInternal(FormatUserForDisplayInputData inputData) {
        FormatUserForDisplayOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("FormatUserForDisplay");
            }
            if (inputData.getUser() == null) {
                throw ValidationException.invalidInput("User is required");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - format user for display
        if (errorException == null) {
            try {
                TaiKhoan user = inputData.getUser();

                // Format role display
                String roleDisplay = formatRoleDisplay(user.getVaiTro());
                String roleBadgeColor = getRoleBadgeColor(user.getVaiTro());

                // Format created date
                String formattedCreatedDate = null;
                if (user.getNgayTao() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    formattedCreatedDate = user.getNgayTao().format(formatter);
                }

                // Format phone number (mask if needed for privacy)
                String phoneNumber = user.getSoDienThoai();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    // Keep as is, or mask: "0901***456"
                    phoneNumber = phoneNumber;
                }

                outputData = new FormatUserForDisplayOutputData(
                        user.getMaNguoiDung(),
                        user.getTenDangNhap(),
                        user.getEmail(),
                        user.getHoTen(),
                        phoneNumber,
                        roleDisplay,
                        roleBadgeColor,
                        formattedCreatedDate
                );

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "FORMAT_ERROR";
            outputData = FormatUserForDisplayOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }

    private String formatRoleDisplay(VaiTro vaiTro) {
        if (vaiTro == null) {
            return "Chưa xác định";
        }

        switch (vaiTro) {
            case ADMIN:
                return "Quản trị viên";
            case CUSTOMER:
                return "Khách hàng";
            default:
                return vaiTro.name();
        }
    }

    private String getRoleBadgeColor(VaiTro vaiTro) {
        if (vaiTro == null) {
            return "secondary";
        }

        switch (vaiTro) {
            case ADMIN:
                return "danger";
            case CUSTOMER:
                return "primary";
            default:
                return "secondary";
        }
    }
}
