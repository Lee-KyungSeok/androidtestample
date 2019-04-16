package com.example.testsample.android;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class AndroidUnitTestingProblemsTest {

    AndroidUnitTestingProblems SUT;

    @Before
    public void setup() throws Exception {
        SUT = new AndroidUnitTestingProblems();
    }

    @Test
    public void testStaticApiCall() {
        SUT.callStaticAndroidApi("");
        assertThat(true, is(true));
    }
}