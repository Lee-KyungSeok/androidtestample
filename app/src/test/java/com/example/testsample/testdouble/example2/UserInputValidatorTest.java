package com.example.testsample.testdouble.example2;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class UserInputValidatorTest {

    UserInputValidator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new UserInputValidator();
    }

    @Test
    public void isValidFullName_validFullName_trueReturned() {
        boolean result = SUT.isValidFullName("validFullName");
        assertThat(result, is(true));
    }

    @Test
    public void isValidFullName_invalidFullName_falseReturned() {
        boolean result = SUT.isValidFullName("");
        assertThat(result, is(false));
    }

    @Test
    public void isValidUserName_validUserName_trueReturned() {
        boolean result = SUT.isValidUserName("validUserName");
        assertThat(result, is(true));
    }


    @Test
    public void isValidUserName_inValidUserName_falseReturned() {
        boolean result = SUT.isValidUserName("");
        assertThat(result, is(false));
    }

}