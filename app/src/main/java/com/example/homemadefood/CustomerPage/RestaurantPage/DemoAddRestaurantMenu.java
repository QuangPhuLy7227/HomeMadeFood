package com.example.homemadefood.CustomerPage.RestaurantPage;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class DemoAddRestaurantMenu extends AppCompatActivity {

    private EditText foodOrDrinkNameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private Spinner foodCategorySpinner;
    private StorageReference mStorageRef;
    private ImageView addItemImage;
    private Uri selectedImageUri;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_add_restaurant_menu);

        mFirestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("item_images");

        addItemImage = findViewById(R.id.uploadItemImage);
        Button selectItemImage = findViewById(R.id.selectItemImage);

        final CollectionReference foodMenu = mFirestore.collection("restaurants")
                .document("Breakfast")
                .collection("Menu")
                .getParent().collection("Food_Menu");

        final CollectionReference drinkMenu = mFirestore.collection("restaurants")
                .document("Breakfast")
                .collection("Menu")
                .getParent().collection("Drink_Menu");

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
//                menuItem.put("category", foodCategory);

                // Determine which collection to add the menu item based on the selected item in the spinner
                CollectionReference selectedItemCategory;
                if (foodCategory.equals("Food")) {
                    selectedItemCategory = foodMenu;
                } else {
                    selectedItemCategory = drinkMenu;
                }

                mStorageRef.child(foodOrDrinkName).putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mStorageRef.child(foodOrDrinkName).getDownloadUrl().addOnSuccessListener(uri -> {
                            menuItem.put("image", uri);
                            selectedItemCategory.add(menuItem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(DemoAddRestaurantMenu.this, "New Item Added", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DemoAddRestaurantMenu.this, "Fail to add this item", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }).addOnFailureListener(e -> Toast.makeText(DemoAddRestaurantMenu.this, "Failed to get download URL", Toast.LENGTH_SHORT).show());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    private void openImagePicker() {
        imagePickerLauncher.launch("image/*");
    }
}
