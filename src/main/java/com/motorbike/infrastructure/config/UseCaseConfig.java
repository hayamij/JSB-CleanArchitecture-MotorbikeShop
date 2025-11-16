package com.motorbike.infrastructure.config;

import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.*;
import com.motorbike.business.usecase.control.*;
import com.motorbike.adapters.presenters.*;
import com.motorbike.adapters.viewmodels.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

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
    public ViewCartOutputBoundary viewCartPresenter(ViewCartViewModel viewCartViewModel) {
        return new ViewCartPresenter(viewCartViewModel);
    }
    
    // Update Cart Quantity Use Case
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
    public CheckoutOutputBoundary checkoutPresenter(CheckoutViewModel checkoutViewModel) {
        return new CheckoutPresenter(checkoutViewModel);
    }
}
