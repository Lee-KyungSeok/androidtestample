package com.example.testsample.testdouble.example.users;

import androidx.annotation.Nullable;

public interface UsersCache {

    void cacheUser(User user);

    @Nullable
    User getUser(String userId);
}
