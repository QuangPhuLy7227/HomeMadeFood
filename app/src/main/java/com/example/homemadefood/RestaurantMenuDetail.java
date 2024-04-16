package com.example.homemadefood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homemadefood.CustomerPage.CustomerViewRestaurant.CustomerMenuSelection;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantMenuDrinkModel;

public class RestaurantMenuDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu_detail);

        ImageView itemImage = findViewById(R.id.itemImage);
        TextView itemName = findViewById(R.id.itemName);
        TextView itemPrice = findViewById(R.id.itemPrice);


        RestaurantMenuDrinkModel menuDrinkData = getIntent().getParcelableExtra("drink_data");

        if (menuDrinkData != null) {
            Glide.with(this).load(menuDrinkData.getDrinkImage()).into(itemImage);
            itemName.setText(menuDrinkData.getDrinkName());
            itemPrice.setText(String.valueOf(menuDrinkData.getDrinkPrice()));

        } else {
            Toast.makeText(RestaurantMenuDetail.this, "Do detail", Toast.LENGTH_SHORT).show();
        }
    }
}