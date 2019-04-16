package com.example.testsample.tdd.example1;

import com.example.testsample.tdd.example1.network.AddToCartHttpEndpointSync;
import com.example.testsample.tdd.example1.network.CartItemScheme;
import com.example.testsample.tdd.example1.network.NetworkErrorException;

public class AddToCartUseCaseSync {

    private AddToCartHttpEndpointSync addToCartHttpEndpointSync;

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    public AddToCartUseCaseSync(AddToCartHttpEndpointSync addToCartHttpEndpointSync) {
        this.addToCartHttpEndpointSync = addToCartHttpEndpointSync;
    }

    public UseCaseResult addToCartSync(String offerId, int amount) {
        AddToCartHttpEndpointSync.EndpointResult result;

        try {
            result = addToCartHttpEndpointSync
                    .addToCartSync(new CartItemScheme(offerId, amount));
        } catch (NetworkErrorException e) {
            return UseCaseResult.NETWORK_ERROR;
        }

        switch (result) {
            case SUCCESS:
                return UseCaseResult.SUCCESS;
            case AUTH_ERROR:
                return UseCaseResult.FAILURE;
            case GENERAL_ERROR:
                return UseCaseResult.FAILURE;
            default:
                throw new RuntimeException("Invalid status" + result);
        }
    }
}
