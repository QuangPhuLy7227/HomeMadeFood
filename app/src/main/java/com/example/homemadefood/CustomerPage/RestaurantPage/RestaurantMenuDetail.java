package com.example.homemadefood.CustomerPage.RestaurantPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homemadefood.CustomerPage.RecyclerViewData.ModelClass.RestaurantMenuDrinkModel;
import com.example.homemadefood.CustomerPage.RecyclerViewData.ModelClass.RestaurantMenuFoodModel;
import com.example.homemadefood.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RestaurantMenuDetail extends AppCompatActivity {
    private int quantity = 1;
    private float itemPriceValue;
    private RestaurantMenuDrinkModel menuDrinkData;
    private RestaurantMenuFoodModel menuFoodData;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu_detail);

        mFirestore = FirebaseFirestore.getInstance();

        ImageView itemImage = findViewById(R.id.itemImage);
        TextView itemName = findViewById(R.id.itemName);
        TextView itemPrice = findViewById(R.id.itemPrice);
        TextView pricePerItem = findViewById(R.id.cartPricePerItemTextView);

        TextView itemQuantity = findViewById(R.id.itemQuantityTextView);
        ImageButton minusQuantity = findViewById(R.id.minusQuantityButton);
        ImageButton plusQuantity = findViewById(R.id.addQuantityButton);
        Button addToCartButton = findViewById(R.id.addToCartButton);

        menuDrinkData = getIntent().getParcelableExtra("drink_data");
        menuFoodData = getIntent().getParcelableExtra("food_data");

        if (menuDrinkData != null) {
            Glide.with(this).load(menuDrinkData.getDrinkImage()).into(itemImage);
            itemName.setText(menuDrinkData.getDrinkName());
            itemPriceValue = menuDrinkData.getDrinkPrice();
            itemPrice.setText(String.valueOf(itemPriceValue));
        } else if (menuFoodData != null) {
            Glide.with(this).load(menuFoodData.getFoodImage()).into(itemImage);
            itemName.setText(menuFoodData.getFoodName());
            itemPriceValue = menuFoodData.getFoodPrice();
            itemPrice.setText(String.valueOf(itemPriceValue));
        }

        itemQuantity.setText(String.valueOf(quantity));
        updatePricePerItem(pricePerItem); // Initial price update

        minusQuantity.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                itemQuantity.setText(String.valueOf(quantity));
                updatePricePerItem(pricePerItem);
            }
        });

        plusQuantity.setOnClickListener(v -> {
            quantity++;
            itemQuantity.setText(String.valueOf(quantity));
            updatePricePerItem(pricePerItem);
        });

        addToCartButton.setOnClickListener(v -> {
            addOrderToFirestore();
        });
    }

    private double calculatePricePerItem() {
        return quantity * itemPriceValue;
    }

    private void updatePricePerItem(TextView pricePerItemTextView) {
        double pricePerItemValue = calculatePricePerItem();
        pricePerItemTextView.setText(String.valueOf(pricePerItemValue));
    }

    private void addOrderToFirestore() {

        String username ="pham012";
        String restaurantName = "Fast Food";

        Map<String, Object> order = new HashMap<>();
        String itemNameValue = menuDrinkData != null ? menuDrinkData.getDrinkName() : menuFoodData.getFoodName();
        String itemImageUrl = menuDrinkData != null ? menuDrinkData.getDrinkImage() : menuFoodData.getFoodImage();

        order.put("itemName", itemNameValue);
        order.put("timestamp", Timestamp.now());
        order.put("itemImage", itemImageUrl);
        order.put("username", username);

        order.put("restaurantName", restaurantName);

        // Price node with itemPricePerUnit and itemTotalPrice
        Map<String, Object> price = new HashMap<>();
        price.put("itemPricePerUnit", itemPriceValue);
        price.put("itemTotalPrice", calculatePricePerItem());
        order.put("price", price); // Add price node to Firestore

        String orderId = "ORDER" + UUID.randomUUID().toString().substring(0, 6); // Using first 6 characters of UUID
        order.put("orderId", orderId); // Add the generated orderId to the order map

        mFirestore.collection("orders")
                .document(orderId) // Use the generated orderId as the document ID for the order
                .set(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RestaurantMenuDetail.this, "Order added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RestaurantMenuDetail.this, "Failed to add order", Toast.LENGTH_SHORT).show();
                });



    }

}
