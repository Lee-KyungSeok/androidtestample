package com.example.testsample.tdd.excercise;

import com.example.testsample.tdd.excercise.GetReputationUseCaseSync.UseCaseResult;
import com.example.testsample.tdd.excercise.networking.GetReputationHttpEndpointSync;
import com.example.testsample.tdd.excercise.networking.GetReputationHttpEndpointSync.EndpointResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetReputationUseCaseSyncTest {

    @Mock
    GetReputationHttpEndpointSync getReputationHttpEndpointSyncMock;

    GetReputationUseCaseSync SUT;

    private static final int REPUTATION = 100;

    @Before
    public void setUp() throws Exception {
        SUT = new GetReputationUseCaseSync(getReputationHttpEndpointSyncMock);
    }

    @Test
    public void getReputationSync_success_successStatusReturned() {
        // Arrange
        success();

        // Act
        UseCaseResult result = SUT.getReputationSync();

        // Assert
        assertThat(result.getStatus(), is(GetReputationUseCaseSync.Status.SUCCESS));
    }

    @Test
    public void getReputationSync_success_reputationReturned() {
        // Arrange
        success();

        // Act
        UseCaseResult result = SUT.getReputationSync();

        // Assert
        assertThat(result.getReputation(), is(REPUTATION));
    }

    @Test
    public void getReputationSync_generalError_failureStatusReturned() {
        // Arrange
        generalError();

        // Act
        UseCaseResult result = SUT.getReputationSync();

        // Assert
        assertThat(result.getStatus(), is(GetReputationUseCaseSync.Status.FAILURE));
    }

    @Test
    public void getReputationSync_networkError_failureStatusReturned() {
        // Arrange
        networkError();

        // Act
        UseCaseResult result = SUT.getReputationSync();

        // Assert
        assertThat(result.getStatus(), is(GetReputationUseCaseSync.Status.FAILURE));
    }

    @Test
    public void getReputationSync_generalError_ZeroReputationReturned() {
        // Arrange
        generalError();

        // Act
        UseCaseResult result = SUT.getReputationSync();

        // Assert
        assertThat(result.getReputation(), is(0));
    }


    @Test
    public void getReputationSync_networkError_ZeroReputationReturned() {
        // Arrange
        networkError();

        // Act
        UseCaseResult result = SUT.getReputationSync();

        // Assert
        assertThat(result.getReputation(), is(0));
    }

    private void success() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.SUCCESS, REPUTATION));
    }

    private void generalError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR, 0));
    }

    private void networkError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR, 0));
    }
}