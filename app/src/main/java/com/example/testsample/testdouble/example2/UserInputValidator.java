package com.example.testsample.testdouble.example2;

public class UserInputValidator {

    public boolean isValidFullName(String fullName) {
        return FullNameValidator.isValidFullName(fullName);
    }

    public boolean isValidUserName(String username) {
        return ServerUsernameValidator.isValidUserName(username);
    }
}
