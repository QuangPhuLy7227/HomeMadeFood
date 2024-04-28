package com.example.homemadefood.ProviderPage;

import static com.example.homemadefood.LoginActivity.KEY_USERNAME;
import static com.example.homemadefood.LoginActivity.SHARED_PREF_NAME;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homemadefood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DemoAddRestaurantMenu extends AppCompatActivity {

    private EditText foodOrDrinkNameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private Spinner foodCategorySpinner;
    private StorageReference mStorageRef;
    private ImageView addItemImage;
    private Uri selectedImageUri;

    private Button backButton;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private FirebaseFirestore mFirestore;


    private CollectionReference foodMenu;
    private CollectionReference drinkMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_add_restaurant_menu);

        mFirestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("item_images");

        addItemImage = findViewById(R.id.uploadItemImage);
        Button selectItemImage = findViewById(R.id.selectItemImage);


        // Retrieve the username of the current user
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, "");

        // Reference to Food Menu
        foodMenu = mFirestore.collection("restaurants")
                .document(username)
                .collection("Menu")
                .document("Food_Menu").collection("Items");

        // Reference to Drink Menu
        drinkMenu = mFirestore.collection("restaurants")
                .document(username)
                .collection("Menu")
                .document("Drink_Menu").collection("Items");

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                Glide.with(this)
                        .load(selectedImageUri)
                        .override(600, 600)
                        .into(addItemImage);
            }
        });

        foodOrDrinkNameEditText = findViewById(R.id.addFoodOrDrinkName);
        descriptionEditText = findViewById(R.id.addDescription);
        priceEditText = findViewById(R.id.addPrice);
        foodCategorySpinner = findViewById(R.id.foodCategorySpinner);

        selectItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        Button addItemButton = findViewById(R.id.addItem);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMenuDetails();
            }
        });


        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
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

    private void saveMenuDetails() {
        String foodOrDrinkName = foodOrDrinkNameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        double price = Double.parseDouble(priceStr);
        String foodCategory = foodCategorySpinner.getSelectedItem().toString();

        // Create a Map to hold the menu item data
        Map<String, Object> menuItem = new HashMap<>();
        menuItem.put("itemName", foodOrDrinkName);
        menuItem.put("description", description);
        menuItem.put("price", price);
        // menuItem.put("category", foodCategory); // Uncomment this if you want to add category

        // Determine which collection to add the menu item based on the selected item in the spinner
        CollectionReference selectedItemCategory;
        if (foodCategory.equals("Food")) {
            selectedItemCategory = foodMenu;
        } else {
            selectedItemCategory = drinkMenu;
        }

        // Check if an image is selected
        if (selectedImageUri != null) {
            // Upload image to Firebase Storage
            mStorageRef.child(foodOrDrinkName).putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully, get its download URL
                        mStorageRef.child(foodOrDrinkName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    // Add image URL to the menu item data
                                    menuItem.put("image", uri.toString());

                                    // Add the menu item to Firestore with the item name as the document ID
                                    selectedItemCategory.document(foodOrDrinkName).set(menuItem)
                                            .addOnSuccessListener(aVoid -> {
                                                // Item added successfully
                                                Toast.makeText(DemoAddRestaurantMenu.this, "New Item Added", Toast.LENGTH_SHORT).show();
                                                onBackPressed(); // Go back to previous activity
                                            })
                                            .addOnFailureListener(e -> {
                                                // Error adding item to Firestore
                                                Toast.makeText(DemoAddRestaurantMenu.this, "Failed to add this item", Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    // Error getting image URL
                                    Toast.makeText(DemoAddRestaurantMenu.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Error uploading image
                        Toast.makeText(DemoAddRestaurantMenu.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // No image selected, add the menu item without an image
            selectedItemCategory.document(foodOrDrinkName).set(menuItem)
                    .addOnSuccessListener(aVoid -> {
                        // Item added successfully
                        Toast.makeText(DemoAddRestaurantMenu.this, "New Item Added", Toast.LENGTH_SHORT).show();
                        onBackPressed(); // Go back to previous activity
                    })
                    .addOnFailureListener(e -> {
                        // Error adding item to Firestore
                        Toast.makeText(DemoAddRestaurantMenu.this, "Failed to add this item", Toast.LENGTH_SHORT).show();
                    });
        }
    }


}
// Before creating Method.
//        addItemButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String foodOrDrinkName = foodOrDrinkNameEditText.getText().toString().trim();
//                String description = descriptionEditText.getText().toString().trim();
//                String priceStr = priceEditText.getText().toString().trim();
//                double price = Double.parseDouble(priceStr);
//                String foodCategory = foodCategorySpinner.getSelectedItem().toString();
//
//                // Create a Map to hold the menu item data
//                Map<String, Object> menuItem = new HashMap<>();
//                menuItem.put("itemName", foodOrDrinkName);
//                menuItem.put("description", description);
//                menuItem.put("price", price);
////                menuItem.put("category", foodCategory);
//
//                // Determine which collection to add the menu item based on the selected item in the spinner
//                CollectionReference selectedItemCategory;
//                if (foodCategory.equals("Food")) {
//                    selectedItemCategory = foodMenu;
//                } else {
//                    selectedItemCategory = drinkMenu;
//                }
//
//                mStorageRef.child(foodOrDrinkName).putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        mStorageRef.child(foodOrDrinkName).getDownloadUrl().addOnSuccessListener(uri -> {
//                            menuItem.put("image", uri);
//                            selectedItemCategory.add(menuItem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Toast.makeText(com.example.homemadefood.ProviderPage.DemoAddRestaurantMenu.this, "New Item Added", Toast.LENGTH_SHORT).show();
//                                    onBackPressed();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(com.example.homemadefood.ProviderPage.DemoAddRestaurantMenu.this, "Fail to add this item", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }).addOnFailureListener(e -> Toast.makeText(com.example.homemadefood.ProviderPage.DemoAddRestaurantMenu.this, "Failed to get download URL", Toast.LENGTH_SHORT).show());
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//            }
//        });