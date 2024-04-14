package com.example.homemadefood.ProviderPage;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homemadefood.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private ImageView imageViewRestaurant;
    private TextView textViewRestaurantName;
    private TextView textViewRestaurantInfo;
    private TextView textViewRestaurantAddress;
    private TextView textViewRestaurantPhoneNumber;
    private TextView textViewRestaurantOpenCloseHours;
    private TextView textViewRestaurantCategory;
    private TextView textViewRestaurantDate;

    // Firebase Firestore instance
    private FirebaseFirestore firestore;

    // Firebase Auth instance
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_retrieve_home);

        // Initialize views
        imageViewRestaurant = findViewById(R.id.imageViewRestaurant);
        textViewRestaurantName = findViewById(R.id.textViewRestaurantName);
        textViewRestaurantInfo = findViewById(R.id.textViewRestaurantInfo);
        textViewRestaurantAddress = findViewById(R.id.textViewRestaurantAddress);
        textViewRestaurantPhoneNumber = findViewById(R.id.textViewRestaurantPhone);
        textViewRestaurantOpenCloseHours = findViewById(R.id.textViewRestaurantHours);
        textViewRestaurantDate = findViewById(R.id.textViewRestaurantDate);

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
// Get the current user from Firebase Authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
                                String name = document.getString("name");
                                String info = document.getString("info");
                                String address = document.getString("address");
                                String zipCode = document.getString("zipCode");
                                String phoneNumber = document.getString("phoneNumber");
                                String openHours = document.getString("openHours");
                                String closeHours = document.getString("closeHours");
                                String category = document.getString("category");
                                String date = document.getString("date");
                                String imageURL = document.getString("restaurantImageUri");

                                // Update the UI with the retrieved data
                                textViewRestaurantName.setText(name);
                                textViewRestaurantInfo.setText(info);
                                textViewRestaurantAddress.setText(address);
                                textViewRestaurantPhoneNumber.setText(phoneNumber);
                                textViewRestaurantOpenCloseHours.setText(openHours + " - " + closeHours);
                                textViewRestaurantCategory.setText(category);
                                textViewRestaurantDate.setText(date);

                                // Load restaurant image using Glide library
                                Glide.with(this)
                                        .load(imageURL)
                                        .placeholder(R.drawable.default_restaurant_image)
                                        .into(imageViewRestaurant);
                            }
                        } else {
                            // Handle errors
                        }
                    });
        } else {
            // Handle the case when the user is null
            Log.e("RestaurantDetailsActivity", "User is null");
        }
    }
}
