package com.motorbike.business.usecase.user.update;

public class UpdateUserInputData {
    private int id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private String role;

    public UpdateUserInputData(int id, String username, String password,
                               String email, String fullName,
                               String phone, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
}
