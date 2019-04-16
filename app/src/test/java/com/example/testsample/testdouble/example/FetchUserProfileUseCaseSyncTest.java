package com.example.testsample.testdouble.example;

import com.example.testsample.testdouble.example.networking.NetworkErrorException;
import com.example.testsample.testdouble.example.networking.UserProfileHttpEndpointSync;
import com.example.testsample.testdouble.example.users.User;
import com.example.testsample.testdouble.example.users.UsersCache;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.example.testsample.testdouble.example.FetchUserProfileUseCaseSync.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class FetchUserProfileUseCaseSyncTest {

    public static final String USER_ID = "USER_ID";
    public static final String FULL_NAME = "FULL_NAME";
    public static final String IMAGE_URL = "IMAGE_URL";

    UserProfileHttpEndpointSyncTd mUserProfileHttpEndpointSyncTd;
    UsersCacheTd mUsersCacheTd;
    FetchUserProfileUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        mUserProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        mUsersCacheTd = new UsersCacheTd();
        SUT = new FetchUserProfileUseCaseSync(mUserProfileHttpEndpointSyncTd, mUsersCacheTd);
    }

    @Test
    public void fetchUserProfileSync_success_userIdPassedToEndpoint() {
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUserProfileHttpEndpointSyncTd.mUserId, is(USER_ID));
    }

    @Test
    public void fetchUserProfileSync_success_userCached() {
        SUT.fetchUserProfileSync(USER_ID);
        User user = mUsersCacheTd.getUser(USER_ID);
        assertThat(user.getUserId(), is(USER_ID));
        assertThat(user.getFullName(), is(FULL_NAME));
        assertThat(user.getImageUrl(), is(IMAGE_URL));
    }

    @Test
    public void fetchUserProfileSync_generalError_userNotCached() {
        mUserProfileHttpEndpointSyncTd.isGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUsersCacheTd.getUser(USER_ID), is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_authError_userNotCached() {
        mUserProfileHttpEndpointSyncTd.isAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUsersCacheTd.getUser(USER_ID), is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_serverError_userNotCached() {
        mUserProfileHttpEndpointSyncTd.isServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUsersCacheTd.getUser(USER_ID), is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_success_successReturned() {
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(UseCaseResult.SUCCESS));
    }

    @Test
    public void fetchUserProfileSync_generalError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.isGeneralError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfileSync_authError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.isAuthError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfileSync_serverError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.isServerError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfileSync_networkError_networkErrorReturned() {
        mUserProfileHttpEndpointSyncTd.isNetworkError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }

    private class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {

        public String mUserId = "";

        public boolean isGeneralError;
        public boolean isAuthError;
        public boolean isServerError;
        public boolean isNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserId = userId;
            if (isGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            } else if (isAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
            } else if (isServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            } else if (isNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, FULL_NAME, IMAGE_URL);
            }
        }
    }

    private class UsersCacheTd implements UsersCache {

        private List<User> mUsers = new ArrayList<>(1);

        @Override
        public void cacheUser(User user) {
            User existingUser = getUser(user.getUserId());
            if (existingUser != null) {
                mUsers.remove(existingUser);
            }

            mUsers.add(user);

        }

        @Nullable
        @Override
        public User getUser(String userId) {
            for (User user : mUsers) {
                if (user.getUserId().equals(userId)) {
                    return user;
                }
            }
            return null;
        }
    }
}