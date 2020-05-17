package com.mybank.accountmanagement.context;

import com.mybank.accountmanagement.models.User;

public interface UserContextService {
    User getCurrentUser();
    Long getCurrentUserId();
    String getCurrentUsername();
}
