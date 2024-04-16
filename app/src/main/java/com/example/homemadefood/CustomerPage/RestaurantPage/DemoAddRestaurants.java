package com.example.homemadefood.CustomerPage.RestaurantPage;

import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.homemadefood.CustomerPage.MainPage.CustomerHomepage;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantDataModel;
import com.example.homemadefood.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class DemoAddRestaurants extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private CollectionReference mRestaurantsCollection;
    private StorageReference mStorageRef;
    private ImageView addRestaurantImage;
    private Uri selectedImageUri;
    private ActivityResultLauncher<String> imagePickerLauncher;

    // Shared Preferences constants
    private static final String SHARED_PREF_NAME = "homemadefood_shared_pref";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_add_restaurants);

        addRestaurantImage = findViewById(R.id.uploadRestaurantImage);
        Button selectRestaurantImage = findViewById(R.id.selectRestaurantImage);

        mFirestore = FirebaseFirestore.getInstance();
        mRestaurantsCollection = mFirestore.collection("restaurants");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("restaurant_images");

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                Glide.with(this)
                        .load(selectedImageUri)
                        .override(600, 600)
                        .into(addRestaurantImage);
            }
        });

        selectRestaurantImage.setOnClickListener(v -> openImagePicker());

        Button saveRestaurantInfo = findViewById(R.id.saveRestaurantInfo);
        Button visitHomepage = findViewById(R.id.backButton);

        saveRestaurantInfo.setOnClickListener(v -> {
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

            uploadRestaurant(name, category, ratingValue, totalRatingValue, deliveryFeeValue);
        });

        visitHomepage.setOnClickListener(v -> {
            Intent intent = new Intent(DemoAddRestaurants.this, CustomerHomepage.class);
            startActivity(intent);
        });
    }

    private void openImagePicker() {
        imagePickerLauncher.launch("image/*");
    }

    private void uploadRestaurant(String name, String category, float rating, float totalRating, float deliveryFee) {
        // Retrieve provider's username from SharedPreferences
        String providerUsername = retrieveUsernameFromSharedPreferences();

        // Check if a restaurant with the same provider's username already exists
        mRestaurantsCollection.whereEqualTo("providerUsername", providerUsername)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(DemoAddRestaurants.this, "You can only add one restaurant", Toast.LENGTH_SHORT).show();
                    } else {
                        RestaurantDataModel restaurantData = new RestaurantDataModel(selectedImageUri.toString(), name, category, deliveryFee, rating, (int) totalRating, providerUsername);
                        uploadRestaurantToFirestore(restaurantData);
                    }
                });
    }

    private String retrieveUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    private void uploadRestaurantToFirestore(RestaurantDataModel restaurantData) {
        String restaurantName = restaurantData.getName();

        DocumentReference restaurantRef = mRestaurantsCollection.document(restaurantName);

        // Upload image to Firebase Storage
        mStorageRef.child(restaurantName).putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    mStorageRef.child(restaurantName).getDownloadUrl().addOnSuccessListener(uri -> {
                        restaurantData.setRestaurantImageUri(uri.toString());

                        // Create a Map
                        Map<String, Object> restaurantMap = new HashMap<>();
                        restaurantMap.put("restaurantImageUri", restaurantData.getRestaurantImageUri());
                        restaurantMap.put("name", restaurantData.getName());
                        restaurantMap.put("category", restaurantData.getCategory());
                        restaurantMap.put("deliveryFee", restaurantData.getDeliveryFee());
                        restaurantMap.put("rating", restaurantData.getRating());
                        restaurantMap.put("totalRating", restaurantData.getTotalRating());
                        restaurantMap.put("providerUsername", restaurantData.getProviderUsername());

                        restaurantRef.set(restaurantMap)
                                .addOnSuccessListener(aVoid -> {
                                    // Create a "Menu" collection for every new restaurant
                                    mFirestore.collection("restaurants").document(restaurantName)
                                            .collection("Menu").document("initialDocument")
                                            .set(new HashMap<>())
                                            .addOnSuccessListener(a -> {
                                                Toast.makeText(DemoAddRestaurants.this, "Restaurant added successfully", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(DemoAddRestaurants.this, "Failed to add Menu", Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(DemoAddRestaurants.this, "Failed to add restaurant", Toast.LENGTH_SHORT).show();
                                });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(DemoAddRestaurants.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DemoAddRestaurants.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }
}
