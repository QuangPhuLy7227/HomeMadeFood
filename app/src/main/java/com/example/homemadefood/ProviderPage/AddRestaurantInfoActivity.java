package com.example.homemadefood.ProviderPage;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.homemadefood.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.HashMap;
import java.util.Map;

public class AddRestaurantInfoActivity extends AppCompatActivity {

    private EditText resNameEditText, resInfoEditText, resAddressEditText, zipCodeEditText,
            phoneNumberEditText, openHoursEditText, closeHoursEditText;

    private Spinner selectCategorySpinner, dateRangeSpinner;
    private Button addButton, cancelButton;

    private FirebaseFirestore firestore;
    private ImageView addRestaurantImage;
    private Uri selectedImageUri;
    private Button selectRestaurantImage;
    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant_info);

        addRestaurantImage = findViewById(R.id.uploadRestaurantImage);
        selectRestaurantImage = findViewById(R.id.selectRestaurantImage1);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                Glide.with(this)
                        .load(selectedImageUri)
                        .override(600, 600)
                        .into(addRestaurantImage);
            }
        });

        selectRestaurantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        resNameEditText = findViewById(R.id.resName);
        resInfoEditText = findViewById(R.id.resInfo);
        resAddressEditText = findViewById(R.id.resAddress);
        zipCodeEditText = findViewById(R.id.zipCode);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        openHoursEditText = findViewById(R.id.openHours);
        closeHoursEditText = findViewById(R.id.closeHours);
        dateRangeSpinner = findViewById(R.id.dateRangeSpinner);
        selectCategorySpinner = findViewById(R.id.selectCategorySpinner);
        addButton = findViewById(R.id.save);
        cancelButton = findViewById(R.id.cancel);

        // Set onClickListener for the Add button
        addButton.setOnClickListener(v -> saveRestaurantToFirestore());

        // Set onClickListener for the Cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to provider's homepage
                onBackPressed();
            }
        });
    }

    private void openImagePicker() {
        imagePickerLauncher.launch("image/*");
    }

    // Save the restaurant data to Firestore
    private void saveRestaurantToFirestore() {
        // Get input values from UI elements
        String name = resNameEditText.getText().toString().trim();
        String info = resInfoEditText.getText().toString().trim();
        String address = resAddressEditText.getText().toString().trim();
        String zipCode = zipCodeEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String openHours = openHoursEditText.getText().toString().trim();
        String closeHours = closeHoursEditText.getText().toString().trim();
        String category = selectCategorySpinner.getSelectedItem().toString().trim();
        String date = dateRangeSpinner.getSelectedItem().toString().trim();

        // Check if all fields are filled
        if (name.isEmpty() || info.isEmpty() || address.isEmpty() || zipCode.isEmpty() ||
                phoneNumber.isEmpty() || openHours.isEmpty() || closeHours.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if an image is selected
        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select a restaurant image", Toast.LENGTH_SHORT).show();
            return;
        }


        // Check if the restaurant name already exists
        // Implement your logic to check if the restaurant name already exists in Firestore
        // You may query the Firestore collection to check if a document with the same name exists

        // Construct the restaurant data
        Map<String, Object> restaurantData = new HashMap<>();
        restaurantData.put("name", name);
        restaurantData.put("info", info);
        restaurantData.put("address", address);
        restaurantData.put("zipCode", zipCode);
        restaurantData.put("phoneNumber", phoneNumber);
        restaurantData.put("openHours", openHours);
        restaurantData.put("closeHours", closeHours);
        restaurantData.put("category", category);
        restaurantData.put("date", date);

        // Add the user ID who added the restaurant
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            restaurantData.put("addedBy", userId);
            Log.d("Authentication", "User ID: " + userId);
        } else {
            // Handle case where user is not logged in
            Log.d("Authentication", "User not logged in");
            Toast.makeText(this, "User not logged in. Please log in to add a restaurant.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload the image to Firestore storage
        uploadImageToStorage(name, restaurantData);
    }

    // Upload the restaurant image to Firestore storage
    private void uploadImageToStorage(String restaurantName, Map<String, Object> restaurantData) {
        // Create a reference to "restaurant_images" folder in Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("restaurant_images");

        // Create a reference to the image file with the name of the restaurant
        StorageReference imageRef = storageRef.child(restaurantName);

        // Upload the image to Firebase Storage
        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the download URL to Firestore
                        saveRestaurantDetails(restaurantName, restaurantData, uri.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    // Failed to upload image
                    Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Save restaurant data to Firestore
    private void saveRestaurantDetails(String restaurantName, Map<String, Object> restaurantData, String imageURL) {
        // If imageURL is null, it means there's no image to save
        if (imageURL != null) {
            restaurantData.put("restaurantImageUri", imageURL);
        }

        // Save restaurant details to Firestore
        firestore.collection("restaurants")
                .document(restaurantName)
                .set(restaurantData)
                .addOnSuccessListener(aVoid -> {
                    // Restaurant data added successfully
                    Toast.makeText(this, "Restaurant added successfully!", Toast.LENGTH_SHORT).show();
                    // Finish the activity to go back to the home page
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Failed to add restaurant data to Firestore
                    Toast.makeText(this, "Failed to add restaurant: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}