
package com.motorbike.business.usecase.control;
import com.motorbike.business.usecase.input.ViewCartInputBoundary;

import com.motorbike.business.dto.viewcart.ViewCartInputData;
import com.motorbike.business.dto.viewcart.ViewCartOutputData;
import com.motorbike.business.dto.formatcartitems.FormatCartItemsForDisplayInputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.ViewCartOutputBoundary;
import com.motorbike.business.dto.calculatecarttotals.CalculateCartTotalsInputData;
import com.motorbike.business.usecase.input.CalculateCartTotalsInputBoundary;
import com.motorbike.business.usecase.input.FormatCartItemsForDisplayInputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.SystemException;
import com.motorbike.domain.exceptions.ValidationException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ViewCartUseCaseControl implements ViewCartInputBoundary {
    
    private final ViewCartOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CalculateCartTotalsInputBoundary calculateCartTotalsUseCase;
    private final FormatCartItemsForDisplayInputBoundary formatCartItemsUseCase;
    
    public ViewCartUseCaseControl(
            ViewCartOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository,
            CalculateCartTotalsInputBoundary calculateCartTotalsUseCase,
            FormatCartItemsForDisplayInputBoundary formatCartItemsUseCase) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.calculateCartTotalsUseCase = calculateCartTotalsUseCase;
        this.formatCartItemsUseCase = formatCartItemsUseCase;
    }

    // Constructor with 3 parameters (for backward compatibility)
    public ViewCartUseCaseControl(
            ViewCartOutputBoundary outputBoundary,
            CartRepository cartRepository,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.calculateCartTotalsUseCase = new CalculateCartTotalsUseCaseControl(null);
        this.formatCartItemsUseCase = new FormatCartItemsForDisplayUseCaseControl(null, productRepository);
    }
    
    @Override
    public void execute(ViewCartInputData inputData) {
        ViewCartOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            com.motorbike.domain.entities.TaiKhoan.checkInput(inputData.getUserId());
        } catch (Exception e) {
            errorException = e;
        }
        
        GioHang gioHang = null;
        if (errorException == null) {
            try {
                gioHang = cartRepository.findByUserId(inputData.getUserId())
                    .orElseThrow(DomainException::emptyCart);
                
                if (gioHang.getDanhSachSanPham().isEmpty()) {
                    throw DomainException.emptyCart();
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException == null && gioHang != null) {
            try {
                // Step 1: UC-78 Format cart items for display (with stock warnings)
                FormatCartItemsForDisplayInputData formatInput = new FormatCartItemsForDisplayInputData(
                    gioHang.getDanhSachSanPham()
                );
                var formatResult = ((FormatCartItemsForDisplayUseCaseControl) formatCartItemsUseCase)
                    .formatInternal(formatInput);
                
                if (!formatResult.isSuccess()) {
                    throw new SystemException(formatResult.getErrorMessage(), formatResult.getErrorCode());
                }
                
                // Convert FormatCartItemsForDisplayOutputData.CartItemDisplayData to ViewCartOutputData.CartItemData
                List<ViewCartOutputData.CartItemData> itemList = formatResult.getFormattedItems().stream()
                    .map(item -> new ViewCartOutputData.CartItemData(
                        Long.parseLong(item.getProductId()),
                        item.getProductName(),
                        item.getImageUrl(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getSubtotal(),
                        item.getAvailableStock(),
                        item.isHasStockWarning(),
                        item.getStockWarningMessage()
                    ))
                    .collect(Collectors.toList());
                
                // Step 2: UC-42 Calculate cart totals
                CalculateCartTotalsInputData totalsInput = new CalculateCartTotalsInputData(
                    gioHang.getDanhSachSanPham()
                );
                var totalsResult = ((CalculateCartTotalsUseCaseControl) calculateCartTotalsUseCase)
                    .calculateInternal(totalsInput);
                
                outputData = ViewCartOutputData.forSuccess(
                    gioHang.getMaGioHang(),
                    itemList,
                    totalsResult.getTotalAmount()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException != null) {
            if (errorException instanceof DomainException && "EMPTY_CART".equals(((DomainException) errorException).getErrorCode())) {
                outputData = ViewCartOutputData.forEmptyCart();
            } else {
                String errorCode = "SYSTEM_ERROR";
                String message = errorException.getMessage();
                
                if (errorException instanceof ValidationException) {
                    errorCode = ((ValidationException) errorException).getErrorCode();
                } else if (errorException instanceof DomainException) {
                    errorCode = ((DomainException) errorException).getErrorCode();
                } else if (errorException instanceof com.motorbike.domain.exceptions.SystemException) {
                    errorCode = ((com.motorbike.domain.exceptions.SystemException) errorException).getErrorCode();
                }
                
                outputData = ViewCartOutputData.forError(errorCode, message);
            }
        }
        
        outputBoundary.present(outputData);
    }
}
