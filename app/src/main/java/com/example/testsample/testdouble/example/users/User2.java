package com.example.testsample.testdouble.example.users;

public class User2 {
    private final String mUserId;
    private final String mUsername;

    public User2(String userId, String username) {
        mUserId = userId;
        mUsername = username;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getUsername() {
        return mUsername;
    }
}