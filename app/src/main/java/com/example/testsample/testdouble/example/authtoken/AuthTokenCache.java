package com.example.testsample.testdouble.example.authtoken;

public interface AuthTokenCache {
    void cacheAuthToken(String authToken);

    String getAuthToken();
}
