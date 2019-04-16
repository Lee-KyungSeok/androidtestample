package com.example.testsample.unit.fundamentals;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StringFuncTest {

    StringFunc SUT;

    @Before
    public void setUp() throws Exception {

        SUT = new StringFunc();
    }

    @Test
    public void reverse_emptyString_emptyStringReturned() {
        String result = SUT.reverse("");
        assertThat(result, is(""));
    }

    @Test
    public void reverse_singleCharacter_sameStringReturned() {
        String result = SUT.reverse("a");
        assertThat(result, is("a"));
    }

    @Test
    public void reverse_longString_reversedStringReturned() {
        String result = SUT.reverse("Lee Kyung Seok");
        assertThat(result, is("koeS gnuyK eeL"));
    }

    @Test
    public void duplicate_emptyString_emptyStringReturned() {
        String result = SUT.duplicate("");
        assertThat(result, is(""));
    }

    @Test
    public void duplicate_singleCharacter_duplicatedStringReturned() {
        String result = SUT.duplicate("a");
        assertThat(result, is("aa"));
    }

    @Test
    public void duplicate_longString_duplicatedStringReturned() {
        String result = SUT.duplicate("Kyung Seok");
        assertThat(result, is("Kyung SeokKyung Seok"));
    }
}