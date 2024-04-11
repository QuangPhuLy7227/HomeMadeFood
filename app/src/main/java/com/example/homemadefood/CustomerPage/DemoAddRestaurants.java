package com.example.homemadefood.CustomerPage;

import android.content.Intent;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.homemadefood.CustomerPage.MainPage.CustomerHomepage;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantData;
import com.example.homemadefood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoAddRestaurants extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private CollectionReference mRestaurantsCollection;
    private StorageReference mStorageRef;
    private List<RestaurantData> dataList;
    private ImageView addRestaurantImage;
    private Uri selectedImageUri;
    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_add_restaurants);

        addRestaurantImage = findViewById(R.id.uploadRestaurantImage);
        Button selectRestaurantImage = findViewById(R.id.selectRestaurantImage);

        mFirestore = FirebaseFirestore.getInstance();
        mRestaurantsCollection = mFirestore.collection("restaurants");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("restaurant_images");
        dataList = new ArrayList<>();

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

                uploadRestaurantToFireStore(restaurantData);
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

    private void uploadRestaurantToFireStore(RestaurantData restaurantData) {
        String restaurantName = restaurantData.getName();

        mRestaurantsCollection.document(restaurantName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Toast.makeText(DemoAddRestaurants.this, "Restaurant with this name already exists", Toast.LENGTH_SHORT).show();
                } else {
                    DocumentReference restaurantRef = mRestaurantsCollection.document(restaurantName);

                    mStorageRef.child(restaurantName).putFile(selectedImageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                mStorageRef.child(restaurantName).getDownloadUrl().addOnSuccessListener(uri -> {
                                    restaurantData.setRestaurantImageUri(uri.toString());

                                    // Create a new document with restaurantData
                                    Map<String, Object> restaurant = new HashMap<>();
                                    restaurant.put("name", restaurantData.getName());
                                    restaurant.put("category", restaurantData.getCategory());
                                    restaurant.put("deliveryFee", restaurantData.getDeliveryFee());
                                    restaurant.put("rating", restaurantData.getRating());
                                    restaurant.put("totalRating", restaurantData.getTotalRating());
                                    restaurant.put("restaurantImageUri", restaurantData.getRestaurantImageUri());

                                    // Add a new document to the Firestore collection
                                    mRestaurantsCollection.document(restaurantName)
                                            .set(restaurant)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(DemoAddRestaurants.this, "Restaurant added successfully", Toast.LENGTH_SHORT).show();
                                                dataList.add(restaurantData);
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(DemoAddRestaurants.this, "Failed to add restaurant: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.e("Firestore", "Error adding document", e);
                                            });
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(DemoAddRestaurants.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("Firestore", "Failed to get download URL", e);
                                });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(DemoAddRestaurants.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("Firestore", "Failed to upload image", e);
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DemoAddRestaurants.this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firestore", "Database error", e);
            }
        });
    }
}
