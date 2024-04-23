package com.example.homemadefood.ProviderPage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.LoginActivity;
import com.example.homemadefood.R;
import com.example.homemadefood.UserProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class ProviderOrderActivity extends AppCompatActivity {

    private TextView textView;
    private ImageButton imageLogo;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_provider_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.orderRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a list to hold the orders
        List<Order> orders = new ArrayList<>();

        // Populate the list with 5 orders (you can replace these with your actual order data)
        for (int i = 0; i < 5; i++) {
            orders.add(new Order("Order " + (i + 1), "Item " + (i + 1),"User" + (i + 1), "User" + (i+1) + "@example.com"));
        }

        // Assuming you have already populated the `orders` list
        OrdersAdapter adapter = new OrdersAdapter(orders);
        recyclerView.setAdapter(adapter);

        textView = findViewById(R.id.textView);
        imageLogo = findViewById(R.id.imageLogo);
        SearchView searchOrders = findViewById(R.id.searchOrders);

        //Get provider information from database or wherever its stored
        String providerName = "Provider Name";
        int logoResourceId = R.drawable.logo1;

        //Set provider name to TextView
        textView.setText(providerName);

        //Set provider logo to ImageButton
        imageLogo.setImageResource(logoResourceId);

        searchOrders.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //User pressed the search button on the keyboard
                //Handle search query here
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Filter the data as per newText
                return false;
            }
        });

        Button btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show the logout confirmation dialog
                new AlertDialog.Builder(ProviderOrderActivity.this)
                        .setTitle("Update Profile")
                        .setMessage("Are you sure you want to update your profile?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked "Yes", so navigate to the login page
                                Intent intent = new Intent(ProviderOrderActivity.this, UserProfileActivity.class);
                                startActivity(intent);
                                //Finish MainActivity
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)//Nothing happens if clicked
                        .show();
            }
        });

        //When the logout button is clicked this takes the user to the dialogue alert asking the user if they want to logout of their current account
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show the logout confirmation dialog
                new AlertDialog.Builder(ProviderOrderActivity.this)
                        .setTitle("Logout Confirmation!!!")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked "Yes", so navigate to the login page
                                Intent intent = new Intent(ProviderOrderActivity.this, LoginActivity.class);
                                startActivity(intent);
                                //Finish MainActivity
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)//Nothing happens if clicked
                        .show();
            }
        });

    }

}