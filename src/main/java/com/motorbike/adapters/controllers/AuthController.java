package com.motorbike.adapters.controllers;

import com.motorbike.business.dto.login.LoginInputData;
import com.motorbike.business.dto.register.RegisterInputData;
import com.motorbike.business.usecase.control.LoginUseCaseControl;
import com.motorbike.business.usecase.control.RegisterUseCaseControl;
import com.motorbike.adapters.viewmodels.LoginViewModel;
import com.motorbike.adapters.viewmodels.RegisterViewModel;
import com.motorbike.adapters.dto.request.RegisterRequest;
import com.motorbike.adapters.dto.request.LoginRequest;
import com.motorbike.adapters.dto.response.RegisterResponse;
import com.motorbike.adapters.dto.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final LoginUseCaseControl loginUseCase;
    private final RegisterUseCaseControl registerUseCase;
    private final LoginViewModel loginViewModel;
    private final RegisterViewModel registerViewModel;

    @Autowired
    public AuthController(LoginUseCaseControl loginUseCase,
                         RegisterUseCaseControl registerUseCase,
                         LoginViewModel loginViewModel,
                         RegisterViewModel registerViewModel) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
        this.loginViewModel = loginViewModel;
        this.registerViewModel = registerViewModel;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterInputData inputData = new RegisterInputData(
            request.getName(),
            request.getEmail(),
            request.getUsername(),
            request.getPassword(),
            request.getConfirmPassword(),
            request.getPhone(),
            request.getAddress()
        );
        
        registerUseCase.execute(inputData);
        
        if (registerViewModel.success) {
            RegisterResponse response = new RegisterResponse(
                true, registerViewModel.userId, registerViewModel.email,
                registerViewModel.username, registerViewModel.roleDisplay, registerViewModel.phone, registerViewModel.address,
                registerViewModel.registeredAtDisplay, registerViewModel.autoLoginEnabled,
                registerViewModel.sessionToken, registerViewModel.message, null, null
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            RegisterResponse response = new RegisterResponse(
                false, null, null, null, null, null, null, null, false, null, null,
                registerViewModel.errorCode, registerViewModel.errorMessage
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginInputData inputData = new LoginInputData(
            request.getUsername(),
            request.getPassword()
        );
        
        loginUseCase.execute(inputData);
        
        if (loginViewModel.success) {
            LoginResponse response = new LoginResponse(
                true, loginViewModel.userId, loginViewModel.email,
                loginViewModel.username, loginViewModel.role, loginViewModel.phone, loginViewModel.address,
                loginViewModel.cartId, loginViewModel.cartMerged, loginViewModel.mergedItemsCount,
                loginViewModel.message, null, null
            );
            return ResponseEntity.ok(response);
        } else {
            LoginResponse response = new LoginResponse(
                false, null, null, null, null, null, null, null, false, 0, null,
                loginViewModel.errorCode, loginViewModel.errorMessage
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
