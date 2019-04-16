package com.example.testsample.testdouble.example;

import com.example.testsample.testdouble.example.networking.NetworkErrorException;
import com.example.testsample.testdouble.example.networking.UserProfileHttpEndpointSync;
import com.example.testsample.testdouble.example.users.User;
import com.example.testsample.testdouble.example.users.UsersCache;

import static com.example.testsample.testdouble.example.networking.UserProfileHttpEndpointSync.*;

public class FetchUserProfileUseCaseSync {

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    private final UserProfileHttpEndpointSync mUserProfileHttpEndpointSync;
    private final UsersCache mUsersCache;

    public FetchUserProfileUseCaseSync(UserProfileHttpEndpointSync userProfileHttpEndpointSync, UsersCache usersCache) {
        this.mUserProfileHttpEndpointSync = userProfileHttpEndpointSync;
        this.mUsersCache = usersCache;
    }

    public UseCaseResult fetchUserProfileSync(String userId) {
        EndpointResult endpointResult;
        try {
            endpointResult = mUserProfileHttpEndpointSync.getUserProfile(userId);
        } catch (NetworkErrorException error) {
            return UseCaseResult.NETWORK_ERROR;
        }

        if (isSuccessfulEndpointResult(endpointResult)) {
            mUsersCache.cacheUser(
                    new User(endpointResult.getUserId(), endpointResult.getFullName(), endpointResult.getImageUrl())
            );
            return UseCaseResult.SUCCESS;
        } else {
            return UseCaseResult.FAILURE;
        }
    }

    private boolean isSuccessfulEndpointResult(EndpointResult endpointResult) {
        return endpointResult.getStatus() == UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS;
    }
}
