package com.motorbike.infrastructure.config;

import com.motorbike.adapters.presenters.AddMotorbikePresenter;
import com.motorbike.adapters.viewmodels.AddMotorbikeViewModel;
import com.motorbike.business.usecase.control.AddMotorbikeUseCaseControl;
import com.motorbike.business.usecase.input.AddMotorbikeInputBoundary;
import com.motorbike.business.usecase.output.AddMotorbikeOutputBoundary;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import com.motorbike.adapters.presenters.AddToCartPresenter;
import com.motorbike.adapters.presenters.CancelOrderPresenter;
import com.motorbike.adapters.presenters.CheckoutPresenter;
import com.motorbike.adapters.presenters.GetAllMotorbikesPresenter;
import com.motorbike.adapters.presenters.ListAllOrdersPresenter;
import com.motorbike.adapters.presenters.LoginPresenter;
import com.motorbike.adapters.presenters.ProductDetailPresenter;
import com.motorbike.adapters.presenters.RegisterPresenter;
import com.motorbike.adapters.presenters.SearchMotorbikesPresenter;
import com.motorbike.adapters.presenters.UpdateCartQuantityPresenter;
import com.motorbike.adapters.presenters.ViewCartPresenter;
import com.motorbike.adapters.repositories.MotorbikeRepositoryAdapter;
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
import com.motorbike.business.usecase.control.UpdateCartQuantityUseCaseControl;
import com.motorbike.business.usecase.control.ViewCartUseCaseControl;
import com.motorbike.business.usecase.input.GetAllMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.SearchMotorbikesInputBoundary;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;
import com.motorbike.business.usecase.output.CheckoutOutputBoundary;
import com.motorbike.business.usecase.output.GetAllMotorbikesOutputBoundary;
import com.motorbike.business.usecase.output.GetProductDetailOutputBoundary;
import com.motorbike.business.usecase.output.ListAllOrdersOutputBoundary;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.business.usecase.output.RegisterOutputBoundary;
import com.motorbike.business.usecase.output.SearchMotorbikesOutputBoundary;
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
public AddMotorbikeInputBoundary addMotorbikeInputBoundary(
        AddMotorbikeUseCaseControl useCase
) {
    return useCase;
}

@Bean
public MotorbikeRepository motorbikeRepository(
        XeMayJpaRepository jpaRepository
) {
    return new MotorbikeRepositoryAdapter(jpaRepository);
}


}
