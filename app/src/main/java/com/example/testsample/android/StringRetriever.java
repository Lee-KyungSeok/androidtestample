package com.example.testsample.android;

import android.content.Context;

public class StringRetriever {

    private final Context context;

    public StringRetriever(Context context) {
        this.context = context;
    }

    public String getString(int id) {
        return context.getString(id);
    }
}
