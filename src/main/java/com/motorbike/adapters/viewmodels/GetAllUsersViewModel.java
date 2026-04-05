package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.user.GetAllUsersOutputData.UserItem;

import java.util.List;

public class GetAllUsersViewModel {
    public boolean hasError = false;
    public String errorCode;
    public String errorMessage;
    public List<UserItem> users;
}
