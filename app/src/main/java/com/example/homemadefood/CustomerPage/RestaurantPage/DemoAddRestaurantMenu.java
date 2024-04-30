package com.example.homemadefood.CustomerPage.RestaurantPage;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homemadefood.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class DemoAddRestaurantMenu extends AppCompatActivity {

    private EditText foodOrDrinkNameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private Spinner foodCategorySpinner;
    private ImageView addItemImage;
    private Uri selectedImageUri;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorageRef;

    private static final String SHARED_PREF_NAME = "homemadefood_shared_pref";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_add_restaurant_menu);

        mFirestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("item_images");

        addItemImage = findViewById(R.id.uploadItemImage);
        Button selectItemImage = findViewById(R.id.selectItemImage);
        foodOrDrinkNameEditText = findViewById(R.id.addFoodOrDrinkName);
        descriptionEditText = findViewById(R.id.addDescription);
        priceEditText = findViewById(R.id.addPrice);
        foodCategorySpinner = findViewById(R.id.foodCategorySpinner);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                Glide.with(this)
                        .load(selectedImageUri)
                        .override(600, 600)
                        .into(addItemImage);
            }
        });

        selectItemImage.setOnClickListener(v -> openImagePicker());

        Button addItemButton = findViewById(R.id.addItem);
        addItemButton.setOnClickListener(v -> {
            String providerUsername = retrieveUsernameFromSharedPreferences();

            if (providerUsername.isEmpty()) {
                Toast.makeText(DemoAddRestaurantMenu.this, "You are not logged in as a provider", Toast.LENGTH_SHORT).show();
                return;
            }

            checkRestaurantExistence(providerUsername);
        });
    }

    private void checkRestaurantExistence(String providerUsername) {
        mFirestore.collection("restaurants").whereEqualTo("providerUsername", providerUsername)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String restaurantName = documentSnapshot.getId();
                        addMenuItemToRestaurant(restaurantName);
                    } else {
                        Toast.makeText(DemoAddRestaurantMenu.this, "You don't have a restaurant", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DemoAddRestaurantMenu.this, "Error checking restaurant", Toast.LENGTH_SHORT).show();
                });
    }

    private void addMenuItemToRestaurant(String restaurantName) {
        String foodOrDrinkName = foodOrDrinkNameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        double price = Double.parseDouble(priceStr);
        String foodCategory = foodCategorySpinner.getSelectedItem().toString();

        Map<String, Object> menuItem = new HashMap<>();
        menuItem.put("itemName", foodOrDrinkName);
        menuItem.put("description", description);
        menuItem.put("price", price);
        menuItem.put("category", foodCategory);

        CollectionReference menuCollection = mFirestore.collection("restaurants").document(restaurantName).collection("Menu");

        mStorageRef.child(foodOrDrinkName).putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    mStorageRef.child(foodOrDrinkName).getDownloadUrl().addOnSuccessListener(uri -> {
                        menuItem.put("image", uri.toString());

                        menuCollection.document(foodOrDrinkName).set(menuItem)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(DemoAddRestaurantMenu.this, "New Item Added", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(DemoAddRestaurantMenu.this, "Fail to add this item", Toast.LENGTH_SHORT).show();
                                });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(DemoAddRestaurantMenu.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DemoAddRestaurantMenu.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }

    private void openImagePicker() {
        imagePickerLauncher.launch("image/*");
    }

    private String retrieveUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, "");
    }
}
