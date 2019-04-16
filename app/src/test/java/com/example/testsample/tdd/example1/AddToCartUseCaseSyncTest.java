package com.example.testsample.tdd.example1;

import com.example.testsample.tdd.example1.network.AddToCartHttpEndpointSync;
import com.example.testsample.tdd.example1.network.AddToCartHttpEndpointSync.EndpointResult;
import com.example.testsample.tdd.example1.network.CartItemScheme;
import com.example.testsample.tdd.example1.network.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.example.testsample.tdd.example1.AddToCartUseCaseSync.UseCaseResult;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddToCartUseCaseSyncTest {

    public static final String OFFER_ID = "offerId";
    public static final int AMOUNT = 4;

    @Mock
    AddToCartHttpEndpointSync addToCartHttpEndpointSyncMock;

    AddToCartUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new AddToCartUseCaseSync(addToCartHttpEndpointSyncMock);
        success();
    }

    // correct parameters passed to the endpoint

    @Test
    public void addToCartSync_correctParametersPassedToEndpoint() throws Exception {
        // Arrange
        ArgumentCaptor<CartItemScheme> ac = ArgumentCaptor.forClass(CartItemScheme.class);

        // Act
        SUT.addToCartSync(OFFER_ID, AMOUNT);

        // Assert
        verify(addToCartHttpEndpointSyncMock).addToCartSync(ac.capture());
        assertThat(ac.getValue().getOfferId(), is(OFFER_ID));
        assertThat(ac.getValue().getAmount(), is(AMOUNT));
    }

    // endpoint sucess - success returned

    @Test
    public void addToCartSync_success_successReturned() throws Exception {
        // Arrange

        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);

        // Assert
        assertThat(result, is(UseCaseResult.SUCCESS));
    }

    // endpoint auth error - failure returned

    @Test
    public void addToCartSync_authError_failureReturned() throws Exception {
        // Arrange
        authError();

        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);

        // Assert
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    // endpoint general error - failure returned
    @Test
    public void addToCartSync_generalError_failureReturned() throws Exception {
        // Arrange
        generalError();

        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);

        // Assert
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    // network exception - network error returned


    @Test
    public void addToCartSync_networkError_networkErrorReturned() throws Exception {
        // Arrange
        networkError();

        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);

        // Assert
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }

    private void success() throws NetworkErrorException {
        when(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(EndpointResult.SUCCESS);
    }

    private void authError() throws Exception {
        when(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(EndpointResult.AUTH_ERROR);
    }

    private void generalError() throws Exception {
        when(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(EndpointResult.GENERAL_ERROR);
    }

    private void networkError() throws Exception {
        doThrow(NetworkErrorException.class)
                .when(addToCartHttpEndpointSyncMock)
                .addToCartSync(any(CartItemScheme.class));
    }

}