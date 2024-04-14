package com.example.homemadefood.ProviderPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homemadefood.LoginActivity;
import com.example.homemadefood.R;

public class ProvidersHomePage extends AppCompatActivity {

    private ImageButton profileBtn;
    private Button addButton;
    private Button addMenButton;
    private TextView restaurantPlaceholder;
    private TextView menuPlaceholder;

    // Declare FirebaseFirestore variable
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home_page);

        profileBtn = findViewById(R.id.profileButton);
        addButton = findViewById(R.id.addRestaurantButton);
        addMenButton = findViewById(R.id.addMenuButton);

        // Initialize placeholders
        restaurantPlaceholder = findViewById(R.id.restaurantPlaceholder);
        menuPlaceholder = findViewById(R.id.menuPlaceholder);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Fetch user data from Firestore
        showPlaceholderViews();

        // Set onClickListener for the profile button
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ProvidersHomePage.this, profileBtn);
                popupMenu.getMenuInflater().inflate(R.menu.profile_pop_up_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.viewAccount) {
                            // Handle view account click
                            // Add your code here to navigate to the View Account page
                            return true;
                        } else if (menuItem.getItemId() == R.id.changePassword) {
                            // Handle change password click
                            // Add your code here to navigate to the Change Password page
                            return true;
                        } else if (menuItem.getItemId() == R.id.logOut) {
                            // Handle log out click
                            logout(); // Assuming this method is for logging out
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        // Set onClickListener for the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start the AddRestaurantInfoActivity
                Intent intent = new Intent(ProvidersHomePage.this, AddRestaurantInfoActivity.class);

                // Start the activity
                startActivity(intent);
            }
        });

    }



    private void showPlaceholderViews() {
        // Show the placeholder views
        Log.d("ProvidersHomePage", "Showing placeholder views");
        restaurantPlaceholder.setVisibility(View.VISIBLE);
        menuPlaceholder.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
        addMenButton.setVisibility(View.VISIBLE);
    }


    // Method for logging out
    private void logout() {
        Intent intent = new Intent(ProvidersHomePage.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}