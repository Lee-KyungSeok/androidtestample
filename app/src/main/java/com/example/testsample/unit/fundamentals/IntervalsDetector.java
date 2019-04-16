package com.example.testsample.unit.fundamentals;

public class IntervalsDetector {

    public boolean isOverlap(Interval interval1, Interval interval2) {

        return  interval1.getmStart() < interval2.getmEnd() && interval2.getmStart() < interval1.getmEnd();

    }

    public boolean isAdjacent(Interval interval1, Interval interval2) {
        if(isSameIntervals(interval1, interval2)) {
            return false;
        }
        return interval1.getmEnd() == interval2.getmStart() || interval1.getmStart() == interval2.getmEnd();
    }

    private boolean isSameIntervals(Interval interval1, Interval interval2) {
        return interval1.getmStart() == interval2.getmStart() && interval1.getmEnd() == interval2.getmEnd();
    }
}
