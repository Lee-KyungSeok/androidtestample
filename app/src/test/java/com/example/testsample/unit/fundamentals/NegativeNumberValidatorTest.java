package com.example.testsample.unit.fundamentals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class NegativeNumberValidatorTest {

    NegativeNumberValidator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void checkNegativeNumber() {
        boolean result = SUT.isNegative(-1);
        Assert.assertThat(result, is(true));
    }

    @Test
    public void checkPositiveNumber() {
        boolean result = SUT.isNegative(1);
        Assert.assertThat(result, is(false));
    }

    @Test
    public void checkZero() {
        boolean result = SUT.isNegative(0);
        Assert.assertThat(result, is(false));
    }
}