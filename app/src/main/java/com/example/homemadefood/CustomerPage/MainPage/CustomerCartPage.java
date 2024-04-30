package com.example.homemadefood.CustomerPage.MainPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import com.example.homemadefood.CustomerPage.RecyclerViewData.Adapter.CartItemAdapter;
import com.example.homemadefood.R;

import java.util.ArrayList;

public class CustomerCartPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartItemAdapter adapter;
    private ArrayList<Object> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_cart_page);

        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = new ArrayList<>();


        // Create and set adapter
        adapter = new CartItemAdapter(cartItems);
        recyclerView.setAdapter(adapter);
    }
}
