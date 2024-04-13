package com.example.homemadefood.CustomerPage.CustomerViewRestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.homemadefood.CustomerPage.MainPage.CustomerHomepage;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantData;
import com.example.homemadefood.R;

public class CustomerMenuSelection extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu_selection);

        ImageButton closeButton = findViewById(R.id.closeButton);

        ImageView restaurantImage = findViewById(R.id.restaurantImage);
        TextView restaurantNameTextView = findViewById(R.id.restaurantName);
        TextView ratingTextView = findViewById(R.id.ratingTextView);
        TextView totalRatingTextView = findViewById(R.id.totalRatingTextView);

        RestaurantData restaurantData = getIntent().getParcelableExtra("restaurant_data");

        if (restaurantData != null) {
            Glide.with(this).load(restaurantData.getRestaurantImageUri()).into(restaurantImage);
            restaurantNameTextView.setText(restaurantData.getName());
            ratingTextView.setText(String.valueOf(restaurantData.getRating()));
            totalRatingTextView.setText(String.valueOf(restaurantData.getTotalRating()));
        } else {
            Toast.makeText(CustomerMenuSelection.this, "No Data", Toast.LENGTH_SHORT).show();
        }


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerMenuSelection.this, CustomerHomepage.class);
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
                finish(); // finish the current activity to apply animation
                startActivity(intent);
            }
        });
    }
}
