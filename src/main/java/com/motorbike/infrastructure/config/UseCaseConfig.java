package com.motorbike.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import com.motorbike.adapters.presenters.CreateUserPresenter;
import com.motorbike.adapters.presenters.AddMotorbikePresenter;
import com.motorbike.adapters.viewmodels.AddMotorbikeViewModel;
import com.motorbike.business.usecase.control.AddMotorbikeUseCaseControl;
import com.motorbike.business.usecase.output.AddMotorbikeOutputBoundary;
import com.motorbike.business.usecase.input.DeleteMotorbikeInputBoundary;
import com.motorbike.adapters.presenters.AddToCartPresenter;
import com.motorbike.adapters.presenters.CancelOrderPresenter;
import com.motorbike.adapters.presenters.CheckoutPresenter;
import com.motorbike.adapters.presenters.CreateAccessoryPresenter;
import com.motorbike.adapters.presenters.GetAllMotorbikesPresenter;
import com.motorbike.adapters.presenters.ListAllOrdersPresenter;
import com.motorbike.adapters.presenters.LoginPresenter;
import com.motorbike.adapters.presenters.ProductDetailPresenter;
import com.motorbike.adapters.presenters.RegisterPresenter;
import com.motorbike.adapters.presenters.SearchMotorbikesPresenter;
import com.motorbike.adapters.presenters.UpdateCartQuantityPresenter;
import com.motorbike.adapters.presenters.ViewCartPresenter;
import com.motorbike.adapters.repositories.MotorbikeRepositoryAdapter;
import com.motorbike.adapters.presenters.GetAllAccessoriesPresenter;
import com.motorbike.adapters.presenters.SearchAccessoriesPresenter;
import com.motorbike.adapters.viewmodels.AddToCartViewModel;
import com.motorbike.adapters.viewmodels.CancelOrderViewModel;
import com.motorbike.adapters.viewmodels.CheckoutViewModel;
import com.motorbike.adapters.viewmodels.GetAllMotorbikesViewModel;
import com.motorbike.adapters.viewmodels.ListAllOrdersViewModel;
import com.motorbike.adapters.viewmodels.LoginViewModel;
import com.motorbike.adapters.viewmodels.ProductDetailViewModel;
import com.motorbike.adapters.viewmodels.RegisterViewModel;
import com.motorbike.adapters.viewmodels.SearchMotorbikesViewModel;
import com.motorbike.adapters.viewmodels.UpdateCartQuantityViewModel;
import com.motorbike.adapters.viewmodels.ViewCartViewModel;
import com.motorbike.adapters.viewmodels.GetAllAccessoriesViewModel;
import com.motorbike.adapters.viewmodels.SearchAccessoriesViewModel;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.control.AddToCartUseCaseControl;
import com.motorbike.business.usecase.control.CancelOrderUseCaseControl;
import com.motorbike.business.usecase.control.CheckoutUseCaseControl;
import com.motorbike.business.usecase.control.GetAllMotorbikesUseCaseControl;
import com.motorbike.business.usecase.control.GetProductDetailUseCaseControl;
import com.motorbike.business.usecase.control.ListAllOrdersUseCaseControl;
import com.motorbike.business.usecase.control.LoginUseCaseControl;
import com.motorbike.business.usecase.control.RegisterUseCaseControl;
import com.motorbike.business.usecase.control.SearchMotorbikesUseCaseControl;
import com.motorbike.business.usecase.control.GetAllAccessoriesUseCaseControl;
import com.motorbike.business.usecase.control.SearchAccessoriesUseCaseControl;
import com.motorbike.business.usecase.control.UpdateCartQuantityUseCaseControl;
import com.motorbike.business.usecase.control.ViewCartUseCaseControl;
import com.motorbike.business.usecase.input.GetAllMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.SearchMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.GetAllAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.SearchAccessoriesInputBoundary;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;
import com.motorbike.business.usecase.output.CheckoutOutputBoundary;
import com.motorbike.business.usecase.output.GetAllMotorbikesOutputBoundary;
import com.motorbike.business.usecase.output.GetProductDetailOutputBoundary;
import com.motorbike.business.usecase.output.ListAllOrdersOutputBoundary;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.business.usecase.output.RegisterOutputBoundary;
import com.motorbike.business.usecase.output.SearchMotorbikesOutputBoundary;
import com.motorbike.business.usecase.output.GetAllAccessoriesOutputBoundary;
import com.motorbike.business.usecase.output.SearchAccessoriesOutputBoundary;
import com.motorbike.business.usecase.output.UpdateCartQuantityOutputBoundary;
import com.motorbike.business.usecase.output.ViewCartOutputBoundary;
import com.motorbike.infrastructure.persistence.jpa.repositories.XeMayJpaRepository;

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
    public LoginUseCaseControl loginUseCase(
            LoginOutputBoundary loginPresenter,
            UserRepository userRepository,
            CartRepository cartRepository) {
        return new LoginUseCaseControl(loginPresenter, userRepository, cartRepository);
    }
    
    @Bean
    public LoginOutputBoundary loginPresenter(LoginViewModel loginViewModel) {
        return new LoginPresenter(loginViewModel);
    }
    
    @Bean
    public RegisterUseCaseControl registerUseCase(
            RegisterOutputBoundary registerPresenter,
            UserRepository userRepository,
            CartRepository cartRepository) {
        return new RegisterUseCaseControl(registerPresenter, userRepository, cartRepository);
    }
    
    @Bean
    public RegisterOutputBoundary registerPresenter(RegisterViewModel registerViewModel) {
        return new RegisterPresenter(registerViewModel);
    }
    
    @Bean
    public GetProductDetailUseCaseControl getProductDetailUseCase(
            GetProductDetailOutputBoundary productDetailPresenter,
            ProductRepository productRepository) {
        return new GetProductDetailUseCaseControl(productDetailPresenter, productRepository);
    }
    
    @Bean
    public GetProductDetailOutputBoundary productDetailPresenter(ProductDetailViewModel productDetailViewModel) {
        return new ProductDetailPresenter(productDetailViewModel);
    }
    
    @Bean
    public AddToCartUseCaseControl addToCartUseCase(
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
    public ViewCartUseCaseControl viewCartUseCase(
            ViewCartOutputBoundary viewCartPresenter,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        return new ViewCartUseCaseControl(viewCartPresenter, cartRepository, productRepository);
    }
    
    @Bean
    public ViewCartOutputBoundary viewCartPresenter(ViewCartViewModel viewCartViewModel) {
        return new ViewCartPresenter(viewCartViewModel);
    }
    
    @Bean
    public UpdateCartQuantityUseCaseControl updateCartQuantityUseCase(
            UpdateCartQuantityOutputBoundary updateCartQuantityPresenter,
            CartRepository cartRepository) {
        return new UpdateCartQuantityUseCaseControl(updateCartQuantityPresenter, cartRepository);
    }
    
    @Bean
    public UpdateCartQuantityOutputBoundary updateCartQuantityPresenter(UpdateCartQuantityViewModel updateCartQuantityViewModel) {
        return new UpdateCartQuantityPresenter(updateCartQuantityViewModel);
    }
    
    @Bean
    public CheckoutUseCaseControl checkoutUseCase(
            CheckoutOutputBoundary checkoutPresenter,
            CartRepository cartRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository) {
        return new CheckoutUseCaseControl(checkoutPresenter, cartRepository, productRepository, orderRepository);
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
    public ListAllOrdersUseCaseControl listAllOrdersUseCase(
            ListAllOrdersOutputBoundary listAllOrdersPresenter,
            OrderRepository orderRepository) {
        return new ListAllOrdersUseCaseControl(listAllOrdersPresenter, orderRepository);
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
    public CancelOrderUseCaseControl cancelOrderUseCase(
            CancelOrderOutputBoundary cancelOrderPresenter,
            OrderRepository orderRepository,
            ProductRepository productRepository) {
        return new CancelOrderUseCaseControl(cancelOrderPresenter, orderRepository, productRepository);
    }

    @Bean
    public CancelOrderOutputBoundary cancelOrderPresenter(CancelOrderViewModel cancelOrderViewModel) {
        return new CancelOrderPresenter(cancelOrderViewModel);
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
            ProductRepository productRepository
    ) {
        return new GetAllMotorbikesUseCaseControl(presenter, productRepository);
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
            ProductRepository productRepository
    ) {
        return new SearchMotorbikesUseCaseControl(outputBoundary, productRepository);
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
            MotorbikeRepository motorbikeRepository
    ) {
        return new AddMotorbikeUseCaseControl(presenter, motorbikeRepository);
    }

    @Bean
    @RequestScope
    public GetAllAccessoriesViewModel getAllAccessoriesViewModel() {
        return new GetAllAccessoriesViewModel();
    }

    @Bean
    public GetAllAccessoriesUseCaseControl getAllAccessoriesUseCase(
            GetAllAccessoriesOutputBoundary presenter,
            ProductRepository productRepository
    ) {
        return new GetAllAccessoriesUseCaseControl(presenter, productRepository);
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
            ProductRepository productRepository
    ) {
        return new SearchAccessoriesUseCaseControl(outputBoundary, productRepository);
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
    public com.motorbike.adapters.viewmodels.GetAllUsersViewModel getAllUsersViewModel() {
        return new com.motorbike.adapters.viewmodels.GetAllUsersViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.control.GetAllUsersUseCaseControl getAllUsersUseCase(
            com.motorbike.business.usecase.output.GetAllUsersOutputBoundary presenter,
            UserRepository userRepository
    ) {
        return new com.motorbike.business.usecase.control.GetAllUsersUseCaseControl(presenter, userRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.GetAllUsersOutputBoundary getAllUsersPresenter(
            com.motorbike.adapters.viewmodels.GetAllUsersViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.GetAllUsersPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.SearchUsersViewModel searchUsersViewModel() {
        return new com.motorbike.adapters.viewmodels.SearchUsersViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.control.SearchUsersUseCaseControl searchUsersUseCase(
            com.motorbike.business.usecase.output.SearchUsersOutputBoundary presenter,
            UserRepository userRepository
    ) {
        return new com.motorbike.business.usecase.control.SearchUsersUseCaseControl(presenter, userRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.SearchUsersOutputBoundary searchUsersPresenter(
            com.motorbike.adapters.viewmodels.SearchUsersViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.SearchUsersPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.DeleteUserViewModel deleteUserViewModel() {
        return new com.motorbike.adapters.viewmodels.DeleteUserViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.control.DeleteUserUseCaseControl deleteUserUseCase(
            com.motorbike.business.usecase.output.DeleteUserOutputBoundary presenter,
            UserRepository userRepository
    ) {
        return new com.motorbike.business.usecase.control.DeleteUserUseCaseControl(presenter, userRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.DeleteUserOutputBoundary deleteUserPresenter(
            com.motorbike.adapters.viewmodels.DeleteUserViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.DeleteUserPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.AddUserViewModel addUserViewModel() {
        return new com.motorbike.adapters.viewmodels.AddUserViewModel();
    }

@Bean
@RequestScope
public com.motorbike.adapters.viewmodels.CreateUserViewModel createUserViewModel() {
    return new com.motorbike.adapters.viewmodels.CreateUserViewModel();
}

@Bean
public com.motorbike.business.usecase.control.AddUserUseCaseControl addUserUseCase(
        com.motorbike.business.usecase.output.AddUserOutputBoundary presenter,
        UserRepository userRepository
) {
    return new com.motorbike.business.usecase.control.AddUserUseCaseControl(presenter, userRepository);
}

@Bean
public com.motorbike.business.usecase.output.AddUserOutputBoundary addUserOutputBoundary(
        com.motorbike.adapters.viewmodels.AddUserViewModel viewModel
) {
    return new com.motorbike.adapters.presenters.AddUserPresenter(viewModel);
}

@Bean
public com.motorbike.business.usecase.control.CreateUserUseCaseControl createUserUseCaseControl(
        com.motorbike.business.usecase.output.CreateUserOutputBoundary presenter,
        UserRepository userRepository
) {
    return new com.motorbike.business.usecase.control.CreateUserUseCaseControl(presenter, userRepository);
}

@Bean
public com.motorbike.business.usecase.output.CreateUserOutputBoundary createUserPresenter(
        com.motorbike.adapters.viewmodels.CreateUserViewModel viewModel
) {
    return new CreateUserPresenter(viewModel);
}    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.UpdateUserViewModel updateUserViewModel() {
        return new com.motorbike.adapters.viewmodels.UpdateUserViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.control.UpdateUserUseCaseControl updateUserUseCase(
            com.motorbike.business.usecase.output.UpdateUserOutputBoundary presenter,
            UserRepository userRepository
    ) {
        return new com.motorbike.business.usecase.control.UpdateUserUseCaseControl(presenter, userRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.UpdateUserOutputBoundary updateUserPresenter(
            com.motorbike.adapters.viewmodels.UpdateUserViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.UpdateUserPresenter(viewModel);
    }

    // ===== MOTORBIKE MANAGEMENT USE CASES =====

    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.DeleteMotorbikeViewModel deleteMotorbikeViewModel() {
        return new com.motorbike.adapters.viewmodels.DeleteMotorbikeViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.control.DeleteMotorbikeUseCaseControl deleteMotorbikeUseCase(
            com.motorbike.business.usecase.output.DeleteMotorbikeOutputBoundary presenter,
            ProductRepository productRepository
    ) {
        return new com.motorbike.business.usecase.control.DeleteMotorbikeUseCaseControl(presenter, productRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.DeleteMotorbikeOutputBoundary deleteMotorbikePresenter(
            com.motorbike.adapters.viewmodels.DeleteMotorbikeViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.DeleteMotorbikePresenter(viewModel);
    }

    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.UpdateMotorbikeViewModel updateMotorbikeViewModel() {
        return new com.motorbike.adapters.viewmodels.UpdateMotorbikeViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.control.UpdateMotorbikeUseCaseControl updateMotorbikeUseCase(
            com.motorbike.business.usecase.output.UpdateMotorbikeOutputBoundary presenter,
            ProductRepository productRepository
    ) {
        return new com.motorbike.business.usecase.control.UpdateMotorbikeUseCaseControl(presenter, productRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.UpdateMotorbikeOutputBoundary updateMotorbikePresenter(
            com.motorbike.adapters.viewmodels.UpdateMotorbikeViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.UpdateMotorbikePresenter(viewModel);
    }

    // ===== ACCESSORY MANAGEMENT USE CASES =====

    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.AddAccessoryViewModel addAccessoryViewModel() {
        return new com.motorbike.adapters.viewmodels.AddAccessoryViewModel();
    }

    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.CreateAccessoryViewModel createAccessoryViewModel() {
        return new com.motorbike.adapters.viewmodels.CreateAccessoryViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.control.CreateAccessoryUseCaseControl createAccessoryUseCaseControl(
            com.motorbike.business.usecase.output.CreateAccessoryOutputBoundary presenter,
            ProductRepository productRepository
    ) {
        return new com.motorbike.business.usecase.control.CreateAccessoryUseCaseControl(presenter, productRepository);
    }

    @Bean
    public com.motorbike.business.usecase.control.AddAccessoryUseCaseControl addAccessoryUseCase(
            com.motorbike.business.usecase.output.AddAccessoryOutputBoundary presenter,
            com.motorbike.business.ports.repository.AccessoryRepository accessoryRepository
    ) {
        return new com.motorbike.business.usecase.control.AddAccessoryUseCaseControl(presenter, accessoryRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.AddAccessoryOutputBoundary addAccessoryOutputBoundary(
            com.motorbike.adapters.viewmodels.AddAccessoryViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.AddAccessoryPresenter(viewModel);
    }

    @Bean
    public com.motorbike.business.usecase.output.CreateAccessoryOutputBoundary createAccessoryPresenter(
            com.motorbike.adapters.viewmodels.CreateAccessoryViewModel viewModel
    ) {
        return new CreateAccessoryPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.UpdateAccessoryViewModel updateAccessoryViewModel() {
        return new com.motorbike.adapters.viewmodels.UpdateAccessoryViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.control.UpdateAccessoryUseCaseControl updateAccessoryUseCase(
            com.motorbike.business.usecase.output.UpdateAccessoryOutputBoundary presenter,
            ProductRepository productRepository
    ) {
        return new com.motorbike.business.usecase.control.UpdateAccessoryUseCaseControl(presenter, productRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.UpdateAccessoryOutputBoundary updateAccessoryPresenter(
            com.motorbike.adapters.viewmodels.UpdateAccessoryViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.UpdateAccessoryPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.DeleteAccessoryViewModel deleteAccessoryViewModel() {
        return new com.motorbike.adapters.viewmodels.DeleteAccessoryViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.control.DeleteAccessoryUseCaseControl deleteAccessoryUseCase(
            com.motorbike.business.usecase.output.DeleteAccessoryOutputBoundary presenter,
            ProductRepository productRepository
    ) {
        return new com.motorbike.business.usecase.control.DeleteAccessoryUseCaseControl(presenter, productRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.DeleteAccessoryOutputBoundary deleteAccessoryPresenter(
            com.motorbike.adapters.viewmodels.DeleteAccessoryViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.DeleteAccessoryPresenter(viewModel);
    }

    // ===== ORDER MANAGEMENT USE CASES =====

    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.SearchOrdersViewModel searchOrdersViewModel() {
        return new com.motorbike.adapters.viewmodels.SearchOrdersViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.control.SearchOrdersUseCaseControl searchOrdersUseCase(
            com.motorbike.business.usecase.output.SearchOrdersOutputBoundary presenter,
            OrderRepository orderRepository,
            UserRepository userRepository
    ) {
        return new com.motorbike.business.usecase.control.SearchOrdersUseCaseControl(presenter, orderRepository, userRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.SearchOrdersOutputBoundary searchOrdersPresenter(
            com.motorbike.adapters.viewmodels.SearchOrdersViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.SearchOrdersPresenter(viewModel);
    }

    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.UpdateOrderViewModel updateOrderViewModel() {
        return new com.motorbike.adapters.viewmodels.UpdateOrderViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.control.UpdateOrderUseCaseControl updateOrderUseCase(
            com.motorbike.business.usecase.output.UpdateOrderOutputBoundary presenter,
            OrderRepository orderRepository
    ) {
        return new com.motorbike.business.usecase.control.UpdateOrderUseCaseControl(presenter, orderRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.UpdateOrderOutputBoundary updateOrderPresenter(
            com.motorbike.adapters.viewmodels.UpdateOrderViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.UpdateOrderPresenter(viewModel);
    }

    // GetAllProducts Use Case
    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.GetAllProductsViewModel getAllProductsViewModel() {
        return new com.motorbike.adapters.viewmodels.GetAllProductsViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.input.GetAllProductsInputBoundary getAllProductsUseCase(
            com.motorbike.business.usecase.output.GetAllProductsOutputBoundary presenter,
            ProductRepository productRepository
    ) {
        return new com.motorbike.business.usecase.control.GetAllProductsUseCaseControl(presenter, productRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.GetAllProductsOutputBoundary getAllProductsPresenter(
            com.motorbike.adapters.viewmodels.GetAllProductsViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.GetAllProductsPresenter(viewModel);
    }

    // GetOrderDetail Use Case
    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.GetOrderDetailViewModel getOrderDetailViewModel() {
        return new com.motorbike.adapters.viewmodels.GetOrderDetailViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.input.GetOrderDetailInputBoundary getOrderDetailUseCase(
            com.motorbike.business.usecase.output.GetOrderDetailOutputBoundary presenter,
            OrderRepository orderRepository
    ) {
        return new com.motorbike.business.usecase.control.GetOrderDetailUseCaseControl(presenter, orderRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.GetOrderDetailOutputBoundary getOrderDetailPresenter(
            com.motorbike.adapters.viewmodels.GetOrderDetailViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.GetOrderDetailPresenter(viewModel);
    }

    // GetValidOrderStatuses Use Case
    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.GetValidOrderStatusesViewModel getValidOrderStatusesViewModel() {
        return new com.motorbike.adapters.viewmodels.GetValidOrderStatusesViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.input.GetValidOrderStatusesInputBoundary getValidOrderStatusesUseCase(
            com.motorbike.business.usecase.output.GetValidOrderStatusesOutputBoundary presenter
    ) {
        return new com.motorbike.business.usecase.control.GetValidOrderStatusesUseCaseControl(presenter);
    }

    @Bean
    public com.motorbike.business.usecase.output.GetValidOrderStatusesOutputBoundary getValidOrderStatusesPresenter(
            com.motorbike.adapters.viewmodels.GetValidOrderStatusesViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.GetValidOrderStatusesPresenter(viewModel);
    }

    // ToggleProductVisibility Use Case
    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.ToggleProductVisibilityViewModel toggleProductVisibilityViewModel() {
        return new com.motorbike.adapters.viewmodels.ToggleProductVisibilityViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.input.ToggleProductVisibilityInputBoundary toggleProductVisibilityUseCase(
            com.motorbike.business.usecase.output.ToggleProductVisibilityOutputBoundary presenter,
            ProductRepository productRepository
    ) {
        return new com.motorbike.business.usecase.control.ToggleProductVisibilityUseCaseControl(presenter, productRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.ToggleProductVisibilityOutputBoundary toggleProductVisibilityPresenter(
            com.motorbike.adapters.viewmodels.ToggleProductVisibilityViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.ToggleProductVisibilityPresenter(viewModel);
    }

    // ==================== GET TOP PRODUCTS USE CASE ====================
    
    @Bean
    @RequestScope
    public com.motorbike.adapters.viewmodels.GetTopProductsViewModel getTopProductsViewModel() {
        return new com.motorbike.adapters.viewmodels.GetTopProductsViewModel();
    }

    @Bean
    public com.motorbike.business.usecase.control.GetTopProductsUseCaseControl getTopProductsUseCase(
            com.motorbike.business.usecase.output.GetTopProductsOutputBoundary presenter,
            OrderRepository orderRepository
    ) {
        return new com.motorbike.business.usecase.control.GetTopProductsUseCaseControl(presenter, orderRepository);
    }

    @Bean
    public com.motorbike.business.usecase.output.GetTopProductsOutputBoundary getTopProductsPresenter(
            com.motorbike.adapters.viewmodels.GetTopProductsViewModel viewModel
    ) {
        return new com.motorbike.adapters.presenters.GetTopProductsPresenter(viewModel);
    }

}