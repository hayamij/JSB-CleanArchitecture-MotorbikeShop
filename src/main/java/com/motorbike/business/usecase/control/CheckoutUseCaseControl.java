
package com.motorbike.business.usecase.control;

import com.motorbike.business.usecase.input.CheckoutInputBoundary;

import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.dto.checkout.CheckoutOutputData;
import com.motorbike.business.dto.validatecart.ValidateCartBeforeCheckoutInputData;
import com.motorbike.business.dto.clearcart.ClearCartInputData;
import com.motorbike.business.dto.createorder.CreateOrderFromCartInputData;
import com.motorbike.business.dto.reducestock.ReduceProductStockInputData;
import com.motorbike.business.dto.formatorderitems.FormatOrderItemsForCheckoutInputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.CheckoutOutputBoundary;
import com.motorbike.business.usecase.input.ValidateCartBeforeCheckoutInputBoundary;
import com.motorbike.business.usecase.input.ClearCartInputBoundary;
import com.motorbike.business.usecase.input.CreateOrderFromCartInputBoundary;
import com.motorbike.business.usecase.input.ReduceProductStockInputBoundary;
import com.motorbike.business.usecase.input.FormatOrderItemsForCheckoutInputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.PhuongThucThanhToan;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.SystemException;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutUseCaseControl implements CheckoutInputBoundary {
    
    private final CheckoutOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ValidateCartBeforeCheckoutInputBoundary validateCartUseCase;
    private final CreateOrderFromCartInputBoundary createOrderUseCase;
    private final ReduceProductStockInputBoundary reduceStockUseCase;
    private final ClearCartInputBoundary clearCartUseCase;
    private final FormatOrderItemsForCheckoutInputBoundary formatOrderItemsUseCase;
    
    public CheckoutUseCaseControl(
            CheckoutOutputBoundary outputBoundary,
            CartRepository cartRepository,
            OrderRepository orderRepository,
            ValidateCartBeforeCheckoutInputBoundary validateCartUseCase,
            CreateOrderFromCartInputBoundary createOrderUseCase,
            ReduceProductStockInputBoundary reduceStockUseCase,
            ClearCartInputBoundary clearCartUseCase,
            FormatOrderItemsForCheckoutInputBoundary formatOrderItemsUseCase) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.validateCartUseCase = validateCartUseCase;
        this.createOrderUseCase = createOrderUseCase;
        this.reduceStockUseCase = reduceStockUseCase;
        this.clearCartUseCase = clearCartUseCase;
        this.formatOrderItemsUseCase = formatOrderItemsUseCase;
    }
    
    // Constructor for tests with 4 params (outputBoundary, cartRepo, productRepo, orderRepo)
    public CheckoutUseCaseControl(
            CheckoutOutputBoundary outputBoundary,
            CartRepository cartRepository,
            com.motorbike.business.ports.repository.ProductRepository productRepository,
            OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.validateCartUseCase = new ValidateCartBeforeCheckoutUseCaseControl(null, cartRepository, productRepository);
        this.createOrderUseCase = new CreateOrderFromCartUseCaseControl(null, orderRepository, cartRepository);
        this.reduceStockUseCase = new ReduceProductStockUseCaseControl(null, productRepository);
        this.clearCartUseCase = new ClearCartUseCaseControl(null, cartRepository);
        this.formatOrderItemsUseCase = new FormatOrderItemsForCheckoutUseCaseControl(null);
    }
    
    public void execute(CheckoutInputData inputData) {
        CheckoutOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            DonHang.checkInput(
                inputData.getUserId(),
                inputData.getReceiverName(),
                inputData.getPhoneNumber(),
                inputData.getShippingAddress()
            );
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
                
                // UC-41: Validate cart before checkout
                ValidateCartBeforeCheckoutInputData validateInput = new ValidateCartBeforeCheckoutInputData(
                    gioHang.getMaGioHang()
                );
                var validationResult = ((ValidateCartBeforeCheckoutUseCaseControl) validateCartUseCase)
                    .validateInternal(validateInput);
                
                if (!validationResult.isSuccess()) {
                    throw new DomainException(validationResult.getErrorMessage(), validationResult.getErrorCode());
                }
                
                if (!validationResult.isValid()) {
                    String errorMsg = "Giỏ hàng không hợp lệ: " + String.join("; ", validationResult.getReasons());
                    throw new DomainException(errorMsg, "INVALID_CART_STATE");
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException == null && gioHang != null) {
            try {
                PhuongThucThanhToan phuongThucThanhToan = PhuongThucThanhToan.THANH_TOAN_TRUC_TIEP;
                if (inputData.getPaymentMethod() != null) {
                    try {
                        phuongThucThanhToan = PhuongThucThanhToan.valueOf(inputData.getPaymentMethod());
                    } catch (IllegalArgumentException e) {
                        // Default to COD if invalid payment method
                    }
                }
                
                // UC-44: Create order from cart
                CreateOrderFromCartInputData createOrderInput = new CreateOrderFromCartInputData(
                    gioHang,
                    inputData.getReceiverName(),
                    inputData.getPhoneNumber(),
                    inputData.getShippingAddress(),
                    inputData.getNote(),
                    phuongThucThanhToan
                );
                var createOrderResult = ((CreateOrderFromCartUseCaseControl) createOrderUseCase)
                    .createOrderInternal(createOrderInput);
                
                if (!createOrderResult.isSuccess()) {
                    throw new DomainException(createOrderResult.getErrorMessage(), createOrderResult.getErrorCode());
                }
                
                // Get the created order from UC-44
                DonHang donHang = createOrderResult.getDonHang();
                
                // Save order to get ID
                DonHang savedOrder = orderRepository.save(donHang);
                
                // UC-45: Reduce product stock for all items
                for (ChiTietGioHang item : gioHang.getDanhSachSanPham()) {
                    ReduceProductStockInputData reduceInput = new ReduceProductStockInputData(
                        item.getMaSanPham(),
                        item.getSoLuong()
                    );
                    var reduceResult = ((ReduceProductStockUseCaseControl) reduceStockUseCase)
                        .reduceStockInternal(reduceInput);
                    
                    if (!reduceResult.isSuccess()) {
                        throw new DomainException(reduceResult.getErrorMessage(), reduceResult.getErrorCode());
                    }
                }
                
                // UC-43: Clear cart after successful order creation
                ClearCartInputData clearInput = new ClearCartInputData(gioHang.getMaGioHang());
                ((ClearCartUseCaseControl) clearCartUseCase).clearInternal(clearInput);
                
                // UC-82: Format order items for checkout response
                FormatOrderItemsForCheckoutInputData formatInput = new FormatOrderItemsForCheckoutInputData(
                    savedOrder.getDanhSachSanPham()
                );
                var formatResult = ((FormatOrderItemsForCheckoutUseCaseControl) formatOrderItemsUseCase)
                    .formatInternal(formatInput);
                
                if (!formatResult.isSuccess()) {
                    throw new SystemException(formatResult.getErrorMessage(), formatResult.getErrorCode());
                }
                
                // Convert to CheckoutOutputData.OrderItemData
                List<CheckoutOutputData.OrderItemData> orderItems = formatResult.getFormattedItems().stream()
                    .map(item -> new CheckoutOutputData.OrderItemData(
                        Long.parseLong(item.getProductId()),
                        item.getProductName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getSubtotal()
                    ))
                    .collect(Collectors.toList());
                
                outputData = CheckoutOutputData.forSuccess(
                    savedOrder.getMaDonHang(),
                    savedOrder.getMaTaiKhoan(),
                    savedOrder.getTenNguoiNhan(),
                    savedOrder.getSoDienThoai(),
                    savedOrder.getDiaChiGiaoHang(),
                    savedOrder.getTrangThai().name(),
                    savedOrder.getTongTien(),
                    savedOrder.getDanhSachSanPham().size(),
                    orderItems,
                    savedOrder.getPhuongThucThanhToan().name(),
                    savedOrder.getPhuongThucThanhToan().getMoTa()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();
            
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            } else if (errorException instanceof com.motorbike.domain.exceptions.SystemException) {
                errorCode = ((com.motorbike.domain.exceptions.SystemException) errorException).getErrorCode();
            }
            
            outputData = CheckoutOutputData.forError(errorCode, message);
        }
        
        outputBoundary.present(outputData);
    }
}
