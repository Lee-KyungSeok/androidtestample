package com.example.testsample.testdouble.example2;

public class ServerUsernameValidator {
    // this sleep mimics network request that checks whether username is free, but fails due to
    // absence of network connection
    public static boolean isValidUserName(String username) {
        try {
            Thread.sleep(1000);
            throw new RuntimeException("no network connection");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
