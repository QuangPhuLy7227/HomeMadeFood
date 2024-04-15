package com.example.homemadefood.ProviderPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.LoginActivity;
import com.example.homemadefood.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private ImageView imageViewRestaurant;
    private RecyclerView recyclerView;

    // Firebase Firestore instance
    private FirebaseFirestore firestore;

    // Firebase Auth instance
    private FirebaseAuth mAuth;
    private ImageButton profileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_retrieve_home);

        // Initialize views
//        imageViewRestaurant = findViewById(R.id.imageViewRestaurant);
        recyclerView = findViewById(R.id.recyclerView);

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance();
        profileBtn = findViewById(R.id.profileButton);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Get the current user from Firebase Authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Set onClickListener for the profile button
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(RestaurantDetailsActivity.this, profileBtn);
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
        // Check if the user is not null before accessing its properties
        if (user != null) {
            // Get the username of the current user
            String addedBy = user.getDisplayName(); // Assuming the username is stored in the display name field
            // Proceed with your code here

            // Query Firestore for the restaurant info with the specified addedBy username
            firestore.collection("restaurants")
                    .whereEqualTo("addedBy", addedBy)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Retrieve restaurant data from the document
                                String imageURL = document.getString("restaurantImageUri");

                                // Load restaurant image using Glide library
                                Glide.with(this)
                                        .load(imageURL)
                                        .placeholder(R.drawable.default_restaurant_image)
                                        .into(imageViewRestaurant);
                            }
                        } else {
                            // Handle errors
                            Log.e("RestaurantDetailsActivity", "Error getting documents.", task.getException());
                        }
                    });
        } else {
            // Handle the case when the user is null
            Log.e("RestaurantDetailsActivity", "User is null. This action requires an authenticated user.");
        }
    }
    private void logout() {
        Intent intent = new Intent(RestaurantDetailsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
