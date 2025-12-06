package com.motorbike.adapters.controllers;

import com.motorbike.business.usecase.input.GetAllUsersInputBoundary;
import com.motorbike.business.usecase.input.SearchUsersInputBoundary;
import com.motorbike.business.usecase.input.DeleteUserInputBoundary;
import com.motorbike.business.usecase.input.AddUserInputBoundary;
import com.motorbike.business.usecase.input.UpdateUserInputBoundary;
import com.motorbike.business.dto.user.SearchUsersInputData;
import com.motorbike.business.dto.user.DeleteUserInputData;
import com.motorbike.business.dto.user.AddUserInputData;
import com.motorbike.business.dto.user.UpdateUserInputData;
import com.motorbike.adapters.viewmodels.GetAllUsersViewModel;
import com.motorbike.adapters.viewmodels.SearchUsersViewModel;
import com.motorbike.adapters.viewmodels.DeleteUserViewModel;
import com.motorbike.adapters.viewmodels.AddUserViewModel;
import com.motorbike.adapters.viewmodels.UpdateUserViewModel;
import com.motorbike.domain.entities.VaiTro;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "*")
public class AdminUserController {

    private final GetAllUsersInputBoundary getAllUsersUseCase;
    private final GetAllUsersViewModel getAllUsersViewModel;
    
    private final SearchUsersInputBoundary searchUsersUseCase;
    private final SearchUsersViewModel searchUsersViewModel;
    
    private final DeleteUserInputBoundary deleteUserUseCase;
    private final DeleteUserViewModel deleteUserViewModel;
    
    private final AddUserInputBoundary addUserUseCase;
    private final AddUserViewModel addUserViewModel;
    
    private final UpdateUserInputBoundary updateUserUseCase;
    private final UpdateUserViewModel updateUserViewModel;

    public AdminUserController(
            GetAllUsersInputBoundary getAllUsersUseCase,
            GetAllUsersViewModel getAllUsersViewModel,
            SearchUsersInputBoundary searchUsersUseCase,
            SearchUsersViewModel searchUsersViewModel,
            DeleteUserInputBoundary deleteUserUseCase,
            DeleteUserViewModel deleteUserViewModel,
            AddUserInputBoundary addUserUseCase,
            AddUserViewModel addUserViewModel,
            UpdateUserInputBoundary updateUserUseCase,
            UpdateUserViewModel updateUserViewModel) {
        this.getAllUsersUseCase = getAllUsersUseCase;
        this.getAllUsersViewModel = getAllUsersViewModel;
        this.searchUsersUseCase = searchUsersUseCase;
        this.searchUsersViewModel = searchUsersViewModel;
        this.deleteUserUseCase = deleteUserUseCase;
        this.deleteUserViewModel = deleteUserViewModel;
        this.addUserUseCase = addUserUseCase;
        this.addUserViewModel = addUserViewModel;
        this.updateUserUseCase = updateUserUseCase;
        this.updateUserViewModel = updateUserViewModel;
    }

    // Use Case: GetAllUsers
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        getAllUsersUseCase.execute(null);

        if (getAllUsersViewModel.hasError) {
            return ResponseEntity.status(500)
                    .body(Map.of(
                        "success", false,
                        "errorCode", getAllUsersViewModel.errorCode,
                        "errorMessage", getAllUsersViewModel.errorMessage
                    ));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "users", getAllUsersViewModel.users
        ));
    }

    // Use Case: SearchUsers
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role) {

        SearchUsersInputData input = new SearchUsersInputData(keyword, null, null);
        searchUsersUseCase.execute(input);

        if (searchUsersViewModel.hasError) {
            return ResponseEntity.status(400)
                    .body(Map.of(
                        "success", false,
                        "errorCode", searchUsersViewModel.errorCode,
                        "errorMessage", searchUsersViewModel.errorMessage
                    ));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "users", searchUsersViewModel.users
        ));
    }

    // Use Case: DeleteUser
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        DeleteUserInputData input = new DeleteUserInputData(userId);
        deleteUserUseCase.execute(input);

        if (deleteUserViewModel.hasError) {
            return ResponseEntity.status(400)
                    .body(Map.of(
                        "success", false,
                        "errorCode", deleteUserViewModel.errorCode,
                        "errorMessage", deleteUserViewModel.errorMessage
                    ));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", deleteUserViewModel.successMessage
        ));
    }

    // Use Case: AddUser / CreateUser
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> request) {
        String roleStr = (String) request.get("role");
        VaiTro role = "ADMIN".equals(roleStr) ? VaiTro.ADMIN : VaiTro.CUSTOMER;
        Boolean active = request.get("active") != null ? (Boolean) request.get("active") : true;
        
        AddUserInputData input = new AddUserInputData(
            (String) request.get("name"),
            (String) request.get("email"),
            (String) request.get("username"),
            (String) request.get("password"),
            (String) request.get("phone"),
            (String) request.get("address"),
            role,
            active
        );
        
        addUserUseCase.execute(input);

        if (addUserViewModel.hasError) {
            return ResponseEntity.status(400)
                    .body(Map.of(
                        "success", false,
                        "errorCode", addUserViewModel.errorCode,
                        "errorMessage", addUserViewModel.errorMessage
                    ));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "userId", addUserViewModel.maTaiKhoan,
            "message", addUserViewModel.successMessage
        ));
    }

    // Use Case: UpdateUser
    @PostMapping("/{userId}/update")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> request) {
        
        String roleStr = (String) request.get("role");
        VaiTro role = "ADMIN".equals(roleStr) ? VaiTro.ADMIN : VaiTro.CUSTOMER;
        Boolean active = request.get("active") != null ? (Boolean) request.get("active") : true;
        
        UpdateUserInputData input = new UpdateUserInputData(
            userId,
            (String) request.get("name"),
            (String) request.get("email"),
            (String) request.get("phone"),
            (String) request.get("address"),
            role,
            active
        );
        
        updateUserUseCase.execute(input);

        if (updateUserViewModel.hasError) {
            return ResponseEntity.status(400)
                    .body(Map.of(
                        "success", false,
                        "errorCode", updateUserViewModel.errorCode,
                        "errorMessage", updateUserViewModel.errorMessage
                    ));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", updateUserViewModel.successMessage
        ));
    }
}
