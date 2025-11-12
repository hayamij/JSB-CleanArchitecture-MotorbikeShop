package com.motorbike.infrastructure.config;

import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.*;
import com.motorbike.business.usecase.impl.*;
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
    
    // Login Use Case
    @Bean
    public LoginInputBoundary loginUseCase(
            LoginOutputBoundary loginPresenter,
            UserRepository userRepository,
            CartRepository cartRepository) {
        return new LoginUseCaseImpl(loginPresenter, userRepository, cartRepository);
    }
    
    @Bean
    public LoginOutputBoundary loginPresenter(LoginViewModel loginViewModel) {
        return new LoginPresenter(loginViewModel);
    }
    
    // Register Use Case
    @Bean
    public RegisterInputBoundary registerUseCase(
            RegisterOutputBoundary registerPresenter,
            UserRepository userRepository,
            CartRepository cartRepository) {
        return new RegisterUseCaseImpl(registerPresenter, userRepository, cartRepository);
    }
    
    @Bean
    public RegisterOutputBoundary registerPresenter(RegisterViewModel registerViewModel) {
        return new RegisterPresenter(registerViewModel);
    }
    
    // Get Product Detail Use Case
    @Bean
    public GetProductDetailInputBoundary getProductDetailUseCase(
            GetProductDetailOutputBoundary productDetailPresenter,
            ProductRepository productRepository) {
        return new GetProductDetailUseCaseImpl(productDetailPresenter, productRepository);
    }
    
    @Bean
    public GetProductDetailOutputBoundary productDetailPresenter(ProductDetailViewModel productDetailViewModel) {
        return new ProductDetailPresenter(productDetailViewModel);
    }
    
    // Add To Cart Use Case
    @Bean
    public AddToCartInputBoundary addToCartUseCase(
            AddToCartOutputBoundary addToCartPresenter,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        return new AddToCartUseCaseImpl(addToCartPresenter, cartRepository, productRepository);
    }
    
    @Bean
    public AddToCartOutputBoundary addToCartPresenter(AddToCartViewModel addToCartViewModel) {
        return new AddToCartPresenter(addToCartViewModel);
    }
    
    // View Cart Use Case
    @Bean
    public ViewCartInputBoundary viewCartUseCase(
            ViewCartOutputBoundary viewCartPresenter,
            CartRepository cartRepository) {
        return new ViewCartUseCaseImpl(viewCartPresenter, cartRepository);
    }
    
    @Bean
    public ViewCartOutputBoundary viewCartPresenter() {
        return new ViewCartPresenter();
    }
    
    // Update Cart Quantity Use Case
    @Bean
    public UpdateCartQuantityInputBoundary updateCartQuantityUseCase(
            UpdateCartQuantityOutputBoundary updateCartQuantityPresenter,
            CartRepository cartRepository) {
        return new UpdateCartQuantityUseCaseImpl(updateCartQuantityPresenter, cartRepository);
    }
    
    @Bean
    public UpdateCartQuantityOutputBoundary updateCartQuantityPresenter() {
        return new UpdateCartQuantityPresenter();
    }
    
    // Checkout Use Case
    @Bean
    public CheckoutInputBoundary checkoutUseCase(
            CheckoutOutputBoundary checkoutPresenter,
            CartRepository cartRepository,
            ProductRepository productRepository) {
        return new CheckoutUseCaseImpl(checkoutPresenter, cartRepository, productRepository);
    }
    
    @Bean
    public CheckoutOutputBoundary checkoutPresenter() {
        return new CheckoutPresenter();
    }
}
