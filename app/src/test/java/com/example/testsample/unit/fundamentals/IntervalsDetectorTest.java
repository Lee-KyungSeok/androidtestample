package com.example.testsample.unit.fundamentals;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class IntervalsDetectorTest {

    IntervalsDetector SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new IntervalsDetector();
    }

    // interval1 is before interval2
    @Test
    public void isOverlap_interval1BeforeInterval2_falseReturned() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(8, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(false));
    }

    // interval1 overlaps interval2 on start
    @Test
    public void isOverlap_interval1OverlapsInterval2OnStart_trueReturned() {
        Interval interval1 = new Interval(6, 18);
        Interval interval2 = new Interval(8, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(true));
    }

    // interval1 is contained within interval2
    @Test
    public void isOverlap_interval1ContainedInterval2_trueReturned() {
        Interval interval1 = new Interval(4, 5);
        Interval interval2 = new Interval(3, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(true));
    }

    // interval1 contains interval2
    @Test
    public void isOverlap_interval1ContainsInterval2_trueReturned() {
        Interval interval1 = new Interval(-1, 15);
        Interval interval2 = new Interval(8, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(true));
    }

    // interval1 overlaps interval2 on end
    @Test
    public void isOverlap_interval1OverlapsInterval2OnEnd_trueReturned() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(3, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(true));
    }

    // interval1 is after interval2
    @Test
    public void isOverlap_interval1AfterInterval2_falseReturned() {
        Interval interval1 = new Interval(15, 20);
        Interval interval2 = new Interval(8, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(false));
    }

    // interval1 is before adjacent interval2
    @Test
    public void isOverlap_interval1BeforeAdjacentInterval2_falseReturned() {
        Interval interval1 = new Interval(12, 20);
        Interval interval2 = new Interval(8, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(false));
    }

    // interval1 is after adjacent interval2
    @Test
    public void isOverlap_interval1AfterAdjacentInterval2_falseReturned() {
        Interval interval1 = new Interval(1, 8);
        Interval interval2 = new Interval(8, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_interval1BeforeAdjacentInterval2_trueReturned() {
        Interval interval1 = new Interval(-1, 8);
        Interval interval2 = new Interval(8, 16);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(true));
    }

    @Test
    public void isAdjacent_interval1OverlapsInterval2OnStart_falseReturned() {
        Interval interval1 = new Interval(-1, 11);
        Interval interval2 = new Interval(10, 16);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_interval1EqualsInterval2_falseReturned() {
        Interval interval1 = new Interval(10, 16);
        Interval interval2 = new Interval(10, 16);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_interval1OverlapsInterval2OnEnd_falseReturned() {
        Interval interval1 = new Interval(15, 20);
        Interval interval2 = new Interval(10, 16);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_interval1AfterAdjacentInterval2_trueReturned() {
        Interval interval1 = new Interval(16, 20);
        Interval interval2 = new Interval(10, 16);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(true));
    }

    @Test
    public void isAdjacent_interval1AfterInterval2_falseReturned() {
        Interval interval1 = new Interval(20, 25);
        Interval interval2 = new Interval(10, 16);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }


}