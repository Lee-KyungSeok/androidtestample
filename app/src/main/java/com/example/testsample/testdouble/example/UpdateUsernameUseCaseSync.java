package com.example.testsample.testdouble.example;

import com.example.testsample.testdouble.example.eventbus.EventBusPoster;
import com.example.testsample.testdouble.example.eventbus.UserDetailsChangedEvent;
import com.example.testsample.testdouble.example.networking.NetworkErrorException;
import com.example.testsample.testdouble.example.networking.UpdateUsernameHttpEndpointSync;
import com.example.testsample.testdouble.example.networking.UpdateUsernameHttpEndpointSync.EndpointResult;
import com.example.testsample.testdouble.example.networking.UpdateUsernameHttpEndpointSync.EndpointResultStatus;
import com.example.testsample.testdouble.example.users.User2;
import com.example.testsample.testdouble.example.users.UsersCache2;

public class UpdateUsernameUseCaseSync {

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    private final UpdateUsernameHttpEndpointSync mUpdateUsernameHttpEndpointSync;
    private final UsersCache2 mUsersCache;
    private final EventBusPoster mEventBusPoster;

    public UpdateUsernameUseCaseSync(UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSync,
                                     UsersCache2 usersCache,
                                     EventBusPoster eventBusPoster) {
        mUpdateUsernameHttpEndpointSync = updateUsernameHttpEndpointSync;
        mUsersCache = usersCache;
        mEventBusPoster = eventBusPoster;
    }

    public UseCaseResult updateUsernameSync(String userId, String username) {
        EndpointResult endpointResult = null;
        try {
            endpointResult = mUpdateUsernameHttpEndpointSync.updateUsername(userId, username);
        } catch (NetworkErrorException e) {
            return UseCaseResult.NETWORK_ERROR;
        }

        if (isSuccessfulEndpointResult(endpointResult)) {
            User2 user = new User2(endpointResult.getUserId(), endpointResult.getUsername());
            mEventBusPoster.postEvent(new UserDetailsChangedEvent(new User2(userId, username)));
            mUsersCache.cacheUser(user);
            return UseCaseResult.SUCCESS;
        } else {
            return UseCaseResult.FAILURE;
        }
    }

    private boolean isSuccessfulEndpointResult(EndpointResult endpointResult) {
        return endpointResult.getStatus() == EndpointResultStatus.SUCCESS;
    }
}
