package com.example.testsample.tdd.excercise;

import com.example.testsample.tdd.excercise.networking.FetchUserHttpEndpointSync;
import com.example.testsample.tdd.excercise.networking.FetchUserHttpEndpointSync.EndpointResult;
import com.example.testsample.tdd.excercise.networking.NetworkErrorException;
import com.example.testsample.tdd.excercise.users.User;
import com.example.testsample.tdd.excercise.users.UsersCache;

class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {

    private FetchUserHttpEndpointSync fetchUserHttpEndpointSync;
    private UsersCache usersCache;

    public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSync fetchUserHttpEndpointSync, UsersCache usersCache) {
        this.fetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
        this.usersCache = usersCache;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId) {
        User user = usersCache.getUser(userId);
        if (user != null ) {
            return new UseCaseResult(Status.SUCCESS, user);
        }

        EndpointResult endpointResult;
        try {
            endpointResult = fetchUserHttpEndpointSync.fetchUserSync(userId);
        } catch (NetworkErrorException e) {
            return new UseCaseResult(Status.NETWORK_ERROR, null);
        }

        if (isSuccessfulEndpointResult(endpointResult)) {
            User newUser = new User(endpointResult.getUserId(), endpointResult.getUsername());
            usersCache.cacheUser(newUser);
            return new UseCaseResult(Status.SUCCESS, newUser);
        }

        return new UseCaseResult(Status.FAILURE, null);
    }

    private boolean isSuccessfulEndpointResult(EndpointResult endpointResult) {
        return endpointResult.getStatus() == FetchUserHttpEndpointSync.EndpointStatus.SUCCESS;
    }
}
