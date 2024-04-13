package com.example.homemadefood.ProviderPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.homemadefood.CustomerPage.RestaurantPage.DemoAddRestaurantMenu;
import com.example.homemadefood.CustomerPage.RestaurantPage.DemoAddRestaurants;
import com.example.homemadefood.R;
import com.example.homemadefood.UserProfileActivity;

public class ProviderHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home_page);

        ImageButton idProfileButton = findViewById(R.id.profileButton);
        ImageButton addRestaurantButton = findViewById(R.id.addRestaurantButton);
        ImageButton addMenuItemButton = findViewById(R.id.addMenuItemButton);
        idProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderHomePage.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        addRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderHomePage.this, DemoAddRestaurants.class);
                startActivity(intent);
            }
        });

        addMenuItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderHomePage.this, DemoAddRestaurantMenu.class);
                startActivity(intent);
            }
        });
    }
}