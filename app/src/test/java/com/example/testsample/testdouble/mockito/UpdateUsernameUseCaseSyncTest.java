package com.example.testsample.testdouble.mockito;

import com.example.testsample.testdouble.example.UpdateUsernameUseCaseSync;
import com.example.testsample.testdouble.example.eventbus.EventBusPoster;
import com.example.testsample.testdouble.example.eventbus.UserDetailsChangedEvent;
import com.example.testsample.testdouble.example.networking.NetworkErrorException;
import com.example.testsample.testdouble.example.networking.UpdateUsernameHttpEndpointSync;
import com.example.testsample.testdouble.example.users.User2;
import com.example.testsample.testdouble.example.users.UsersCache2;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static com.example.testsample.testdouble.example.UpdateUsernameUseCaseSync.*;
import static com.example.testsample.testdouble.example.networking.UpdateUsernameHttpEndpointSync.*;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class UpdateUsernameUseCaseSyncTest {

    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";

    UpdateUsernameUseCaseSync SUT;
    UpdateUsernameHttpEndpointSync mUpdateUsernameHttpEndpointSyncMock;
    UsersCache2 mUsersCache2Mock;
    EventBusPoster mEventBusPosterMock;

    @Before
    public void setUp() throws Exception {
        mUpdateUsernameHttpEndpointSyncMock = mock(UpdateUsernameHttpEndpointSync.class);
        mUsersCache2Mock = mock(UsersCache2.class);
        mEventBusPosterMock = mock(EventBusPoster.class);
        SUT = new UpdateUsernameUseCaseSync(mUpdateUsernameHttpEndpointSyncMock, mUsersCache2Mock, mEventBusPosterMock);

        success();
    }

    @Test
    public void updateUsername_success_userIdAndUsernamePassedToEndpoint() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(mUpdateUsernameHttpEndpointSyncMock, times(1))
                .updateUsername(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertThat(captures.get(0), is(USER_ID));
        assertThat(captures.get(1), is(USERNAME));
    }

    @Test
    public void updateUsername_success_userCached() {
        ArgumentCaptor<User2> ac = ArgumentCaptor.forClass(User2.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);

        verify(mUsersCache2Mock)
                .cacheUser(ac.capture());

        User2 cachedUser = ac.getValue();
        assertThat(cachedUser.getUserId(), is(USER_ID));
        assertThat(cachedUser.getUsername(), is(USERNAME));

    }

    @Test
    public void updateUsername_generalError_userNotCached() throws Exception {
        generalError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCache2Mock);
    }

    @Test
    public void updateUsername_authError_userNotCached() throws Exception {
        authError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCache2Mock);
    }

    @Test
    public void updateUsername_serverError_userNotCached() throws Exception {
        serverError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCache2Mock);
    }

    @Test
    public void updateUsername_success_UserDetailsChangedEventPosted() {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);

        verify(mEventBusPosterMock)
                .postEvent(ac.capture());

        assertThat(ac.getValue(), is(instanceOf(UserDetailsChangedEvent.class)));

    }

    @Test
    public void updateUsername_generalError_noInteractionWithEventBusPoster() throws Exception {
        generalError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsername_authError_noInteractionWithEventBusPoster() throws Exception {
        authError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsername_serverError_noInteractionWithEventBusPoster() throws Exception {
        serverError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsername_success_successReturned() throws Exception {
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UseCaseResult.SUCCESS));
    }

    @Test
    public void updateUsername_generalError_failureReturned() throws Exception {
        generalError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_authError_failureReturned() throws Exception {
        authError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_serverError_failureReturned() throws Exception {
        serverError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_networkError_networkErrorReturned() throws Exception {
        networkError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }

    private void networkError() throws Exception {
        doThrow(new NetworkErrorException())
                .when(mUpdateUsernameHttpEndpointSyncMock)
                .updateUsername(anyString(), anyString());
    }

    private void success() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, USERNAME));
    }

    private void generalError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", ""));
    }

    private void authError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", ""));
    }

    private void serverError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", ""));
    }
}
