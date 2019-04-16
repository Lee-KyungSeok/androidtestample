package com.example.testsample.testdouble.example;

import com.example.testsample.testdouble.example.authtoken.AuthTokenCache;
import com.example.testsample.testdouble.example.eventbus.EventBusPoster;
import com.example.testsample.testdouble.example.eventbus.LoggedInEvent;
import com.example.testsample.testdouble.example.networking.LoginHttpEndpointSync;
import com.example.testsample.testdouble.example.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;

import static com.example.testsample.testdouble.example.LoginUseCaseSync.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class LoginUseCaseSyncTest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AUTH_TOKEN = "authToken";

    LoginHttpEndpointSyncTd mLoginHttpEndpointSyncTd;
    AuthTokenCacheTd mAuthTokenCacheTd;
    EventBusPosterTd mEventBusPosterTd;

    LoginUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        mLoginHttpEndpointSyncTd = new LoginHttpEndpointSyncTd();
        mAuthTokenCacheTd = new AuthTokenCacheTd();
        mEventBusPosterTd = new EventBusPosterTd();
        SUT = new LoginUseCaseSync(mLoginHttpEndpointSyncTd, mAuthTokenCacheTd, mEventBusPosterTd);
    }


    // username and password passed to the endpoint

    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() {
        SUT.loginSync(USERNAME, PASSWORD);
        assertThat(mLoginHttpEndpointSyncTd.mUsername, is(USERNAME));
        assertThat(mLoginHttpEndpointSyncTd.mPassword, is(PASSWORD));
    }

    // if login succeeds - user's auth token must be cached
    @Test
    public void loginSync_success_authTokenCached() {
        SUT.loginSync(USERNAME, PASSWORD);
        assertThat(mAuthTokenCacheTd.mAuthToken, is(AUTH_TOKEN));
    }

    // if login fails - auth token is not changed
    @Test
    public void loginSync_generalError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertThat(mAuthTokenCacheTd.mAuthToken, is(""));
    }

    @Test
    public void loginSync_authError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertThat(mAuthTokenCacheTd.mAuthToken, is(""));
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertThat(mAuthTokenCacheTd.mAuthToken, is(""));
    }

    // if login succeeds - login event posted to event bus
    @Test
    public void loginSync_success_loggedInEventPosted() {
        SUT.loginSync(USERNAME, PASSWORD);
        assertThat(mEventBusPosterTd.mEvent, is(instanceOf(LoggedInEvent.class)));
    }

    // if login fails - no login event posted
    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertThat(mEventBusPosterTd.mInteractionsCount, is(0));
    }

    @Test
    public void loginSync_authError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertThat(mEventBusPosterTd.mInteractionsCount, is(0));
    }

    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertThat(mEventBusPosterTd.mInteractionsCount, is(0));
    }

    // if login succeeds - success returned
    @Test
    public void loginSync_success_successReturned() {
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(UseCaseResult.SUCCESS));
    }

    // fails - fail returned
    @Test
    public void loginSync_generalError_failureReturned() {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_authError_failureReturned() {
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_serverError_failureReturned() {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    // network - network error returned
    @Test
    public void loginSync_networkError_networkErrorReturned() {
        mLoginHttpEndpointSyncTd.mIsNetworkError = true;
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }


    // ---------------------------------------------------------------------------------------------
    // Helper classes

    private static class LoginHttpEndpointSyncTd implements LoginHttpEndpointSync {
        public String mUsername = "";
        private String mPassword = "";

        public boolean mIsGeneralError;
        public boolean mIsAuthError;
        public boolean mIsServerError;
        public boolean mIsNetworkError;

        @Override
        public EndpointResult loginSync(String username, String password) throws NetworkErrorException {
            mUsername = username;
            mPassword = password;
            if (mIsGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "");
            } else if (mIsAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "");
            } else if (mIsServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "");
            } else if (mIsNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN);
            }
        }
    }

    private static class AuthTokenCacheTd implements AuthTokenCache {

        String mAuthToken = "";

        @Override
        public void cacheAuthToken(String authToken) {
            mAuthToken = authToken;
        }

        @Override
        public String getAuthToken() {
            return mAuthToken;
        }
    }

    private static class EventBusPosterTd implements EventBusPoster {

        public Object mEvent;
        public int mInteractionsCount;

        @Override
        public void postEvent(Object event) {
            mEvent = event;
            mInteractionsCount++;
        }
    }
}