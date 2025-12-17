package com.motorbike.infrastructure.config;

import com.motorbike.adapters.presenters.AddAccessoryPresenter;
import com.motorbike.adapters.presenters.AddMotorbikePresenter;
import com.motorbike.adapters.presenters.AddToCartPresenter;
import com.motorbike.adapters.presenters.AddUserPresenter;
import com.motorbike.adapters.presenters.CancelOrderPresenter;
import com.motorbike.adapters.presenters.CheckInventoryAvailabilityPresenter;
import com.motorbike.adapters.presenters.CheckoutPresenter;
import com.motorbike.adapters.presenters.CreateAccessoryPresenter;
import com.motorbike.adapters.presenters.CreateUserPresenter;
import com.motorbike.adapters.presenters.DeleteAccessoryPresenter;
import com.motorbike.adapters.presenters.DeleteMotorbikePresenter;
import com.motorbike.adapters.presenters.DeleteUserPresenter;
import com.motorbike.adapters.presenters.GetAllAccessoriesPresenter;
import com.motorbike.adapters.presenters.GetAllMotorbikesPresenter;
import com.motorbike.adapters.presenters.GetAllProductsPresenter;
import com.motorbike.adapters.presenters.GetAllUsersPresenter;
import com.motorbike.adapters.presenters.GetOrderDetailPresenter;
import com.motorbike.adapters.presenters.GetTopProductsPresenter;
import com.motorbike.adapters.presenters.GetValidOrderStatusesPresenter;
import com.motorbike.adapters.presenters.ImportMotorbikesPresenter;
import com.motorbike.adapters.presenters.ImportAccessoriesPresenter;
import com.motorbike.adapters.presenters.ExportMotorbikesPresenter;
import com.motorbike.adapters.presenters.ExportAccessoriesPresenter;
import com.motorbike.adapters.presenters.ListAllOrdersPresenter;
import com.motorbike.adapters.presenters.LoginPresenter;
import com.motorbike.adapters.presenters.ProductDetailPresenter;
import com.motorbike.adapters.presenters.RegisterPresenter;
import com.motorbike.adapters.presenters.SearchAccessoriesPresenter;
import com.motorbike.adapters.presenters.SearchMotorbikesPresenter;
import com.motorbike.adapters.presenters.SearchOrdersPresenter;
import com.motorbike.adapters.presenters.SearchUsersPresenter;
import com.motorbike.adapters.presenters.ToggleProductVisibilityPresenter;
import com.motorbike.adapters.presenters.UpdateAccessoryPresenter;
import com.motorbike.adapters.presenters.UpdateCartQuantityPresenter;
import com.motorbike.adapters.presenters.UpdateMotorbikePresenter;
import com.motorbike.adapters.presenters.UpdateOrderPresenter;
import com.motorbike.adapters.presenters.UpdateUserPresenter;
import com.motorbike.adapters.presenters.ViewCartPresenter;
import com.motorbike.adapters.repositories.MotorbikeRepositoryAdapter;
import com.motorbike.adapters.viewmodels.AddAccessoryViewModel;
import com.motorbike.adapters.viewmodels.AddMotorbikeViewModel;
import com.motorbike.adapters.viewmodels.AddToCartViewModel;
import com.motorbike.adapters.viewmodels.AddUserViewModel;
import com.motorbike.adapters.viewmodels.CancelOrderViewModel;
import com.motorbike.adapters.viewmodels.CheckInventoryAvailabilityViewModel;
import com.motorbike.adapters.viewmodels.CheckoutViewModel;
import com.motorbike.adapters.viewmodels.CreateAccessoryViewModel;
import com.motorbike.adapters.viewmodels.CreateUserViewModel;
import com.motorbike.adapters.viewmodels.DeleteAccessoryViewModel;
import com.motorbike.adapters.viewmodels.DeleteMotorbikeViewModel;
import com.motorbike.adapters.viewmodels.DeleteUserViewModel;
import com.motorbike.adapters.viewmodels.GetAllAccessoriesViewModel;
import com.motorbike.adapters.viewmodels.GetAllMotorbikesViewModel;
import com.motorbike.adapters.viewmodels.GetAllProductsViewModel;
import com.motorbike.adapters.viewmodels.GetAllUsersViewModel;
import com.motorbike.adapters.viewmodels.GetOrderDetailViewModel;
import com.motorbike.adapters.viewmodels.GetTopProductsViewModel;
import com.motorbike.adapters.viewmodels.GetValidOrderStatusesViewModel;
import com.motorbike.adapters.viewmodels.ImportMotorbikesViewModel;
import com.motorbike.adapters.viewmodels.ImportAccessoriesViewModel;
import com.motorbike.adapters.viewmodels.ExportMotorbikesViewModel;
import com.motorbike.adapters.viewmodels.ExportAccessoriesViewModel;
import com.motorbike.adapters.viewmodels.ListAllOrdersViewModel;
import com.motorbike.adapters.viewmodels.LoginViewModel;
import com.motorbike.adapters.viewmodels.ProductDetailViewModel;
import com.motorbike.adapters.viewmodels.RegisterViewModel;
import com.motorbike.adapters.viewmodels.SearchAccessoriesViewModel;
import com.motorbike.adapters.viewmodels.SearchMotorbikesViewModel;
import com.motorbike.adapters.viewmodels.SearchOrdersViewModel;
import com.motorbike.adapters.viewmodels.SearchUsersViewModel;
import com.motorbike.adapters.viewmodels.ToggleProductVisibilityViewModel;
import com.motorbike.adapters.viewmodels.UpdateAccessoryViewModel;
import com.motorbike.adapters.viewmodels.UpdateCartQuantityViewModel;
import com.motorbike.adapters.viewmodels.UpdateMotorbikeViewModel;
import com.motorbike.adapters.viewmodels.UpdateOrderViewModel;
import com.motorbike.adapters.viewmodels.UpdateUserViewModel;
import com.motorbike.adapters.viewmodels.ViewCartViewModel;
import com.motorbike.business.ports.parser.ExcelParser;
import com.motorbike.business.ports.exporter.ExcelExporter;
import com.motorbike.business.ports.repository.AccessoryRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.control.AddAccessoryUseCaseControl;
import com.motorbike.business.usecase.control.AddMotorbikeUseCaseControl;
import com.motorbike.business.usecase.control.AddToCartUseCaseControl;
import com.motorbike.business.usecase.control.AddUserUseCaseControl;
import com.motorbike.business.usecase.control.CancelOrderUseCaseControl;
import com.motorbike.business.usecase.control.CheckoutUseCaseControl;
import com.motorbike.business.usecase.control.CreateAccessoryUseCaseControl;
import com.motorbike.business.usecase.control.CreateUserUseCaseControl;
import com.motorbike.business.usecase.control.DeleteAccessoryUseCaseControl;
import com.motorbike.business.usecase.control.DeleteMotorbikeUseCaseControl;
import com.motorbike.business.usecase.control.DeleteUserUseCaseControl;
import com.motorbike.business.usecase.control.GetAllAccessoriesUseCaseControl;
import com.motorbike.business.usecase.control.GetAllMotorbikesUseCaseControl;
import com.motorbike.business.usecase.control.GetAllProductsUseCaseControl;
import com.motorbike.business.usecase.control.GetAllUsersUseCaseControl;
import com.motorbike.business.usecase.control.GetOrderDetailUseCaseControl;
import com.motorbike.business.usecase.control.GetProductDetailUseCaseControl;
import com.motorbike.business.usecase.control.GetTopProductsUseCaseControl;
import com.motorbike.business.usecase.control.GetValidOrderStatusesUseCaseControl;
import com.motorbike.business.usecase.control.ImportMotorbikesUseCaseControl;
import com.motorbike.business.usecase.control.ImportAccessoriesUseCaseControl;
import com.motorbike.business.usecase.control.ExportMotorbikesUseCaseControl;
import com.motorbike.business.usecase.control.ExportAccessoriesUseCaseControl;
import com.motorbike.business.usecase.control.ListAllOrdersUseCaseControl;
import com.motorbike.business.usecase.control.LoginUseCaseControl;
import com.motorbike.business.usecase.control.RegisterUseCaseControl;
import com.motorbike.business.usecase.control.SearchAccessoriesUseCaseControl;
import com.motorbike.business.usecase.control.SearchMotorbikesUseCaseControl;
import com.motorbike.business.usecase.control.SearchOrdersUseCaseControl;
import com.motorbike.business.usecase.control.SearchUsersUseCaseControl;
import com.motorbike.business.usecase.control.ToggleProductVisibilityUseCaseControl;
import com.motorbike.business.usecase.control.UpdateAccessoryUseCaseControl;
import com.motorbike.business.usecase.control.UpdateCartQuantityUseCaseControl;
import com.motorbike.business.usecase.control.UpdateMotorbikeUseCaseControl;
import com.motorbike.business.usecase.control.UpdateOrderUseCaseControl;
import com.motorbike.business.usecase.control.UpdateUserUseCaseControl;
import com.motorbike.business.usecase.control.ViewCartUseCaseControl;
import com.motorbike.business.usecase.control.ValidateCartItemUseCaseControl;
import com.motorbike.business.usecase.control.FindCartItemUseCaseControl;
import com.motorbike.business.usecase.control.UpdateCartItemQuantityUseCaseControl;
import com.motorbike.business.usecase.control.CalculateCartTotalsUseCaseControl;
import com.motorbike.business.usecase.control.RemoveCartItemUseCaseControl;
import com.motorbike.business.usecase.control.ValidateOrderUseCaseControl;
import com.motorbike.business.usecase.control.ValidateCartBeforeCheckoutUseCaseControl;
import com.motorbike.business.usecase.control.CreateOrderFromCartUseCaseControl;
import com.motorbike.business.usecase.control.ReduceProductStockUseCaseControl;
import com.motorbike.business.usecase.control.UpdateProductStockUseCaseControl;
import com.motorbike.business.usecase.control.ClearCartUseCaseControl;
import com.motorbike.business.usecase.control.ValidateOrderCancellationUseCaseControl;
import com.motorbike.business.usecase.control.CalculateOrderTotalsUseCaseControl;
import com.motorbike.business.usecase.control.FormatOrderForDisplayUseCaseControl;
import com.motorbike.business.usecase.control.ValidateProductDataUseCaseControl;
import com.motorbike.business.usecase.control.CheckProductDuplicationUseCaseControl;
import com.motorbike.business.usecase.control.CheckInventoryAvailabilityUseCaseControl;
import com.motorbike.business.usecase.control.CalculateProductPriceUseCaseControl;
import com.motorbike.business.usecase.control.FormatProductForDisplayUseCaseControl;
import com.motorbike.business.usecase.control.CheckProductAvailabilityUseCaseControl;
import com.motorbike.business.usecase.control.ArchiveProductUseCaseControl;
import com.motorbike.business.usecase.control.ValidateUserRegistrationUseCaseControl;
import com.motorbike.business.usecase.control.CheckUserDuplicationUseCaseControl;
import com.motorbike.business.usecase.control.HashPasswordUseCaseControl;
import com.motorbike.business.usecase.control.VerifyPasswordUseCaseControl;
import com.motorbike.business.usecase.control.AssignUserRoleUseCaseControl;
import com.motorbike.business.usecase.control.CreateUserCartUseCaseControl;
import com.motorbike.business.usecase.control.MergeGuestCartUseCaseControl;
import com.motorbike.business.usecase.control.ApplyUserFiltersUseCaseControl;
import com.motorbike.business.usecase.control.FormatMotorbikesForDisplayUseCaseControl;
import com.motorbike.business.usecase.control.ValidateMotorbikeFieldsUseCaseControl;
import com.motorbike.business.usecase.control.FormatAccessoriesForDisplayUseCaseControl;
import com.motorbike.business.usecase.control.FormatProductsForDisplayUseCaseControl;
import com.motorbike.business.usecase.control.FormatCartItemsForDisplayUseCaseControl;
import com.motorbike.business.usecase.control.SortOrdersByDateUseCaseControl;
import com.motorbike.business.usecase.control.CalculateOrderStatisticsUseCaseControl;
import com.motorbike.business.usecase.control.FormatOrdersForListUseCaseControl;
import com.motorbike.business.usecase.control.FormatOrderItemsForCheckoutUseCaseControl;
import com.motorbike.business.usecase.control.ValidateExcelFileUseCaseControl;
import com.motorbike.business.usecase.control.ParseExcelDataUseCaseControl;
import com.motorbike.business.usecase.control.ValidateImportRowUseCaseControl;
import com.motorbike.business.usecase.control.GenerateImportReportUseCaseControl;
import com.motorbike.business.usecase.control.GenerateExcelFileUseCaseControl;
import com.motorbike.business.usecase.control.FormatDataForExportUseCaseControl;
import com.motorbike.business.usecase.control.BuildSearchCriteriaUseCaseControl;
import com.motorbike.business.usecase.control.ApplySearchFiltersUseCaseControl;
import com.motorbike.business.usecase.control.SortSearchResultsUseCaseControl;
import com.motorbike.business.usecase.input.AddToCartInputBoundary;
import com.motorbike.business.usecase.input.CancelOrderInputBoundary;
import com.motorbike.business.usecase.input.CheckoutInputBoundary;
import com.motorbike.business.usecase.input.DeleteMotorbikeInputBoundary;
import com.motorbike.business.usecase.input.GetAllAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.GetAllMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.GetAllProductsInputBoundary;
import com.motorbike.business.usecase.input.GetOrderDetailInputBoundary;
import com.motorbike.business.usecase.input.GetProductDetailInputBoundary;
import com.motorbike.business.usecase.input.GetValidOrderStatusesInputBoundary;
import com.motorbike.business.usecase.input.ImportMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.ImportAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.ExportMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.ExportAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.ListAllOrdersInputBoundary;
import com.motorbike.business.usecase.input.LoginInputBoundary;
import com.motorbike.business.usecase.input.RegisterInputBoundary;
import com.motorbike.business.usecase.input.SearchAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.SearchMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.ToggleProductVisibilityInputBoundary;
import com.motorbike.business.usecase.input.UpdateCartQuantityInputBoundary;
import com.motorbike.business.usecase.input.ViewCartInputBoundary;
import com.motorbike.business.usecase.input.ValidateCartItemInputBoundary;
import com.motorbike.business.usecase.input.FindCartItemInputBoundary;
import com.motorbike.business.usecase.input.UpdateCartItemQuantityInputBoundary;
import com.motorbike.business.usecase.input.CalculateCartTotalsInputBoundary;
import com.motorbike.business.usecase.input.RemoveCartItemInputBoundary;
import com.motorbike.business.usecase.input.ValidateOrderInputBoundary;
import com.motorbike.business.usecase.input.CreateOrderFromCartInputBoundary;
import com.motorbike.business.usecase.input.UpdateProductStockInputBoundary;
import com.motorbike.business.usecase.input.ClearCartInputBoundary;
import com.motorbike.business.usecase.input.ValidateOrderCancellationInputBoundary;
import com.motorbike.business.usecase.input.CalculateOrderTotalsInputBoundary;
import com.motorbike.business.usecase.input.FormatOrderForDisplayInputBoundary;
import com.motorbike.business.usecase.input.ValidateProductDataInputBoundary;
import com.motorbike.business.usecase.input.CheckProductDuplicationInputBoundary;
import com.motorbike.business.usecase.input.CalculateProductPriceInputBoundary;
import com.motorbike.business.usecase.input.FormatProductForDisplayInputBoundary;
import com.motorbike.business.usecase.input.CheckProductAvailabilityInputBoundary;
import com.motorbike.business.usecase.input.ArchiveProductInputBoundary;
import com.motorbike.business.usecase.input.ValidateUserRegistrationInputBoundary;
import com.motorbike.business.usecase.input.CheckUserDuplicationInputBoundary;
import com.motorbike.business.usecase.input.CheckInventoryAvailabilityInputBoundary;
import com.motorbike.business.usecase.input.HashPasswordInputBoundary;
import com.motorbike.business.usecase.input.VerifyPasswordInputBoundary;
import com.motorbike.business.usecase.input.AssignUserRoleInputBoundary;
import com.motorbike.business.usecase.input.CreateUserCartInputBoundary;
import com.motorbike.business.usecase.input.MergeGuestCartInputBoundary;
import com.motorbike.business.usecase.input.ApplyUserFiltersInputBoundary;
import com.motorbike.business.usecase.input.FormatMotorbikesForDisplayInputBoundary;
import com.motorbike.business.usecase.input.ValidateMotorbikeFieldsInputBoundary;
import com.motorbike.business.usecase.input.FormatAccessoriesForDisplayInputBoundary;
import com.motorbike.business.usecase.input.FormatProductsForDisplayInputBoundary;
import com.motorbike.business.usecase.input.FormatCartItemsForDisplayInputBoundary;
import com.motorbike.business.usecase.input.SortOrdersByDateInputBoundary;
import com.motorbike.business.usecase.input.CalculateOrderStatisticsInputBoundary;
import com.motorbike.business.usecase.input.FormatOrdersForListInputBoundary;
import com.motorbike.business.usecase.input.FormatOrderItemsForCheckoutInputBoundary;
import com.motorbike.business.usecase.input.ValidateCartBeforeCheckoutInputBoundary;
import com.motorbike.business.usecase.input.CreateOrderFromCartInputBoundary;
import com.motorbike.business.usecase.input.ReduceProductStockInputBoundary;
import com.motorbike.business.usecase.input.ClearCartInputBoundary;
import com.motorbike.business.usecase.input.ValidateExcelFileInputBoundary;
import com.motorbike.business.usecase.input.ParseExcelDataInputBoundary;
import com.motorbike.business.usecase.input.ValidateImportRowInputBoundary;
import com.motorbike.business.usecase.input.GenerateImportReportInputBoundary;
import com.motorbike.business.usecase.input.GenerateExcelFileInputBoundary;
import com.motorbike.business.usecase.input.FormatDataForExportInputBoundary;
import com.motorbike.business.usecase.input.BuildSearchCriteriaInputBoundary;
import com.motorbike.business.usecase.input.ApplySearchFiltersInputBoundary;
import com.motorbike.business.usecase.input.SortSearchResultsInputBoundary;
import com.motorbike.business.usecase.output.AddAccessoryOutputBoundary;
import com.motorbike.business.usecase.output.AddMotorbikeOutputBoundary;
import com.motorbike.business.usecase.output.AddUserOutputBoundary;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;
import com.motorbike.business.usecase.output.CheckInventoryAvailabilityOutputBoundary;
import com.motorbike.business.usecase.output.CheckoutOutputBoundary;
import com.motorbike.business.usecase.output.CreateAccessoryOutputBoundary;
import com.motorbike.business.usecase.output.CreateUserOutputBoundary;
import com.motorbike.business.usecase.output.DeleteAccessoryOutputBoundary;
import com.motorbike.business.usecase.output.DeleteMotorbikeOutputBoundary;
import com.motorbike.business.usecase.output.DeleteUserOutputBoundary;
import com.motorbike.business.usecase.output.GetAllAccessoriesOutputBoundary;
import com.motorbike.business.usecase.output.GetAllMotorbikesOutputBoundary;
import com.motorbike.business.usecase.output.GetAllProductsOutputBoundary;
import com.motorbike.business.usecase.output.GetAllUsersOutputBoundary;
import com.motorbike.business.usecase.output.GetOrderDetailOutputBoundary;
import com.motorbike.business.usecase.output.GetProductDetailOutputBoundary;
import com.motorbike.business.usecase.output.GetTopProductsOutputBoundary;
import com.motorbike.business.usecase.output.GetValidOrderStatusesOutputBoundary;
import com.motorbike.business.usecase.output.ImportMotorbikesOutputBoundary;
import com.motorbike.business.usecase.output.ImportAccessoriesOutputBoundary;
import com.motorbike.business.usecase.output.ExportMotorbikesOutputBoundary;
import com.motorbike.business.usecase.output.ExportAccessoriesOutputBoundary;
import com.motorbike.business.usecase.output.ListAllOrdersOutputBoundary;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.business.usecase.output.RegisterOutputBoundary;
import com.motorbike.business.usecase.output.SearchAccessoriesOutputBoundary;
import com.motorbike.business.usecase.output.SearchMotorbikesOutputBoundary;
import com.motorbike.business.usecase.output.SearchOrdersOutputBoundary;
import com.motorbike.business.usecase.output.SearchUsersOutputBoundary;
import com.motorbike.business.usecase.output.ToggleProductVisibilityOutputBoundary;
import com.motorbike.business.usecase.output.UpdateAccessoryOutputBoundary;
import com.motorbike.business.usecase.output.UpdateCartQuantityOutputBoundary;
import com.motorbike.business.usecase.output.UpdateMotorbikeOutputBoundary;
import com.motorbike.business.usecase.output.UpdateOrderOutputBoundary;
import com.motorbike.business.usecase.output.UpdateUserOutputBoundary;
import com.motorbike.business.usecase.output.ViewCartOutputBoundary;
import com.motorbike.business.usecase.output.CreateUserCartOutputBoundary;
import com.motorbike.business.usecase.output.MergeGuestCartOutputBoundary;
import com.motorbike.business.usecase.output.ApplyUserFiltersOutputBoundary;
import com.motorbike.business.usecase.output.FormatMotorbikesForDisplayOutputBoundary;
import com.motorbike.business.usecase.output.ValidateMotorbikeFieldsOutputBoundary;
import com.motorbike.business.usecase.output.FormatAccessoriesForDisplayOutputBoundary;
import com.motorbike.business.usecase.output.FormatProductsForDisplayOutputBoundary;
import com.motorbike.business.usecase.output.FormatCartItemsForDisplayOutputBoundary;
import com.motorbike.business.usecase.output.SortOrdersByDateOutputBoundary;
import com.motorbike.business.usecase.output.CalculateOrderStatisticsOutputBoundary;
import com.motorbike.business.usecase.output.FormatOrdersForListOutputBoundary;
import com.motorbike.business.usecase.output.FormatOrderItemsForCheckoutOutputBoundary;
import com.motorbike.infrastructure.persistence.jpa.repositories.XeMayJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class UseCaseConfig {
    
    @Bean
    @RequestScope
    public LoginViewModel loginViewModel() {
        return new LoginViewModel();
    }
    
    @Bean
    @RequestScope
    public RegisterViewModel registerViewModel() {
        return new RegisterViewModel();
    }
    
    @Bean
    @RequestScope
    public ProductDetailViewModel productDetailViewModel() {
        return new ProductDetailViewModel();
    }
    
    @Bean
    @RequestScope
    public AddToCartViewModel addToCartViewModel() {
        return new AddToCartViewModel();
    }
    
    @Bean
    @RequestScope
    public CheckInventoryAvailabilityViewModel checkInventoryAvailabilityViewModel() {
        return new CheckInventoryAvailabilityViewModel();
    }
    
    @Bean
    @RequestScope
    public ViewCartViewModel viewCartViewModel() {
        return new ViewCartViewModel();
    }
    
    @Bean
    @RequestScope
    public UpdateCartQuantityViewModel updateCartQuantityViewModel() {
        return new UpdateCartQuantityViewModel();
    }
    
    @Bean
    @RequestScope
    public CheckoutViewModel checkoutViewModel() {
        return new CheckoutViewModel();
    }
    
    @Bean
    public LoginInputBoundary loginUseCase(
            LoginOutputBoundary loginPresenter,
            UserRepository userRepository,
            CartRepository cartRepository,
            VerifyPasswordInputBoundary verifyPasswordUseCase,
            CreateUserCartInputBoundary createUserCartUseCase,
            MergeGuestCartInputBoundary mergeGuestCartUseCase) {
        return new LoginUseCaseControl(loginPresenter, userRepository, cartRepository, verifyPasswordUseCase, createUserCartUseCase, mergeGuestCartUseCase);
    }
    
    @Bean
    public LoginOutputBoundary loginPresenter(LoginViewModel loginViewModel) {
        return new LoginPresenter(loginViewModel);
    }
    
    @Bean
    public RegisterInputBoundary registerUseCase(
            RegisterOutputBoundary registerPresenter,
            UserRepository userRepository,
            CartRepository cartRepository,
            ValidateUserRegistrationInputBoundary validateUserRegistrationUseCase,
            CheckUserDuplicationInputBoundary checkUserDuplicationUseCase,
            HashPasswordInputBoundary hashPasswordUseCase,
            CreateUserCartInputBoundary createUserCartUseCase) {
        return new RegisterUseCaseControl(registerPresenter, userRepository, cartRepository, validateUserRegistrationUseCase, checkUserDuplicationUseCase, hashPasswordUseCase, createUserCartUseCase);
    }
    
    @Bean
    public RegisterOutputBoundary registerPresenter(RegisterViewModel registerViewModel) {
        return new RegisterPresenter(registerViewModel);
    }
    
    @Bean
    public GetProductDetailInputBoundary getProductDetailUseCase(
            GetProductDetailOutputBoundary productDetailPresenter,
            ProductRepository productRepository,
            CalculateProductPriceInputBoundary calculatePriceUseCase) {
        return new GetProductDetailUseCaseControl(productDetailPresenter, productRepository, calculatePriceUseCase);
    }
    
    @Bean
    public GetProductDetailOutputBoundary productDetailPresenter(ProductDetailViewModel productDetailViewModel) {
        return new ProductDetailPresenter(productDetailViewModel);
    }
    
    @Bean
    public AddToCartInputBoundary addToCartUseCase(
            AddToCartOutputBoundary addToCartPresenter,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        return new AddToCartUseCaseControl(addToCartPresenter, cartRepository, productRepository);
    }
    
    @Bean
    public AddToCartOutputBoundary addToCartPresenter(AddToCartViewModel addToCartViewModel) {
        return new AddToCartPresenter(addToCartViewModel);
    }
    
    @Bean
    public ViewCartInputBoundary viewCartUseCase(
            ViewCartOutputBoundary viewCartPresenter,
            CartRepository cartRepository,
            ProductRepository productRepository,
            CalculateCartTotalsInputBoundary calculateCartTotalsUseCase,
            FormatCartItemsForDisplayInputBoundary formatCartItemsUseCase) {
        return new ViewCartUseCaseControl(viewCartPresenter, cartRepository, productRepository, calculateCartTotalsUseCase, formatCartItemsUseCase);
    }
    
    @Bean
    public ViewCartOutputBoundary viewCartPresenter(ViewCartViewModel viewCartViewModel) {
        return new ViewCartPresenter(viewCartViewModel);
    }
    
    @Bean
    public CheckInventoryAvailabilityInputBoundary checkInventoryAvailabilityUseCase(
            CheckInventoryAvailabilityOutputBoundary checkInventoryPresenter,
            ProductRepository productRepository) {
        return new CheckInventoryAvailabilityUseCaseControl(checkInventoryPresenter, productRepository);
    }
    
    @Bean
    public CheckInventoryAvailabilityOutputBoundary checkInventoryPresenter(CheckInventoryAvailabilityViewModel checkInventoryViewModel) {
        return new CheckInventoryAvailabilityPresenter(checkInventoryViewModel);
    }
    
    @Bean
    public UpdateCartQuantityInputBoundary updateCartQuantityUseCase(
            UpdateCartQuantityOutputBoundary updateCartQuantityPresenter,
            CartRepository cartRepository,
            CheckInventoryAvailabilityInputBoundary checkInventoryUseCase,
            CalculateCartTotalsInputBoundary calculateCartTotalsUseCase) {
        return new UpdateCartQuantityUseCaseControl(
            updateCartQuantityPresenter, 
            cartRepository,
            checkInventoryUseCase,
            calculateCartTotalsUseCase
        );
    }
    
    @Bean
    public UpdateCartQuantityOutputBoundary updateCartQuantityPresenter(UpdateCartQuantityViewModel updateCartQuantityViewModel) {
        return new UpdateCartQuantityPresenter(updateCartQuantityViewModel);
    }
    
    @Bean
    public CheckoutInputBoundary checkoutUseCase(
            CheckoutOutputBoundary checkoutPresenter,
            CartRepository cartRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            ValidateCartBeforeCheckoutInputBoundary validateCartUseCase,
            CreateOrderFromCartInputBoundary createOrderUseCase,
            ReduceProductStockInputBoundary reduceStockUseCase,
            ClearCartInputBoundary clearCartUseCase,
            FormatOrderItemsForCheckoutInputBoundary formatOrderItemsUseCase) {
        return new CheckoutUseCaseControl(
            checkoutPresenter, 
            cartRepository, 
            orderRepository,
            validateCartUseCase,
            createOrderUseCase,
            reduceStockUseCase,
            clearCartUseCase,
            formatOrderItemsUseCase
        );
    }
    
    
    @Bean
    public CheckoutOutputBoundary checkoutPresenter(CheckoutViewModel checkoutViewModel) {
        return new CheckoutPresenter(checkoutViewModel);
    }

    @Bean
    @RequestScope
    public ListAllOrdersViewModel listAllOrdersViewModel() {
        return new ListAllOrdersViewModel();
    }

    @Bean
    public ListAllOrdersInputBoundary listAllOrdersUseCase(
            ListAllOrdersOutputBoundary listAllOrdersPresenter,
            OrderRepository orderRepository,
            SortOrdersByDateInputBoundary sortOrdersUseCase,
            FormatOrdersForListInputBoundary formatOrdersUseCase) {
        return new ListAllOrdersUseCaseControl(listAllOrdersPresenter, orderRepository, sortOrdersUseCase, formatOrdersUseCase);
    }

    @Bean
    public ListAllOrdersOutputBoundary listAllOrdersPresenter(ListAllOrdersViewModel listAllOrdersViewModel) {
        return new ListAllOrdersPresenter(listAllOrdersViewModel);
    }

    @Bean
    @RequestScope
    public CancelOrderViewModel cancelOrderViewModel() {
        return new CancelOrderViewModel();
    }

    @Bean
    public CancelOrderInputBoundary cancelOrderUseCase(
            CancelOrderOutputBoundary cancelOrderPresenter,
            OrderRepository orderRepository,
            ProductRepository productRepository) {
        return new CancelOrderUseCaseControl(cancelOrderPresenter, orderRepository, productRepository);
    }

    @Bean
    public CancelOrderOutputBoundary cancelOrderPresenter(CancelOrderViewModel cancelOrderViewModel) {
        return new CancelOrderPresenter(cancelOrderViewModel);
    }

    @Bean
    public CancelOrderUseCaseControl cancelOrderUseCaseControl(
            CancelOrderOutputBoundary cancelOrderPresenter,
            OrderRepository orderRepository,
            ProductRepository productRepository) {
        return new CancelOrderUseCaseControl(cancelOrderPresenter, orderRepository, productRepository);
    }

    //motorbike use case beans

    @Bean
    @RequestScope
    public GetAllMotorbikesViewModel getAllMotorbikesViewModel() {
        return new GetAllMotorbikesViewModel();
    }

    @Bean
    public GetAllMotorbikesUseCaseControl getAllMotorbikesUseCase(
            GetAllMotorbikesOutputBoundary presenter,
            ProductRepository productRepository,
            FormatMotorbikesForDisplayInputBoundary formatMotorbikesForDisplayUseCase
    ) {
        return new GetAllMotorbikesUseCaseControl(presenter, productRepository, formatMotorbikesForDisplayUseCase);
    }

    @Bean
    public GetAllMotorbikesOutputBoundary getAllMotorbikesPresenter(
            GetAllMotorbikesViewModel viewModel
    ) {
        return new GetAllMotorbikesPresenter(viewModel);
    }

    @Bean
    public GetAllMotorbikesInputBoundary getAllMotorbikesInputBoundary(
            GetAllMotorbikesUseCaseControl useCase
    ) {
        return useCase;
    }

    //search motorbike use case beans

    @Bean
    public SearchMotorbikesInputBoundary searchMotorbikesUseCase(
            SearchMotorbikesOutputBoundary outputBoundary,
            ProductRepository productRepository,
            BuildSearchCriteriaInputBoundary buildSearchCriteriaUseCase,
            ApplySearchFiltersInputBoundary applySearchFiltersUseCase,
            SortSearchResultsInputBoundary sortSearchResultsUseCase,
            FormatMotorbikesForDisplayInputBoundary formatMotorbikesForDisplayUseCase
    ) {
        return new SearchMotorbikesUseCaseControl(outputBoundary, productRepository, buildSearchCriteriaUseCase, applySearchFiltersUseCase, sortSearchResultsUseCase, formatMotorbikesForDisplayUseCase);
    }

    @Bean
    public SearchMotorbikesOutputBoundary searchMotorbikesOutputBoundary(
            SearchMotorbikesViewModel viewModel
    ) {
        return new SearchMotorbikesPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public SearchMotorbikesViewModel searchMotorbikesViewModel() {
        return new SearchMotorbikesViewModel();
    }

    // ADD MOTORBIKE USE CASE BEANS

    @Bean
    @RequestScope
    public AddMotorbikeViewModel addMotorbikeViewModel() {
        return new AddMotorbikeViewModel();
    }

    @Bean
    public AddMotorbikeOutputBoundary addMotorbikePresenter(
            AddMotorbikeViewModel viewModel
    ) {
        return new AddMotorbikePresenter(viewModel);
    }

    @Bean
    public AddMotorbikeUseCaseControl addMotorbikeUseCase(
            AddMotorbikeOutputBoundary presenter,
            MotorbikeRepository motorbikeRepository,
            ValidateMotorbikeFieldsInputBoundary validateMotorbikeFieldsUseCase
    ) {
        return new AddMotorbikeUseCaseControl(presenter, motorbikeRepository, validateMotorbikeFieldsUseCase);
    }

    // IMPORT MOTORBIKES USE CASE BEANS

    @Bean
    @RequestScope
    public ImportMotorbikesViewModel importMotorbikesViewModel() {
        return new ImportMotorbikesViewModel();
    }

    @Bean
    public ImportMotorbikesOutputBoundary importMotorbikesPresenter(
            ImportMotorbikesViewModel viewModel
    ) {
        return new ImportMotorbikesPresenter(viewModel);
    }

    @Bean
    public ImportMotorbikesUseCaseControl importMotorbikesUseCase(
            ImportMotorbikesOutputBoundary presenter,
            MotorbikeRepository motorbikeRepository,
            ExcelParser excelParser,
            ValidateExcelFileInputBoundary validateExcelFileUseCase,
            ParseExcelDataInputBoundary parseExcelDataUseCase,
            ValidateImportRowInputBoundary validateImportRowUseCase,
            GenerateImportReportInputBoundary generateImportReportUseCase
    ) {
        return new ImportMotorbikesUseCaseControl(presenter, motorbikeRepository, excelParser, validateExcelFileUseCase, parseExcelDataUseCase, validateImportRowUseCase, generateImportReportUseCase);
    }

    @Bean
    public ImportMotorbikesInputBoundary importMotorbikesInputBoundary(
            ImportMotorbikesUseCaseControl useCase
    ) {
        return useCase;
    }

    // ============================
    // IMPORT ACCESSORIES USE CASE
    // ============================

    @Bean
    @RequestScope
    public ImportAccessoriesViewModel importAccessoriesViewModel() {
        return new ImportAccessoriesViewModel();
    }

    @Bean
    public ImportAccessoriesOutputBoundary importAccessoriesPresenter(
            ImportAccessoriesViewModel viewModel
    ) {
        return new ImportAccessoriesPresenter(viewModel);
    }

    @Bean
    public ImportAccessoriesUseCaseControl importAccessoriesUseCase(
            ImportAccessoriesOutputBoundary presenter,
            AccessoryRepository accessoryRepository,
            ExcelParser excelParser,
            @Lazy ValidateExcelFileInputBoundary validateExcelFileUseCase,
            @Lazy ParseExcelDataInputBoundary parseExcelDataUseCase
    ) {
        return new ImportAccessoriesUseCaseControl(presenter, accessoryRepository, excelParser, validateExcelFileUseCase, parseExcelDataUseCase);
    }

    @Bean
    public ImportAccessoriesInputBoundary importAccessoriesInputBoundary(
            ImportAccessoriesUseCaseControl useCase
    ) {
        return useCase;
    }

    // ============================
    // EXPORT MOTORBIKES USE CASE
    // ============================
    @Bean
    @RequestScope
    public ExportMotorbikesViewModel exportMotorbikesViewModel() {
        return new ExportMotorbikesViewModel();
    }

    @Bean
    public ExportMotorbikesOutputBoundary exportMotorbikesPresenter(
            ExportMotorbikesViewModel viewModel
    ) {
        return new ExportMotorbikesPresenter(viewModel);
    }

    @Bean
    public ExportMotorbikesUseCaseControl exportMotorbikesUseCase(
            ExportMotorbikesOutputBoundary presenter,
            MotorbikeRepository motorbikeRepository,
            ExcelExporter excelExporter
    ) {
        return new ExportMotorbikesUseCaseControl(presenter, motorbikeRepository, excelExporter);
    }

    @Bean
    public ExportMotorbikesInputBoundary exportMotorbikesInputBoundary(
            ExportMotorbikesUseCaseControl useCase
    ) {
        return useCase;
    }

    // ============================
    // EXPORT ACCESSORIES USE CASE
    // ============================
    @Bean
    @RequestScope
    public ExportAccessoriesViewModel exportAccessoriesViewModel() {
        return new ExportAccessoriesViewModel();
    }

    @Bean
    public ExportAccessoriesOutputBoundary exportAccessoriesPresenter(
            ExportAccessoriesViewModel viewModel
    ) {
        return new ExportAccessoriesPresenter(viewModel);
    }

    @Bean
    public ExportAccessoriesUseCaseControl exportAccessoriesUseCase(
            ExportAccessoriesOutputBoundary presenter,
            AccessoryRepository accessoryRepository,
            ExcelExporter excelExporter
    ) {
        return new ExportAccessoriesUseCaseControl(presenter, accessoryRepository, excelExporter);
    }

    @Bean
    public ExportAccessoriesInputBoundary exportAccessoriesInputBoundary(
            ExportAccessoriesUseCaseControl useCase
    ) {
        return useCase;
    }

    @Bean
    @RequestScope
    public GetAllAccessoriesViewModel getAllAccessoriesViewModel() {
        return new GetAllAccessoriesViewModel();
    }

    @Bean
    public GetAllAccessoriesUseCaseControl getAllAccessoriesUseCase(
            GetAllAccessoriesOutputBoundary presenter,
            ProductRepository productRepository,
            FormatAccessoriesForDisplayInputBoundary formatAccessoriesForDisplayUseCase
    ) {
        return new GetAllAccessoriesUseCaseControl(presenter, productRepository, formatAccessoriesForDisplayUseCase);
    }

    @Bean
    public GetAllAccessoriesOutputBoundary getAllAccessoriesPresenter(
            GetAllAccessoriesViewModel viewModel
    ) {
        return new GetAllAccessoriesPresenter(viewModel);
    }

    @Bean
    public GetAllAccessoriesInputBoundary getAllAccessoriesInputBoundary(
            GetAllAccessoriesUseCaseControl useCase
    ) {
        return useCase;
    }

    @Bean
    public MotorbikeRepository motorbikeRepository(
            XeMayJpaRepository jpaRepository
    ) {
        return new MotorbikeRepositoryAdapter(jpaRepository);
    }

    @Bean
    public SearchAccessoriesInputBoundary searchAccessoriesUseCase(
            SearchAccessoriesOutputBoundary outputBoundary,
            ProductRepository productRepository,
            FormatAccessoriesForDisplayInputBoundary formatAccessoriesForDisplayUseCase
    ) {
        return new SearchAccessoriesUseCaseControl(outputBoundary, productRepository, formatAccessoriesForDisplayUseCase);
    }

    @Bean
    public SearchAccessoriesOutputBoundary searchAccessoriesOutputBoundary(
            SearchAccessoriesViewModel viewModel
    ) {
        return new SearchAccessoriesPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public SearchAccessoriesViewModel searchAccessoriesViewModel() {
        return new SearchAccessoriesViewModel();
    }

    // ===== USER MANAGEMENT USE CASES =====

    @Bean
    @RequestScope
    public GetAllUsersViewModel getAllUsersViewModel() {
        return new GetAllUsersViewModel();
    }

    @Bean
    public GetAllUsersUseCaseControl getAllUsersUseCase(
            GetAllUsersOutputBoundary presenter,
            UserRepository userRepository
    ) {
        return new GetAllUsersUseCaseControl(presenter, userRepository);
    }

    @Bean
    public GetAllUsersOutputBoundary getAllUsersPresenter(
            GetAllUsersViewModel viewModel
    ) {
        return new GetAllUsersPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public SearchUsersViewModel searchUsersViewModel() {
        return new SearchUsersViewModel();
    }

    @Bean
    public SearchUsersUseCaseControl searchUsersUseCase(
            SearchUsersOutputBoundary presenter,
            UserRepository userRepository,
            ApplyUserFiltersInputBoundary applyUserFiltersUseCase
    ) {
        return new SearchUsersUseCaseControl(presenter, userRepository, applyUserFiltersUseCase);
    }

    @Bean
    public SearchUsersOutputBoundary searchUsersPresenter(
            SearchUsersViewModel viewModel
    ) {
        return new SearchUsersPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public DeleteUserViewModel deleteUserViewModel() {
        return new DeleteUserViewModel();
    }

    @Bean
    public DeleteUserUseCaseControl deleteUserUseCase(
            DeleteUserOutputBoundary presenter,
            UserRepository userRepository
    ) {
        return new DeleteUserUseCaseControl(presenter, userRepository);
    }

    @Bean
    public DeleteUserOutputBoundary deleteUserPresenter(
            DeleteUserViewModel viewModel
    ) {
        return new DeleteUserPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public AddUserViewModel addUserViewModel() {
        return new AddUserViewModel();
    }

@Bean
@RequestScope
public CreateUserViewModel createUserViewModel() {
    return new CreateUserViewModel();
}

@Bean
public AddUserUseCaseControl addUserUseCase(
        AddUserOutputBoundary presenter,
        UserRepository userRepository,
        CheckUserDuplicationInputBoundary checkUserDuplicationUseCase
) {
    return new AddUserUseCaseControl(presenter, userRepository, checkUserDuplicationUseCase);
}

@Bean
public AddUserOutputBoundary addUserOutputBoundary(
        AddUserViewModel viewModel
) {
    return new AddUserPresenter(viewModel);
}

@Bean
public CreateUserUseCaseControl createUserUseCaseControl(
        CreateUserOutputBoundary presenter,
        UserRepository userRepository,
        ValidateUserRegistrationInputBoundary validateUserRegistrationUseCase,
        CheckUserDuplicationInputBoundary checkUserDuplicationUseCase,
        HashPasswordInputBoundary hashPasswordUseCase
) {
    return new CreateUserUseCaseControl(presenter, userRepository, validateUserRegistrationUseCase, checkUserDuplicationUseCase, hashPasswordUseCase);
}

@Bean
public CreateUserOutputBoundary createUserPresenter(
        CreateUserViewModel viewModel
) {
    return new CreateUserPresenter(viewModel);
}    @Bean
    @RequestScope
    public UpdateUserViewModel updateUserViewModel() {
        return new UpdateUserViewModel();
    }

    @Bean
    public UpdateUserUseCaseControl updateUserUseCase(
            UpdateUserOutputBoundary presenter,
            UserRepository userRepository,
            CheckUserDuplicationInputBoundary checkUserDuplicationUseCase
    ) {
        return new UpdateUserUseCaseControl(presenter, userRepository, checkUserDuplicationUseCase);
    }

    @Bean
    public UpdateUserOutputBoundary updateUserPresenter(
            UpdateUserViewModel viewModel
    ) {
        return new UpdateUserPresenter(viewModel);
    }

    // ===== MOTORBIKE MANAGEMENT USE CASES =====

    @Bean
    @RequestScope
    public DeleteMotorbikeViewModel deleteMotorbikeViewModel() {
        return new DeleteMotorbikeViewModel();
    }

    @Bean
    public DeleteMotorbikeUseCaseControl deleteMotorbikeUseCase(
            DeleteMotorbikeOutputBoundary presenter,
            ProductRepository productRepository,
            ArchiveProductInputBoundary archiveProductUseCase
    ) {
        return new DeleteMotorbikeUseCaseControl(presenter, productRepository, archiveProductUseCase);
    }

    @Bean
    public DeleteMotorbikeOutputBoundary deleteMotorbikePresenter(
            DeleteMotorbikeViewModel viewModel
    ) {
        return new DeleteMotorbikePresenter(viewModel);
    }

    @Bean
    @RequestScope
    public UpdateMotorbikeViewModel updateMotorbikeViewModel() {
        return new UpdateMotorbikeViewModel();
    }

    @Bean
    public UpdateMotorbikeUseCaseControl updateMotorbikeUseCase(
            UpdateMotorbikeOutputBoundary presenter,
            ProductRepository productRepository,
            ValidateProductDataInputBoundary validateProductDataUseCase,
            CheckProductDuplicationInputBoundary checkDuplicationUseCase
    ) {
        return new UpdateMotorbikeUseCaseControl(presenter, productRepository, validateProductDataUseCase, checkDuplicationUseCase);
    }

    @Bean
    public UpdateMotorbikeOutputBoundary updateMotorbikePresenter(
            UpdateMotorbikeViewModel viewModel
    ) {
        return new UpdateMotorbikePresenter(viewModel);
    }

    // ===== ACCESSORY MANAGEMENT USE CASES =====

    @Bean
    @RequestScope
    public AddAccessoryViewModel addAccessoryViewModel() {
        return new AddAccessoryViewModel();
    }

    @Bean
    @RequestScope
    public CreateAccessoryViewModel createAccessoryViewModel() {
        return new CreateAccessoryViewModel();
    }

    @Bean
    public CreateAccessoryUseCaseControl createAccessoryUseCaseControl(
            CreateAccessoryOutputBoundary presenter,
            ProductRepository productRepository
    ) {
        return new CreateAccessoryUseCaseControl(presenter, productRepository);
    }

    @Bean
    public AddAccessoryUseCaseControl addAccessoryUseCase(
            AddAccessoryOutputBoundary presenter,
            AccessoryRepository accessoryRepository
    ) {
        return new AddAccessoryUseCaseControl(presenter, accessoryRepository);
    }

    @Bean
    public AddAccessoryOutputBoundary addAccessoryOutputBoundary(
            AddAccessoryViewModel viewModel
    ) {
        return new AddAccessoryPresenter(viewModel);
    }

    @Bean
    public CreateAccessoryOutputBoundary createAccessoryPresenter(
            CreateAccessoryViewModel viewModel
    ) {
        return new CreateAccessoryPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public UpdateAccessoryViewModel updateAccessoryViewModel() {
        return new UpdateAccessoryViewModel();
    }

    @Bean
    public UpdateAccessoryUseCaseControl updateAccessoryUseCase(
            UpdateAccessoryOutputBoundary presenter,
            ProductRepository productRepository,
            ValidateProductDataInputBoundary validateProductDataUseCase,
            @Lazy CheckProductDuplicationInputBoundary checkDuplicationUseCase
    ) {
        return new UpdateAccessoryUseCaseControl(presenter, productRepository, validateProductDataUseCase, checkDuplicationUseCase);
    }

    @Bean
    public UpdateAccessoryOutputBoundary updateAccessoryPresenter(
            UpdateAccessoryViewModel viewModel
    ) {
        return new UpdateAccessoryPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public DeleteAccessoryViewModel deleteAccessoryViewModel() {
        return new DeleteAccessoryViewModel();
    }

    @Bean
    public DeleteAccessoryUseCaseControl deleteAccessoryUseCase(
            DeleteAccessoryOutputBoundary presenter,
            ProductRepository productRepository,
            ArchiveProductInputBoundary archiveProductUseCase
    ) {
        return new DeleteAccessoryUseCaseControl(presenter, productRepository, archiveProductUseCase);
    }

    @Bean
    public DeleteAccessoryOutputBoundary deleteAccessoryPresenter(
            DeleteAccessoryViewModel viewModel
    ) {
        return new DeleteAccessoryPresenter(viewModel);
    }

    // ===== ORDER MANAGEMENT USE CASES =====

    @Bean
    @RequestScope
    public SearchOrdersViewModel searchOrdersViewModel() {
        return new SearchOrdersViewModel();
    }

    @Bean
    public SearchOrdersUseCaseControl searchOrdersUseCase(
            SearchOrdersOutputBoundary presenter,
            OrderRepository orderRepository,
            UserRepository userRepository
    ) {
        return new SearchOrdersUseCaseControl(presenter, orderRepository, userRepository);
    }

    @Bean
    public SearchOrdersOutputBoundary searchOrdersPresenter(
            SearchOrdersViewModel viewModel
    ) {
        return new SearchOrdersPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public UpdateOrderViewModel updateOrderViewModel() {
        return new UpdateOrderViewModel();
    }

    @Bean
    public UpdateOrderUseCaseControl updateOrderUseCase(
            UpdateOrderOutputBoundary presenter,
            OrderRepository orderRepository
    ) {
        return new UpdateOrderUseCaseControl(presenter, orderRepository);
    }

    @Bean
    public UpdateOrderOutputBoundary updateOrderPresenter(
            UpdateOrderViewModel viewModel
    ) {
        return new UpdateOrderPresenter(viewModel);
    }

    // GetAllProducts Use Case
    @Bean
    @RequestScope
    public GetAllProductsViewModel getAllProductsViewModel() {
        return new GetAllProductsViewModel();
    }

    @Bean
    public GetAllProductsInputBoundary getAllProductsUseCase(
            GetAllProductsOutputBoundary presenter,
            ProductRepository productRepository,
            FormatProductsForDisplayInputBoundary formatProductsUseCase
    ) {
        return new GetAllProductsUseCaseControl(presenter, productRepository, formatProductsUseCase);
    }

    @Bean
    public GetAllProductsOutputBoundary getAllProductsPresenter(
            GetAllProductsViewModel viewModel
    ) {
        return new GetAllProductsPresenter(viewModel);
    }

    // GetOrderDetail Use Case
    @Bean
    @RequestScope
    public GetOrderDetailViewModel getOrderDetailViewModel() {
        return new GetOrderDetailViewModel();
    }

    @Bean
    public GetOrderDetailInputBoundary getOrderDetailUseCase(
            GetOrderDetailOutputBoundary presenter,
            OrderRepository orderRepository
    ) {
        return new GetOrderDetailUseCaseControl(presenter, orderRepository);
    }

    @Bean
    public GetOrderDetailOutputBoundary getOrderDetailPresenter(
            GetOrderDetailViewModel viewModel
    ) {
        return new GetOrderDetailPresenter(viewModel);
    }

    // GetValidOrderStatuses Use Case
    @Bean
    @RequestScope
    public GetValidOrderStatusesViewModel getValidOrderStatusesViewModel() {
        return new GetValidOrderStatusesViewModel();
    }

    @Bean
    public GetValidOrderStatusesInputBoundary getValidOrderStatusesUseCase(
            GetValidOrderStatusesOutputBoundary presenter
    ) {
        return new GetValidOrderStatusesUseCaseControl(presenter);
    }

    @Bean
    public GetValidOrderStatusesOutputBoundary getValidOrderStatusesPresenter(
            GetValidOrderStatusesViewModel viewModel
    ) {
        return new GetValidOrderStatusesPresenter(viewModel);
    }

    // ToggleProductVisibility Use Case
    @Bean
    @RequestScope
    public ToggleProductVisibilityViewModel toggleProductVisibilityViewModel() {
        return new ToggleProductVisibilityViewModel();
    }

    @Bean
    public ToggleProductVisibilityInputBoundary toggleProductVisibilityUseCase(
            ToggleProductVisibilityOutputBoundary presenter,
            ProductRepository productRepository
    ) {
        return new ToggleProductVisibilityUseCaseControl(presenter, productRepository);
    }

    @Bean
    public ToggleProductVisibilityOutputBoundary toggleProductVisibilityPresenter(
            ToggleProductVisibilityViewModel viewModel
    ) {
        return new ToggleProductVisibilityPresenter(viewModel);
    }

    // ==================== GET TOP PRODUCTS USE CASE ====================
    
    @Bean
    @RequestScope
    public GetTopProductsViewModel getTopProductsViewModel() {
        return new GetTopProductsViewModel();
    }

    @Bean
    public GetTopProductsUseCaseControl getTopProductsUseCase(
            GetTopProductsOutputBoundary presenter,
            OrderRepository orderRepository
    ) {
        return new GetTopProductsUseCaseControl(presenter, orderRepository);
    }

    @Bean
    public GetTopProductsOutputBoundary getTopProductsPresenter(
            GetTopProductsViewModel viewModel
    ) {
        return new GetTopProductsPresenter(viewModel);
    }

    // ==================== ATOMIC USE CASES (UC-39 TO UC-70) ====================
    
    // ===== CART GROUP (UC-39 TO UC-43) =====
    
    @Bean
    public ValidateCartItemInputBoundary validateCartItemUseCase() {
        return new ValidateCartItemUseCaseControl(data -> {});
    }
    
    @Bean
    public FindCartItemInputBoundary findCartItemUseCase(CartRepository cartRepository) {
        return new FindCartItemUseCaseControl(data -> {}, cartRepository);
    }
    
    @Bean
    public UpdateCartItemQuantityInputBoundary updateCartItemQuantityUseCase(CartRepository cartRepository) {
        return new UpdateCartItemQuantityUseCaseControl(data -> {}, cartRepository);
    }
    
    @Bean
    public CalculateCartTotalsInputBoundary calculateCartTotalsUseCase(ProductRepository productRepository) {
        return new CalculateCartTotalsUseCaseControl(data -> {});
    }
    
    @Bean
    public RemoveCartItemInputBoundary removeCartItemUseCase(CartRepository cartRepository) {
        return new RemoveCartItemUseCaseControl(data -> {}, cartRepository);
    }
    
    // ===== ORDER GROUP (UC-44 TO UC-50) =====
    
    @Bean
    public ValidateOrderInputBoundary validateOrderUseCase() {
        return new ValidateOrderUseCaseControl(data -> {}, null);
    }
    
    @Bean
    public ValidateCartBeforeCheckoutInputBoundary validateCartBeforeCheckoutUseCase(
            CartRepository cartRepository,
            ProductRepository productRepository) {
        return new ValidateCartBeforeCheckoutUseCaseControl(data -> {}, cartRepository, productRepository);
    }
    
    @Bean
    public CreateOrderFromCartInputBoundary createOrderFromCartUseCase(
            OrderRepository orderRepository,
            CartRepository cartRepository) {
        return new CreateOrderFromCartUseCaseControl(data -> {});
    }
    
    @Bean
    public ReduceProductStockInputBoundary reduceProductStockUseCase(ProductRepository productRepository) {
        return new ReduceProductStockUseCaseControl(data -> {}, productRepository);
    }
    
    @Bean
    public ClearCartInputBoundary clearCartUseCase(CartRepository cartRepository) {
        return new ClearCartUseCaseControl(data -> {}, cartRepository);
    }
    
    @Bean
    public ValidateOrderCancellationInputBoundary validateOrderCancellationUseCase() {
        return new ValidateOrderCancellationUseCaseControl(data -> {});
    }
    
    @Bean
    public CalculateOrderTotalsInputBoundary calculateOrderTotalsUseCase(ProductRepository productRepository) {
        return new CalculateOrderTotalsUseCaseControl(data -> {});
    }
    
    @Bean
    public FormatOrderForDisplayInputBoundary formatOrderForDisplayUseCase(ProductRepository productRepository) {
        return new FormatOrderForDisplayUseCaseControl(data -> {});
    }
    
    // ===== PRODUCT GROUP (UC-51 TO UC-56) =====
    
    @Bean
    public ValidateProductDataInputBoundary validateProductDataUseCase() {
        return new ValidateProductDataUseCaseControl(data -> {});
    }
    
    @Bean
    public CheckProductDuplicationInputBoundary checkProductDuplicationUseCase(ProductRepository productRepository) {
        return new CheckProductDuplicationUseCaseControl(data -> {}, productRepository);
    }
    
    @Bean
    public CalculateProductPriceInputBoundary calculateProductPriceUseCase() {
        return new CalculateProductPriceUseCaseControl(data -> {});
    }
    
    @Bean
    public FormatProductForDisplayInputBoundary formatProductForDisplayUseCase() {
        return new FormatProductForDisplayUseCaseControl(data -> {});
    }
    
    @Bean
    public CheckProductAvailabilityInputBoundary checkProductAvailabilityUseCase(ProductRepository productRepository) {
        return new CheckProductAvailabilityUseCaseControl(data -> {}, productRepository);
    }
    
    @Bean
    public ArchiveProductInputBoundary archiveProductUseCase(ProductRepository productRepository) {
        return new ArchiveProductUseCaseControl(data -> {}, productRepository);
    }
    
    // ===== USER GROUP (UC-57 TO UC-61) =====
    
    @Bean
    public ValidateUserRegistrationInputBoundary validateUserRegistrationUseCase() {
        return new ValidateUserRegistrationUseCaseControl(data -> {});
    }
    
    @Bean
    public CheckUserDuplicationInputBoundary checkUserDuplicationUseCase(UserRepository userRepository) {
        return new CheckUserDuplicationUseCaseControl(data -> {}, userRepository);
    }
    
    @Bean
    public HashPasswordInputBoundary hashPasswordUseCase() {
        return new HashPasswordUseCaseControl(data -> {});
    }
    
    @Bean
    public VerifyPasswordInputBoundary verifyPasswordUseCase() {
        return new VerifyPasswordUseCaseControl(data -> {});
    }
    
    @Bean
    public AssignUserRoleInputBoundary assignUserRoleUseCase() {
        return new AssignUserRoleUseCaseControl(data -> {}, null);
    }
    
    // ===== USER MANAGEMENT SECONDARY USECASES (UC-71 TO UC-73) =====
    
    @Bean
    public CreateUserCartInputBoundary createUserCartUseCase(CartRepository cartRepository) {
        return new CreateUserCartUseCaseControl((CreateUserCartOutputBoundary) data -> {}, cartRepository);
    }
    
    @Bean
    public MergeGuestCartInputBoundary mergeGuestCartUseCase(CartRepository cartRepository) {
        return new MergeGuestCartUseCaseControl((MergeGuestCartOutputBoundary) data -> {}, cartRepository);
    }
    
    @Bean
    public ApplyUserFiltersInputBoundary applyUserFiltersUseCase() {
        return new ApplyUserFiltersUseCaseControl((ApplyUserFiltersOutputBoundary) data -> {});
    }
    
    // ===== MOTORBIKE MANAGEMENT SECONDARY USECASES (UC-74 TO UC-75) =====
    
    @Bean
    public FormatMotorbikesForDisplayInputBoundary formatMotorbikesForDisplayUseCase() {
        return new FormatMotorbikesForDisplayUseCaseControl((FormatMotorbikesForDisplayOutputBoundary) data -> {});
    }
    
    @Bean
    public ValidateMotorbikeFieldsInputBoundary validateMotorbikeFieldsUseCase() {
        return new ValidateMotorbikeFieldsUseCaseControl((ValidateMotorbikeFieldsOutputBoundary) data -> {});
    }
    
    // ===== ACCESSORY MANAGEMENT SECONDARY USECASES (UC-76) =====
    
    @Bean
    public FormatAccessoriesForDisplayInputBoundary formatAccessoriesForDisplayUseCase() {
        return new FormatAccessoriesForDisplayUseCaseControl((FormatAccessoriesForDisplayOutputBoundary) data -> {});
    }
    
    // ===== PRODUCT MANAGEMENT SECONDARY USECASES (UC-77) =====
    
    @Bean
    public FormatProductsForDisplayInputBoundary formatProductsForDisplayUseCase() {
        return new FormatProductsForDisplayUseCaseControl((FormatProductsForDisplayOutputBoundary) data -> {});
    }
    
    // ===== CART MANAGEMENT SECONDARY USECASES (UC-78) =====
    
    @Bean
    public FormatCartItemsForDisplayInputBoundary formatCartItemsForDisplayUseCase(ProductRepository productRepository) {
        return new FormatCartItemsForDisplayUseCaseControl((FormatCartItemsForDisplayOutputBoundary) data -> {}, productRepository);
    }
    
    // ===== ORDER MANAGEMENT SECONDARY USECASES (UC-79, UC-80, UC-81) =====
    
    @Bean
    public SortOrdersByDateInputBoundary sortOrdersByDateUseCase() {
        return new SortOrdersByDateUseCaseControl((SortOrdersByDateOutputBoundary) data -> {});
    }
    
    @Bean
    public CalculateOrderStatisticsInputBoundary calculateOrderStatisticsUseCase() {
        return new CalculateOrderStatisticsUseCaseControl((CalculateOrderStatisticsOutputBoundary) data -> {});
    }
    
    @Bean
    public FormatOrdersForListInputBoundary formatOrdersForListUseCase() {
        return new FormatOrdersForListUseCaseControl((FormatOrdersForListOutputBoundary) data -> {});
    }
    
    @Bean
    public FormatOrderItemsForCheckoutInputBoundary formatOrderItemsForCheckoutUseCase() {
        return new FormatOrderItemsForCheckoutUseCaseControl((FormatOrderItemsForCheckoutOutputBoundary) data -> {});
    }
    
    // ===== IMPORT/EXPORT GROUP (UC-62 TO UC-67) =====
    
    @Bean
    public ValidateExcelFileInputBoundary validateExcelFileUseCase() {
        return new ValidateExcelFileUseCaseControl(data -> {});
    }
    
    @Bean
    public ParseExcelDataInputBoundary parseExcelDataUseCase() {
        return new ParseExcelDataUseCaseControl(data -> {});
    }
    
    @Bean
    public ValidateImportRowInputBoundary validateImportRowUseCase() {
        return new ValidateImportRowUseCaseControl(data -> {});
    }
    
    @Bean
    public GenerateImportReportInputBoundary generateImportReportUseCase() {
        return new GenerateImportReportUseCaseControl(data -> {});
    }
    
    @Bean
    public GenerateExcelFileInputBoundary generateExcelFileUseCase() {
        return new GenerateExcelFileUseCaseControl(data -> {});
    }
    
    @Bean
    public FormatDataForExportInputBoundary formatDataForExportUseCase() {
        return new FormatDataForExportUseCaseControl(outputData -> {});
    }
    
    // ===== SEARCH GROUP (UC-68 TO UC-70) =====
    
    @Bean
    public BuildSearchCriteriaInputBoundary buildSearchCriteriaUseCase() {
        return new BuildSearchCriteriaUseCaseControl(outputData -> {});
    }
    
    @Bean
    public ApplySearchFiltersInputBoundary applySearchFiltersUseCase() {
        return new ApplySearchFiltersUseCaseControl(outputData -> {});
    }
    
    @Bean
    public SortSearchResultsInputBoundary sortSearchResultsUseCase() {
        return new SortSearchResultsUseCaseControl(outputData -> {});
    }

}