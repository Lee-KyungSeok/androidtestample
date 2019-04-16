package com.example.testsample.tdd.excercise.users;

import androidx.annotation.Nullable;

public interface UsersCache {

    void cacheUser(User user);

    @Nullable
    User getUser(String userId);
}
