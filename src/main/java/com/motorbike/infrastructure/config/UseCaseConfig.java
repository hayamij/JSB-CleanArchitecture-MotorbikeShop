package com.motorbike.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import com.motorbike.adapters.presenters.*;
import com.motorbike.adapters.viewmodels.*;
import com.motorbike.business.ports.repository.*;
import com.motorbike.business.usecase.control.*;
import com.motorbike.business.usecase.input.GetAllMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.SearchMotorbikesInputBoundary;
import com.motorbike.business.usecase.output.*;

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
        MotorbikeRepository motorbikeRepository
) {
    return new SearchMotorbikesUseCaseControl(outputBoundary, motorbikeRepository);
}

@Bean
public SearchMotorbikesOutputBoundary searchMotorbikesOutputBoundary(
        SearchMotorbikesViewModel viewModel
) {
    return new SearchMotorbikesPresenter(viewModel);
}

@Bean
public SearchMotorbikesViewModel searchMotorbikesViewModel() {
    return new SearchMotorbikesViewModel();
}

// ========== MOTORBIKE CRUD BEANS ==========

@Bean
@RequestScope
public CreateMotorbikeViewModel createMotorbikeViewModel() {
    return new CreateMotorbikeViewModel();
}

@Bean
public CreateMotorbikeUseCaseControl createMotorbikeUseCase(
        CreateMotorbikeOutputBoundary presenter,
        ProductRepository productRepository
) {
    return new CreateMotorbikeUseCaseControl(presenter, productRepository);
}

@Bean
public CreateMotorbikeOutputBoundary createMotorbikePresenter(
        CreateMotorbikeViewModel viewModel
) {
    return new CreateMotorbikePresenter(viewModel);
}

@Bean
@RequestScope
public UpdateMotorbikeViewModel updateMotorbikeViewModel() {
    return new UpdateMotorbikeViewModel();
}

@Bean
public UpdateMotorbikeUseCaseControl updateMotorbikeUseCase(
        UpdateMotorbikeOutputBoundary presenter,
        ProductRepository productRepository
) {
    return new UpdateMotorbikeUseCaseControl(presenter, productRepository);
}

@Bean
public UpdateMotorbikeOutputBoundary updateMotorbikePresenter(
        UpdateMotorbikeViewModel viewModel
) {
    return new UpdateMotorbikePresenter(viewModel);
}

@Bean
@RequestScope
public DeleteMotorbikeViewModel deleteMotorbikeViewModel() {
    return new DeleteMotorbikeViewModel();
}

@Bean
public DeleteMotorbikeUseCaseControl deleteMotorbikeUseCase(
        DeleteMotorbikeOutputBoundary presenter,
        ProductRepository productRepository
) {
    return new DeleteMotorbikeUseCaseControl(presenter, productRepository);
}

@Bean
public DeleteMotorbikeOutputBoundary deleteMotorbikePresenter(
        DeleteMotorbikeViewModel viewModel
) {
    return new DeleteMotorbikePresenter(viewModel);
}

// ========== ACCESSORY CRUD BEANS ==========

@Bean
@RequestScope
public CreateAccessoryViewModel createAccessoryViewModel() {
    return new CreateAccessoryViewModel();
}

@Bean
public CreateAccessoryUseCaseControl createAccessoryUseCase(
        CreateAccessoryOutputBoundary presenter,
        ProductRepository productRepository
) {
    return new CreateAccessoryUseCaseControl(presenter, productRepository);
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
        ProductRepository productRepository
) {
    return new UpdateAccessoryUseCaseControl(presenter, productRepository);
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
        ProductRepository productRepository
) {
    return new DeleteAccessoryUseCaseControl(presenter, productRepository);
}

@Bean
public DeleteAccessoryOutputBoundary deleteAccessoryPresenter(
        DeleteAccessoryViewModel viewModel
) {
    return new DeleteAccessoryPresenter(viewModel);
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
@RequestScope
public SearchAccessoriesViewModel searchAccessoriesViewModel() {
    return new SearchAccessoriesViewModel();
}

@Bean
public SearchAccessoriesUseCaseControl searchAccessoriesUseCase(
        SearchAccessoriesOutputBoundary presenter,
        AccessoryRepository accessoryRepository
) {
    return new SearchAccessoriesUseCaseControl(presenter, accessoryRepository);
}

@Bean
public SearchAccessoriesOutputBoundary searchAccessoriesPresenter(
        SearchAccessoriesViewModel viewModel
) {
    return new SearchAccessoriesPresenter(viewModel);
}

// ========== USER MANAGEMENT BEANS ==========

@Bean
@RequestScope
public CreateUserViewModel createUserViewModel() {
    return new CreateUserViewModel();
}

@Bean
public CreateUserUseCaseControl createUserUseCase(
        CreateUserOutputBoundary presenter,
        UserRepository userRepository
) {
    return new CreateUserUseCaseControl(presenter, userRepository);
}

@Bean
public CreateUserOutputBoundary createUserPresenter(
        CreateUserViewModel viewModel
) {
    return new CreateUserPresenter(viewModel);
}

@Bean
@RequestScope
public UpdateUserViewModel updateUserViewModel() {
    return new UpdateUserViewModel();
}

@Bean
public UpdateUserUseCaseControl updateUserUseCase(
        UpdateUserOutputBoundary presenter,
        UserRepository userRepository
) {
    return new UpdateUserUseCaseControl(presenter, userRepository);
}

@Bean
public UpdateUserOutputBoundary updateUserPresenter(
        UpdateUserViewModel viewModel
) {
    return new UpdateUserPresenter(viewModel);
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
        UserRepository userRepository
) {
    return new SearchUsersUseCaseControl(presenter, userRepository);
}

@Bean
public SearchUsersOutputBoundary searchUsersPresenter(
        SearchUsersViewModel viewModel
) {
    return new SearchUsersPresenter(viewModel);
}

// ========== ORDER MANAGEMENT BEANS ==========

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

}
