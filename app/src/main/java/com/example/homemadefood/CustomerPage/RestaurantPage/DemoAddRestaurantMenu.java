package com.example.homemadefood.CustomerPage.RestaurantPage;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.homemadefood.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class DemoAddRestaurantMenu extends AppCompatActivity {

    private EditText foodOrDrinkNameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private Spinner foodCategorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_add_restaurant_menu);

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        final CollectionReference foodMenu = mFirestore.collection("restaurants")
                .document("Breakfast")
                .collection("Menu")
                .document("Food_Menu")
                .collection("Food_Menu_Item");

        final CollectionReference drinkMenu = mFirestore.collection("restaurants")
                .document("Breakfast")
                .collection("Menu")
                .document("Drink_Menu")
                .collection("Drink_Menu_Item");

        foodOrDrinkNameEditText = findViewById(R.id.addFoodOrDrinkName);
        descriptionEditText = findViewById(R.id.addDescription);
        priceEditText = findViewById(R.id.addPrice);
        foodCategorySpinner = findViewById(R.id.foodCategorySpinner);

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
                menuItem.put("name", foodOrDrinkName);
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

                // Add the menu item to Firestore with the food or drink name as document ID
                selectedItemCategory.document(foodOrDrinkName)
                        .set(menuItem)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(DemoAddRestaurantMenu.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(DemoAddRestaurantMenu.this, "Failed to Add Item", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}
