package com.example.testsample.tdd.example2;

import com.example.testsample.tdd.example2.cart.CartItem;
import com.example.testsample.tdd.example2.networking.CartItemSchema;
import com.example.testsample.tdd.example2.networking.GetCartItemsHttpEndpoint;
import com.example.testsample.tdd.example2.networking.GetCartItemsHttpEndpoint.Callback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static com.example.testsample.tdd.example2.networking.GetCartItemsHttpEndpoint.FailReason.GENERAL_ERROR;
import static com.example.testsample.tdd.example2.networking.GetCartItemsHttpEndpoint.FailReason.NETWORK_ERROR;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class FetchCartItemsUseCaseTest {

    public static final int LIMIT = 10;
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final int PRICE = 5;

    @Mock
    GetCartItemsHttpEndpoint getCartItemsHttpEndpointMock;

    @Mock
    FetchCartItemsUseCase.Listener mListenerMock1;

    @Mock
    FetchCartItemsUseCase.Listener mListenerMock2;

    @Captor
    ArgumentCaptor<List<CartItem>> mAcListCartItem;

    FetchCartItemsUseCase SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new FetchCartItemsUseCase(getCartItemsHttpEndpointMock);
    }

    @Test
    public void fetchCartItem_correctLimitPassedToEndpoint() {
        // Arrange
        ArgumentCaptor<Integer> acInt = ArgumentCaptor.forClass(Integer.class);

        // Act
        SUT.fetchCartItemsAndNotify(LIMIT);

        // Assert
        verify(getCartItemsHttpEndpointMock)
                .getCartItems(acInt.capture(), any(Callback.class));
        assertThat(acInt.getValue(), is(LIMIT));

    }

    @Test
    public void fetchCartItems_success_observersNotifiedWithCorrectData() {
        // Arrange
        success();

        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);

        // Assert
        verify(mListenerMock1).onCartItemsFetched(mAcListCartItem.capture());
        verify(mListenerMock2).onCartItemsFetched(mAcListCartItem.capture());
        List<List<CartItem>> captures = mAcListCartItem.getAllValues();
        List<CartItem> capture1 = captures.get(0);
        List<CartItem> capture2 = captures.get(1);

        assertThat(capture1, is(getCartItems()));
        assertThat(capture2, is(getCartItems()));
    }

    @Test
    public void fetchCartItems_success_unsubscribedObserversNotNotified() {
        // Arrange
        success();

        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.unregisterListener(mListenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);

        // Assert
        verify(mListenerMock1).onCartItemsFetched(any(List.class));
        verifyNoMoreInteractions(mListenerMock2);
    }

    @Test
    public void fetchCartItems_generalError_observersNotifiedOfFailure() {
        // Arrange
        generalError();

        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);

        // Assert
        verify(mListenerMock1).onFetchCartItemsFailed();
        verify(mListenerMock2).onFetchCartItemsFailed();
    }

    @Test
    public void fetchCartItems_networkError_observersNotifiedOfFailure() {
        // Arrange
        networkError();

        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);

        // Assert
        verify(mListenerMock1).onFetchCartItemsFailed();
        verify(mListenerMock2).onFetchCartItemsFailed();
    }

    private void success() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetCartItemsSucceeded(getCartItemSchemes());
                return null;
            }
        }).when(getCartItemsHttpEndpointMock)
                .getCartItems(anyInt(), any(Callback.class));
    }

    private void generalError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetCartItemsFailed(GENERAL_ERROR);
                return null;
            }
        }).when(getCartItemsHttpEndpointMock)
                .getCartItems(anyInt(), any(Callback.class));
    }

    private void networkError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetCartItemsFailed(NETWORK_ERROR);
                return null;
            }
        }).when(getCartItemsHttpEndpointMock)
                .getCartItems(anyInt(), any(Callback.class));
    }

    private List<CartItemSchema> getCartItemSchemes() {
        List<CartItemSchema> schemas = new ArrayList<>();
        schemas.add(new CartItemSchema(ID, TITLE, DESCRIPTION, PRICE));
        return schemas;
    }

    private List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(ID, TITLE, DESCRIPTION, PRICE));
        return cartItems;
    }
}