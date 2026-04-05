package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.user.SearchUsersOutputData.UserItem;

import java.util.List;

public class SearchUsersViewModel {
    public boolean hasError = false;
    public String errorCode;
    public String errorMessage;
    public List<UserItem> users;
}
