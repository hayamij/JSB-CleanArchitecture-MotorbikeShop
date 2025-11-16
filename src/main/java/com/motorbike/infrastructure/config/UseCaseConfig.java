package com.motorbike.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import com.motorbike.adapters.presenters.AddToCartPresenter;
import com.motorbike.adapters.presenters.CancelOrderPresenter;
import com.motorbike.adapters.presenters.CheckoutPresenter;
import com.motorbike.adapters.presenters.ListAllOrdersPresenter;
import com.motorbike.adapters.presenters.LoginPresenter;
import com.motorbike.adapters.presenters.ProductDetailPresenter;
import com.motorbike.adapters.presenters.RegisterPresenter;
import com.motorbike.adapters.presenters.UpdateCartQuantityPresenter;
import com.motorbike.adapters.presenters.ViewCartPresenter;
import com.motorbike.adapters.viewmodels.AddToCartViewModel;
import com.motorbike.adapters.viewmodels.CancelOrderViewModel;
import com.motorbike.adapters.viewmodels.ListAllOrdersViewModel;
import com.motorbike.adapters.viewmodels.LoginViewModel;
import com.motorbike.adapters.viewmodels.ProductDetailViewModel;
import com.motorbike.adapters.viewmodels.RegisterViewModel;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.control.AddToCartUseCaseControl;
import com.motorbike.business.usecase.control.CancelOrderUseCaseControl;
import com.motorbike.business.usecase.control.CheckoutUseCaseControl;
import com.motorbike.business.usecase.control.GetProductDetailUseCaseControl;
import com.motorbike.business.usecase.control.ListAllOrdersUseCaseControl;
import com.motorbike.business.usecase.control.LoginUseCaseControl;
import com.motorbike.business.usecase.control.RegisterUseCaseControl;
import com.motorbike.business.usecase.control.UpdateCartQuantityUseCaseControl;
import com.motorbike.business.usecase.control.ViewCartUseCaseControl;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;
import com.motorbike.business.usecase.output.CheckoutOutputBoundary;
import com.motorbike.business.usecase.output.GetProductDetailOutputBoundary;
import com.motorbike.business.usecase.output.ListAllOrdersOutputBoundary;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.business.usecase.output.RegisterOutputBoundary;
import com.motorbike.business.usecase.output.UpdateCartQuantityOutputBoundary;
import com.motorbike.business.usecase.output.ViewCartOutputBoundary;

/**
 * Spring Configuration for Use Cases
 * Wires dependencies using Dependency Injection
 */
@Configuration
public class UseCaseConfig {
    
    // View Models (Request Scoped - one per request)
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
    
    // Login Use Case
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
    
    // Register Use Case
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
    
    // Get Product Detail Use Case
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
    
    // Add To Cart Use Case
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
    
    // View Cart Use Case
    @Bean
    public ViewCartUseCaseControl viewCartUseCase(
            ViewCartOutputBoundary viewCartPresenter,
            CartRepository cartRepository) {
        return new ViewCartUseCaseControl(viewCartPresenter, cartRepository);
    }
    
    @Bean
    public ViewCartOutputBoundary viewCartPresenter() {
        return new ViewCartPresenter();
    }
    
    // Update Cart Quantity Use Case
    @Bean
    public UpdateCartQuantityUseCaseControl updateCartQuantityUseCase(
            UpdateCartQuantityOutputBoundary updateCartQuantityPresenter,
            CartRepository cartRepository) {
        return new UpdateCartQuantityUseCaseControl(updateCartQuantityPresenter, cartRepository);
    }
    
    @Bean
    public UpdateCartQuantityOutputBoundary updateCartQuantityPresenter() {
        return new UpdateCartQuantityPresenter();
    }
    
    // Checkout Use Case
    @Bean
    public CheckoutUseCaseControl checkoutUseCase(
            CheckoutOutputBoundary checkoutPresenter,
            CartRepository cartRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository) {
        return new CheckoutUseCaseControl(checkoutPresenter, cartRepository, productRepository, orderRepository);
    }
    
    @Bean
    public CheckoutOutputBoundary checkoutPresenter() {
        return new CheckoutPresenter();
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
// ========== CANCEL ORDER USE CASE ==========

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
}
