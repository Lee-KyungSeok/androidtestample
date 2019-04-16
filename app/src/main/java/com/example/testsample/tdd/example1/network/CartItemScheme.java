package com.example.testsample.tdd.example1.network;

public class CartItemScheme {

    private final String mOfferId;
    private final int mAmount;

    public CartItemScheme(String offedId, int amount) {
        mOfferId = offedId;
        mAmount = amount;
    }

    public String getOfferId() {
        return mOfferId;
    }

    public int getAmount() {
        return mAmount;
    }
}