package com.example.homemadefood.CustomerPage.RestaurantPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homemadefood.CustomerPage.MainPage.CustomerHomepage;
import com.example.homemadefood.R;

public class RestaurantMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_menu);

        ImageView restaurantImage = findViewById(R.id.restaurantImage);
        ImageButton closeButton = findViewById(R.id.closeButton);
        TextView restaurantName = findViewById(R.id.restaurantName);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu();

            }
        });
    }

    private void closeMenu() {
        Intent intent = new Intent(this, CustomerHomepage.class);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        finish(); // finish the current activity to apply animation
        startActivity(intent);
    }

//    private void searchMenu() {
//
//    }

//    private void cardPage() {
//        Intent intent = new Intent(this, CustomerCardPage.class);
//        startActivity(intent);
//    }
}
