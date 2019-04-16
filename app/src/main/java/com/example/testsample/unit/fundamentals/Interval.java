package com.example.testsample.unit.fundamentals;

public class Interval {

    private final int mStart;
    private final int mEnd;

    public Interval(int start, int end) {
        if(start >= end) {
            throw new IllegalArgumentException("invalid interval range");
        }

        this.mStart = start;
        this.mEnd = end;
    }

    public int getmStart() {
        return mStart;
    }

    public int getmEnd() {
        return mEnd;
    }
}
