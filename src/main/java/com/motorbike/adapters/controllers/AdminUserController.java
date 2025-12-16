package com.motorbike.adapters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.motorbike.adapters.dto.request.UpdateUserRequest;
import com.motorbike.adapters.dto.response.AddUserResponse;
import com.motorbike.adapters.dto.response.DeleteUserResponse;
import com.motorbike.adapters.dto.response.ListUsersResponse;
import com.motorbike.adapters.dto.response.UpdateUserResponse;
import com.motorbike.adapters.viewmodels.AddUserViewModel;
import com.motorbike.adapters.viewmodels.DeleteUserViewModel;
import com.motorbike.adapters.viewmodels.ListUsersViewModel;
import com.motorbike.adapters.viewmodels.UpdateUserViewModel;
import com.motorbike.business.dto.adduser.AddUserInputData;
import com.motorbike.business.dto.deleteuser.DeleteUserInputData;
import com.motorbike.business.dto.listusers.ListUsersInputData;
import com.motorbike.business.dto.updateuser.UpdateUserInputData;
import com.motorbike.business.usecase.input.AddUserInputBoundary;
import com.motorbike.business.usecase.input.DeleteUserInputBoundary;
import com.motorbike.business.usecase.input.ListUsersInputBoundary;
import com.motorbike.business.usecase.input.UpdateUserInputBoundary;



@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "*")
public class AdminUserController {

    private final ListUsersInputBoundary listUsersInputBoundary;
    private final ListUsersViewModel listUsersViewModel;

    private final AddUserInputBoundary addUserInputBoundary;
    private final AddUserViewModel addUserViewModel;

   private final DeleteUserInputBoundary deleteUserInputBoundary;
    private final DeleteUserViewModel deleteUserViewModel;

    private final UpdateUserInputBoundary updateUserInputBoundary;
    private final UpdateUserViewModel updateUserViewModel;

    @Autowired
    public AdminUserController(
            ListUsersInputBoundary listUsersInputBoundary,
            ListUsersViewModel listUsersViewModel,
            AddUserInputBoundary addUserInputBoundary,
            AddUserViewModel addUserViewModel,
             DeleteUserInputBoundary deleteUserInputBoundary,
            DeleteUserViewModel deleteUserViewModel,
           UpdateUserInputBoundary updateUserInputBoundary,
            UpdateUserViewModel updateUserViewModel) {

        this.listUsersInputBoundary = listUsersInputBoundary;
        this.listUsersViewModel = listUsersViewModel;
        this.addUserInputBoundary = addUserInputBoundary;
        this.addUserViewModel = addUserViewModel;
         this.deleteUserInputBoundary = deleteUserInputBoundary;
        this.deleteUserViewModel = deleteUserViewModel;
        this.updateUserInputBoundary = updateUserInputBoundary;
        this.updateUserViewModel = updateUserViewModel;
    }

    @GetMapping
    public ResponseEntity<ListUsersResponse> listUsers(
            @RequestParam(name = "admin", defaultValue = "false") boolean admin,
            @RequestParam(name = "keyword", required = false) String keyword) {
        ListUsersInputData input = ListUsersInputData.forAdmin(admin, keyword);
        //listUsersUseCase.execute(input);
        listUsersInputBoundary.execute(input);
        return listUsersViewModel.getResponse();
    }

    @PostMapping
    public ResponseEntity<AddUserResponse> addUser(@RequestParam(name = "admin", defaultValue = "false") boolean admin,
                                                   @RequestBody AddUserRequestBody body) {
        // Simple admin flag check; ideally check auth / role from security context
        if (!admin) {
            return ResponseEntity.status(403).body(AddUserResponse.error("FORBIDDEN", "Only admin can create users"));
        }

        AddUserInputData input = AddUserInputData.of(
                body.email, body.username, body.password, body.phoneNumber, body.address, body.role, body.active
        );

        addUserInputBoundary.execute(input);
        return addUserViewModel.getResponse();
    }
        //delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable("id") Long id,
                                                         @RequestParam(name = "admin", defaultValue = "false") boolean admin) {
        if (!admin) {
            return ResponseEntity.status(403).body(DeleteUserResponse.error("FORBIDDEN", "Only admin can delete users"));
        }

        DeleteUserInputData input = DeleteUserInputData.forAdmin(admin, id);
        deleteUserInputBoundary.execute(input);
        return deleteUserViewModel.getResponse();
    }

    //sữa thông tin người dùng
     @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponse> updateUser(@PathVariable("id") Long id,
                                                         @RequestParam(name = "admin", defaultValue = "false") boolean admin,
                                                         @RequestBody UpdateUserRequest body) {
        if (!admin) {
            return ResponseEntity.status(403).body(UpdateUserResponse.error("FORBIDDEN", "Only admin can update users"));
        }

        UpdateUserInputData input = UpdateUserInputData.forAdmin(
                admin,
                id,
                body.email,
                body.username,
                body.password,
                body.phoneNumber,
                body.address,
                body.role,
                body.active
        );

        updateUserInputBoundary.execute(input);
        return updateUserViewModel.getResponse();
    }

    // small internal class for request body mapping
    public static class AddUserRequestBody {
        public String email;
        public String username;
        public String password;
        public String phoneNumber;
        public String address;
        public String role;
        public Boolean active;
    }
}