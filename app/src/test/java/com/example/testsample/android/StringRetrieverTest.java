package com.example.testsample.android;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StringRetrieverTest {

    public static final int ID = 10;
    public static final String STRING = "string";

    @Mock
    Context contextMock;

    StringRetriever SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringRetriever(contextMock);
    }

    @Test
    public void getString_correctParameterToContext() {
        // Arrange

        // Act
        SUT.getString(ID);

        // Assert
        verify(contextMock).getString(ID);
    }

    @Test
    public void getString_correctResultReturned() {
        // Arrange
        when(contextMock.getString(ID))
                .thenReturn(STRING);

        // Act
        String result = SUT.getString(ID);

        // Assert
        assertThat(result, is(STRING));
    }
}