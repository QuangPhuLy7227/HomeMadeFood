package com.example.homemadefood.CustomerPage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.homemadefood.CustomerPage.MainPage.CustomerHomepage;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantData;
import com.example.homemadefood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DemoAddRestaurants extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private List<RestaurantData> dataList;
    private ImageView addRestaurantImage;
    private Uri selectedImageUri; // To store the URI of the selected image
    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_add_restaurants);

        addRestaurantImage = findViewById(R.id.uploadRestaurantImage);
        Button selectRestaurantImage = findViewById(R.id.selectRestaurantImage);

        mDatabase = FirebaseDatabase.getInstance().getReference();  // Initialize Firebase database reference
        dataList = new ArrayList<>();

        // Initialize the ActivityResultLauncher for image picking
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                // Load and resize the image using Glide
                Glide.with(this)
                        .load(selectedImageUri)
                        .override(600, 600) // Resize the image to 600x600 pixels
                        .into(addRestaurantImage); // Display the image in ImageView
            }
        });

        selectRestaurantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        Button saveRestaurantInfo = findViewById(R.id.saveRestaurantInfo);
        Button visitHomepage = findViewById(R.id.backButton);

        saveRestaurantInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.addName)).getText().toString().trim();
                Spinner foodCategorySpinner = findViewById(R.id.foodCategorySpinner);
                String category = foodCategorySpinner.getSelectedItem().toString().trim();
                String rating = ((EditText) findViewById(R.id.addRating)).getText().toString().trim();
                String totalRating = ((EditText) findViewById(R.id.addTotalRating)).getText().toString().trim();
                String deliveryFee = ((EditText) findViewById(R.id.addDeliveryFee)).getText().toString().trim();

                if (name.isEmpty() || rating.isEmpty() || totalRating.isEmpty() || deliveryFee.isEmpty() || selectedImageUri == null) {
                    Toast.makeText(DemoAddRestaurants.this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                float ratingValue;
                float totalRatingValue;
                float deliveryFeeValue;
                try {
                    ratingValue = Float.parseFloat(rating);
                    totalRatingValue = Float.parseFloat(totalRating);
                    deliveryFeeValue = Float.parseFloat(deliveryFee);
                } catch (NumberFormatException e) {
                    Toast.makeText(DemoAddRestaurants.this, "Invalid rating or delivery fee format", Toast.LENGTH_SHORT).show();
                    return;
                }


                RestaurantData restaurantData = new RestaurantData(selectedImageUri.toString(), name, category, deliveryFeeValue, ratingValue, (int) totalRatingValue);

                uploadRestaurantToFirebase(restaurantData);
            }
        });

        visitHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoAddRestaurants.this, CustomerHomepage.class);
                startActivity(intent);
            }
        });
    }

    private void openImagePicker() {
        imagePickerLauncher.launch("image/*");
    }

    private void uploadRestaurantToFirebase(RestaurantData restaurantData) {
        DatabaseReference restaurantsRef = mDatabase.child("restaurants"); // Reference to the root database

        String restaurantName = restaurantData.getName(); // Get the restaurant name

        // Check if the restaurant name already exists
        restaurantsRef.child(restaurantName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(DemoAddRestaurants.this, "Restaurant with this name already exists", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference restaurantRef = restaurantsRef.child(restaurantName); // Use restaurant name as the key
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("restaurant_images").child(restaurantName); // Reference to the Firebase Storage

                    // Upload image to Firebase Storage
                    storageRef.putFile(selectedImageUri)
                            .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> { // Get the download URL
                                restaurantData.setRestaurantImageUri(uri.toString());  // Save download URL along with other restaurant details in the database
                                restaurantRef.setValue(restaurantData)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(DemoAddRestaurants.this, "Restaurant added successfully", Toast.LENGTH_SHORT).show();
                                            dataList.add(restaurantData);
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(DemoAddRestaurants.this, "Failed to add restaurant", Toast.LENGTH_SHORT).show());
                            }).addOnFailureListener(e -> {
                                Toast.makeText(DemoAddRestaurants.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                            }))
                            .addOnFailureListener(e -> {
                                Toast.makeText(DemoAddRestaurants.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DemoAddRestaurants.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }



}