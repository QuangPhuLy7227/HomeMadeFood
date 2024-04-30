package com.example.homemadefood.AnunjinPart;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homemadefood.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<FoodItem_Model> selectedFoodItems = new ArrayList<>();

    private TextView foodNameTextView;
    private TextView ratingTextView;
    private Button addToCartButton;
    private TextView priceTextView;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_anunjin);// food detail page

        foodNameTextView = findViewById(R.id.foodName);
        ratingTextView = findViewById(R.id.ratingTextView);
        ImageView photoImageView = findViewById(R.id.photo_1);
        addToCartButton = findViewById(R.id.addToCartButton);
        priceTextView = findViewById(R.id.textView);


        foodNameTextView.setText("Your food name");
        ratingTextView.setText("Your rating");
        photoImageView.setImageResource(R.drawable.food_photo);
        priceTextView.setText("$8.99");

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyCartActivity.class);
                intent.putExtra("selectedFoodItems", (Serializable) selectedFoodItems);
                startActivity(intent);
            }
        });

    }
}