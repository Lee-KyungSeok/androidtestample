package com.example.testsample.testdouble.example.eventbus;

import com.example.testsample.testdouble.example.users.User2;

public class UserDetailsChangedEvent {

    private final User2 mUser;

    public UserDetailsChangedEvent(User2 user) {
        mUser = user;
    }

    public User2 getUser() {
        return mUser;
    }
}