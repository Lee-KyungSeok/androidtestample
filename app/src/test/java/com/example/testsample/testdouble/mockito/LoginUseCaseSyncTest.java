package com.example.testsample.testdouble.mockito;


import com.example.testsample.testdouble.example.LoginUseCaseSync;
import com.example.testsample.testdouble.example.LoginUseCaseSync.UseCaseResult;
import com.example.testsample.testdouble.example.authtoken.AuthTokenCache;
import com.example.testsample.testdouble.example.eventbus.EventBusPoster;
import com.example.testsample.testdouble.example.eventbus.LoggedInEvent;
import com.example.testsample.testdouble.example.networking.LoginHttpEndpointSync;
import com.example.testsample.testdouble.example.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.example.testsample.testdouble.example.networking.LoginHttpEndpointSync.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) // Mock Annotation 을 사용할 수 있다.
public class LoginUseCaseSyncTest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AUTH_TOKEN = "authToken";

    @Mock
    LoginHttpEndpointSync mLoginHttpEndpointSyncMock;

    @Mock
    AuthTokenCache mAuthTokenCacheMock;

    @Mock
    EventBusPoster mEventBusPosterMock;

    LoginUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new LoginUseCaseSync(mLoginHttpEndpointSyncMock, mAuthTokenCacheMock, mEventBusPosterMock);

        success();
    }


    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.loginSync(USERNAME, PASSWORD);

        // mLoginHttpEndpointSyncMock.loginSync 은 한번만 불려야 함을 의미한다.
        // capture 를 하면 mLoginHttpEndpointSyncMock.loginSync 가 불릴때 것을 capture 하여 저장하게 된다.
        verify(mLoginHttpEndpointSyncMock, times(1))
                .loginSync(ac.capture(), ac.capture());

        List<String> captures = ac.getAllValues();
        assertThat(captures.get(0), is(USERNAME));
        assertThat(captures.get(1), is(PASSWORD));
    }

    @Test
    public void loginSync_success_authTokenCached() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);

        // Act
        SUT.loginSync(USERNAME, PASSWORD);

        // Assert
        verify(mAuthTokenCacheMock)
                .cacheAuthToken(ac.capture());
        assertThat(ac.getValue(), is(AUTH_TOKEN));
    }

    @Test
    public void loginSync_generalError_authTokenNotCached() throws Exception {
        generalError();
        SUT.loginSync(USERNAME, PASSWORD);

        // 이전에 verify 로 등록해준 녀석들 외 다른 녀석들은 불렸으면 안 된다
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_authError_authTokenNotCached() throws Exception {
        authError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() throws Exception {
        serverError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_success_loggedInEventPosted() {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.loginSync(USERNAME, PASSWORD);
        verify(mEventBusPosterMock)
                .postEvent(ac.capture());
        assertThat(ac.getValue(), is(instanceOf(LoggedInEvent.class)));
    }

    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() throws Exception {
        generalError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_authError_noInteractionWithEventBusPoster() throws Exception {
        authError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster() throws Exception {
        serverError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_success_successReturned() {
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(UseCaseResult.SUCCESS));
    }

    @Test
    public void loginSync_generalError_failureReturned() throws Exception {
        generalError();
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_authError_failureReturned() throws Exception {
        authError();
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(UseCaseResult.FAILURE));

    }

    @Test
    public void loginSync_serverError_failureReturned() throws Exception {
        serverError();
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_networkError_networkErrorReturned() throws Exception {
        networkError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }

    private void success() throws NetworkErrorException {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN));
    }

    private void generalError() throws Exception {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.GENERAL_ERROR, ""));
    }

    private void authError() throws Exception {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.AUTH_ERROR, ""));
    }

    private void serverError() throws Exception {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.SERVER_ERROR, ""));
    }

    private void networkError() throws Exception {
        doThrow(new NetworkErrorException())
                .when(mLoginHttpEndpointSyncMock).loginSync(any(String.class), any(String.class));
    }
}
