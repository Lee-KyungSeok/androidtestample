package com.example.testsample.tdd.excercise;

import com.example.testsample.tdd.excercise.networking.GetReputationHttpEndpointSync;
import com.example.testsample.tdd.excercise.networking.GetReputationHttpEndpointSync.EndpointResult;

public class GetReputationUseCaseSync {

    GetReputationHttpEndpointSync getReputationHttpEndpointSync;

    public GetReputationUseCaseSync(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
        this.getReputationHttpEndpointSync = getReputationHttpEndpointSync;
    }

    public UseCaseResult getReputationSync() {
        EndpointResult endpointResult = getReputationHttpEndpointSync.getReputationSync();

        if (endpointResult.getStatus() != GetReputationHttpEndpointSync.EndpointStatus.SUCCESS) {
            return new UseCaseResult(Status.FAILURE, 0);
        }

        return new UseCaseResult(Status.SUCCESS, endpointResult.getReputation());
    }

    public enum Status {
        SUCCESS,
        FAILURE,
    }


    class UseCaseResult {

        private Status status;
        private int reputation;

        public UseCaseResult(Status status, int reputation) {
            this.status = status;
            this.reputation = reputation;
        }

        public Status getStatus() {
            return status;
        }

        public int getReputation() {
            return reputation;
        }
    }
}
