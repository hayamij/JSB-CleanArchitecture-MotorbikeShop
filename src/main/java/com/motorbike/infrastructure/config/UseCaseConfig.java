package com.motorbike.infrastructure.config;

import com.motorbike.adapters.presenters.AddAccessoryPresenter;
import com.motorbike.adapters.presenters.AddMotorbikePresenter;
import com.motorbike.adapters.presenters.AddToCartPresenter;
import com.motorbike.adapters.presenters.AddUserPresenter;
import com.motorbike.adapters.presenters.CancelOrderPresenter;
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
import com.motorbike.business.ports.exporter.CSVExporter;
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
import com.motorbike.business.usecase.output.AddAccessoryOutputBoundary;
import com.motorbike.business.usecase.output.AddMotorbikeOutputBoundary;
import com.motorbike.business.usecase.output.AddUserOutputBoundary;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;
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
import com.motorbike.infrastructure.persistence.jpa.repositories.XeMayJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            CartRepository cartRepository) {
        return new LoginUseCaseControl(loginPresenter, userRepository, cartRepository);
    }
    
    @Bean
    public LoginOutputBoundary loginPresenter(LoginViewModel loginViewModel) {
        return new LoginPresenter(loginViewModel);
    }
    
    @Bean
    public RegisterInputBoundary registerUseCase(
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
    public GetProductDetailInputBoundary getProductDetailUseCase(
            GetProductDetailOutputBoundary productDetailPresenter,
            ProductRepository productRepository) {
        return new GetProductDetailUseCaseControl(productDetailPresenter, productRepository);
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
            ProductRepository productRepository) {
        return new ViewCartUseCaseControl(viewCartPresenter, cartRepository, productRepository);
    }
    
    @Bean
    public ViewCartOutputBoundary viewCartPresenter(ViewCartViewModel viewCartViewModel) {
        return new ViewCartPresenter(viewCartViewModel);
    }
    
    @Bean
    public UpdateCartQuantityInputBoundary updateCartQuantityUseCase(
            UpdateCartQuantityOutputBoundary updateCartQuantityPresenter,
            CartRepository cartRepository) {
        return new UpdateCartQuantityUseCaseControl(updateCartQuantityPresenter, cartRepository);
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
    public ListAllOrdersInputBoundary listAllOrdersUseCase(
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
            ExcelParser excelParser
    ) {
        return new ImportMotorbikesUseCaseControl(presenter, motorbikeRepository, excelParser);
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
            ExcelParser excelParser
    ) {
        return new ImportAccessoriesUseCaseControl(presenter, accessoryRepository, excelParser);
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
        UserRepository userRepository
) {
    return new AddUserUseCaseControl(presenter, userRepository);
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
        UserRepository userRepository
) {
    return new CreateUserUseCaseControl(presenter, userRepository);
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

    // ===== MOTORBIKE MANAGEMENT USE CASES =====

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
            ProductRepository productRepository
    ) {
        return new GetAllProductsUseCaseControl(presenter, productRepository);
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

}