package com.example.testsample.android;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class MainActivityTest {

    MainActivity SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new MainActivity();
    }

    @Test
    public void onStart_incrementsCountByOne() {
        // Arrange

        // Act
        SUT.onStart();
        int result = SUT.getCount();

        // Assert
        assertThat(result, is(1));
    }
}