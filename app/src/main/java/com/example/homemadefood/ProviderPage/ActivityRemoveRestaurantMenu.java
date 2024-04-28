package com.example.homemadefood.ProviderPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.homemadefood.R;
public class ActivityRemoveRestaurantMenu extends AppCompatActivity {

    private EditText nameEditText, descriptionEditText, priceEditText;
    private Spinner categorySpinner;
    private Button addItemButton, backButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_restaurant_menu);

        nameEditText = findViewById(R.id.textAddFoodOrDrinkName);
        descriptionEditText = findViewById(R.id.textAddDescription);
        priceEditText = findViewById(R.id.textAddPrice);
        categorySpinner = findViewById(R.id.foodCategorySpinner);
        addItemButton = findViewById(R.id.addbtn);
        backButton = findViewById(R.id.backBtn);
        deleteButton = findViewById(R.id.deleteBtn);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to add item to menu goes here
                Toast.makeText(ActivityRemoveRestaurantMenu.this, "Item added!", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}