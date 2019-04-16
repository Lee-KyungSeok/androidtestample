package com.example.testsample.tdd.example2;

import com.example.testsample.tdd.example2.cart.CartItem;
import com.example.testsample.tdd.example2.networking.CartItemSchema;
import com.example.testsample.tdd.example2.networking.GetCartItemsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

import static com.example.testsample.tdd.example2.networking.GetCartItemsHttpEndpoint.FailReason.GENERAL_ERROR;
import static com.example.testsample.tdd.example2.networking.GetCartItemsHttpEndpoint.FailReason.NETWORK_ERROR;

public class FetchCartItemsUseCase {

    public interface Listener {
        void onCartItemsFetched(List<CartItem> cartItemList);

        void onFetchCartItemsFailed();
    }

    private final List<Listener> mListener = new ArrayList<>();
    private final GetCartItemsHttpEndpoint getCartItemsHttpEndpoint;

    public FetchCartItemsUseCase(GetCartItemsHttpEndpoint getCartItemsHttpEndpoint) {
        this.getCartItemsHttpEndpoint = getCartItemsHttpEndpoint;
    }

    public void fetchCartItemsAndNotify(final int limit) {
        getCartItemsHttpEndpoint.getCartItems(limit, new GetCartItemsHttpEndpoint.Callback() {

            @Override
            public void onGetCartItemsSucceeded(List<CartItemSchema> cartItems) {
                for (Listener listener: mListener) {
                    listener.onCartItemsFetched(cartItemsFromSchemas(cartItems));
                }
            }

            @Override
            public void onGetCartItemsFailed(GetCartItemsHttpEndpoint.FailReason failReason) {
                if (failReason == GENERAL_ERROR) {
                    for(Listener listener: mListener) {
                        listener.onFetchCartItemsFailed();
                    }
                }

                if (failReason == NETWORK_ERROR) {
                    for(Listener listener: mListener) {
                        listener.onFetchCartItemsFailed();
                    }
                }
            }
        });
    }

    private List<CartItem> cartItemsFromSchemas(List<CartItemSchema> cartItemSchemas) {
        List<CartItem> cartItems = new ArrayList<>();
        for (CartItemSchema schema : cartItemSchemas) {
            cartItems.add(new CartItem(
                    schema.getId(),
                    schema.getTitle(),
                    schema.getDescription(),
                    schema.getPrice()
            ));
        }
        return cartItems;
    }

    public void registerListener(Listener listener) {
        mListener.add(listener);
    }

    public void unregisterListener(Listener listener) {
        mListener.remove(listener);
    }
}
