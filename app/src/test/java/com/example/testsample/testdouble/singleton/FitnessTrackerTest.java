package com.example.testsample.testdouble.singleton;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

// singleton 인 경우 테스트에 서로 영향을 주므로 테스트 코드를 만들 때 조심해야 한다.
public class FitnessTrackerTest {

    FitnessTracker SUT;

    @Before
    public void setUp() throws Exception {
        SUT =  new FitnessTracker();
    }

    @Test
    public void step_totalIncremented() throws Exception {
        SUT.step();
        assertThat(SUT.getTotalSteps(), is(1));
    }

    @Test
    public void runStep_totalIncrementedByCorrectRatio() throws Exception {
        SUT.runStep();
        assertThat(SUT.getTotalSteps(), is(2));
    }
}