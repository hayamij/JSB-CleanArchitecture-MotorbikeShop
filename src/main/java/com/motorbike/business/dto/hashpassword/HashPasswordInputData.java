package com.motorbike.business.dto.hashpassword;

public class HashPasswordInputData {
    private final String plainPassword;

    public HashPasswordInputData(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getPlainPassword() {
        return plainPassword;
    }
}
