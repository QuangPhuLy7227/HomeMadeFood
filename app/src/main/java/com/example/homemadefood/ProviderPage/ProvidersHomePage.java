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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homemadefood.LoginActivity;
import com.example.homemadefood.ProviderPage.ProRecyclerViewData.MenuData;
import com.example.homemadefood.ProviderPage.ProRecyclerViewData.RestaurantData;
import com.example.homemadefood.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ProvidersHomePage extends AppCompatActivity {

    private ImageButton profileBtn;
    private Button addButton;
    private Button addMenButton;
    private FirestoreManager firestoreManager;
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

        // Initialize FirestoreManager
        firestoreManager = new FirestoreManager();

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Fetch user data from Firestore
//        fetchUserData();
        showPlaceholderViews();

        // Call the method to display restaurant data on the home page
        displayRestaurantDataOnHomePage();

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

    // After successfully adding the restaurant data, retrieve and display it on the home page
    private void displayRestaurantDataOnHomePage() {
        // Retrieve restaurant data from Firestore
        firestore.collection("restaurants")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Check if there are any documents
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Iterate through each document
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Get the restaurant details from the document
                            String name = documentSnapshot.getString("name");
                            String info = documentSnapshot.getString("info");
                            String address = documentSnapshot.getString("address");
                            String zipCode = documentSnapshot.getString("zipCode");
                            String phoneNumber = documentSnapshot.getString("phoneNumber");
                            String openHours = documentSnapshot.getString("openHours");
                            String closeHours = documentSnapshot.getString("closeHours");
                            String category = documentSnapshot.getString("category");
                            String date = documentSnapshot.getString("date");
                            String imageURL = documentSnapshot.getString("restaurantImageUri");

                            // Update the UI with the retrieved data
                            // For example:
                            // restaurantNameTextView.setText(name);
                            // restaurantInfoTextView.setText(info);
                            // ...

                            // Note: You need to add appropriate views in your home page layout to display the restaurant data.
                            // Below is an example assuming you have TextViews in your layout with ids restaurantNameTextView, restaurantInfoTextView, etc.
                            // Update these with your actual view ids.

                            TextView restaurantNameTextView = findViewById(R.id.restaurantNameTextView);
                            TextView restaurantInfoTextView = findViewById(R.id.restaurantInfoTextView);
                            TextView restaurantAddressTextView = findViewById(R.id.restaurantAddressTextView);
                            // Set retrieved data to TextViews
                            restaurantNameTextView.setText(name);
                            restaurantInfoTextView.setText(info);
                            restaurantAddressTextView.setText(address);
                        }
                    } else {
                        // No restaurant data available, show a message or handle the scenario accordingly
                        // For example:
                        // restaurantNameTextView.setText("No restaurant data available");
                    }
                })
                .addOnFailureListener(e -> {
                    // Failed to retrieve restaurant data
                    Toast.makeText(this, "Failed to retrieve restaurant data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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