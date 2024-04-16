package com.example.homemadefood.CustomerPage.CustomerViewRestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homemadefood.CustomerPage.MainPage.CustomerHomepage;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantDataModel;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantMenuDrinkModel;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantMenuFoodModel;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantMenuSelectionAdapter;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RecyclerViewInterface;
import com.example.homemadefood.R;
import com.example.homemadefood.RestaurantMenuDetail;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomerMenuSelection extends AppCompatActivity implements RecyclerViewInterface {

    private FirebaseFirestore mFirestore;
    private RecyclerView drinkMenuRecyclerView;
    private RecyclerView foodMenuRecyclerView;
    private RestaurantMenuSelectionAdapter drinkAdapter;
    private RestaurantMenuSelectionAdapter foodAdapter;
    private List<RestaurantMenuDrinkModel> drinkList = new ArrayList<>();
    private List<RestaurantMenuFoodModel> foodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu_selection);

        mFirestore = FirebaseFirestore.getInstance();

        ImageButton closeButton = findViewById(R.id.closeButton);
        ImageView restaurantImage = findViewById(R.id.restaurantImage);
        TextView restaurantNameTextView = findViewById(R.id.restaurantName);
        TextView ratingTextView = findViewById(R.id.ratingTextView);
        TextView totalRatingTextView = findViewById(R.id.totalRatingTextView);

        drinkMenuRecyclerView = findViewById(R.id.drinkRecyclerView);
        foodMenuRecyclerView = findViewById(R.id.foodRecyclerView);

        drinkAdapter = new RestaurantMenuSelectionAdapter(CustomerMenuSelection.this, new ArrayList<>(), this);
        drinkMenuRecyclerView.setAdapter(drinkAdapter);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(CustomerMenuSelection.this, 2);
        drinkMenuRecyclerView.setLayoutManager(gridLayoutManager1);

        foodAdapter = new RestaurantMenuSelectionAdapter(CustomerMenuSelection.this, new ArrayList<>(), this);
        foodMenuRecyclerView.setAdapter(foodAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CustomerMenuSelection.this);
        foodMenuRecyclerView.setLayoutManager(linearLayoutManager);

        RestaurantDataModel restaurantData = getIntent().getParcelableExtra("restaurant_data");

        if (restaurantData != null) {
            Glide.with(this).load(restaurantData.getRestaurantImageUri()).into(restaurantImage);
            restaurantNameTextView.setText(restaurantData.getName());
            ratingTextView.setText(String.valueOf(restaurantData.getRating()));
            totalRatingTextView.setText(String.valueOf(restaurantData.getTotalRating()));
            String resName = restaurantData.getName();
            fetchMenuItems(resName);
        } else {
            Toast.makeText(CustomerMenuSelection.this, "No Data", Toast.LENGTH_SHORT).show();
        }

        closeButton.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(CustomerMenuSelection.this, CustomerHomepage.class));
        });
    }

    private void fetchMenuItems(String restaurantName) {
        CollectionReference MenuRef = mFirestore.collection("restaurants")
                .document(restaurantName)
                .collection("Menu");

        // Query for drink items
        Query drinkQuery = MenuRef.whereEqualTo("category", "Drink");

        // Query for food items
        Query foodQuery = MenuRef.whereEqualTo("category", "Food");

        drinkQuery.get().addOnSuccessListener(queryDocumentSnapshots1 -> {
            drinkList.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots1) {
                String drinkImageUri = documentSnapshot.getString("image");
                String drinkName = documentSnapshot.getString("itemName");
                float drinkPrice = documentSnapshot.getDouble("price").floatValue();

                RestaurantMenuDrinkModel drinkModel = new RestaurantMenuDrinkModel(drinkImageUri, drinkName, drinkPrice);
                drinkList.add(drinkModel);
            }

            drinkAdapter.setData(new ArrayList<>(drinkList));
            drinkAdapter.notifyDataSetChanged();

        }).addOnFailureListener(e -> {
            Toast.makeText(CustomerMenuSelection.this, "Failed to fetch drink menu items", Toast.LENGTH_SHORT).show();
        });

        foodQuery.get().addOnSuccessListener(queryDocumentSnapshots2 -> {
            foodList.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots2) {
                String foodImageUri = documentSnapshot.getString("image");
                String foodName = documentSnapshot.getString("itemName");
                String foodDescription = documentSnapshot.getString("description");
                float foodPrice = documentSnapshot.getDouble("price").floatValue();

                RestaurantMenuFoodModel foodModel = new RestaurantMenuFoodModel(foodImageUri, foodName, foodDescription, foodPrice);
                foodList.add(foodModel);
            }

            foodAdapter.setData(new ArrayList<>(foodList));
            foodAdapter.notifyDataSetChanged();

        }).addOnFailureListener(e -> {
            Toast.makeText(CustomerMenuSelection.this, "Failed to fetch food menu items", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onItemClick(int position) {
        // Handle item click for both drink and food items
        if (position < drinkList.size()) {
            Intent intent = new Intent(CustomerMenuSelection.this, RestaurantMenuDetail.class);
            intent.putExtra("drink_data", drinkList.get(position));
            startActivity(intent);
        } else {
            int foodPosition = position - drinkList.size();
            Intent intent = new Intent(CustomerMenuSelection.this, RestaurantMenuDetail.class);
            intent.putExtra("food_data", foodList.get(foodPosition));
            startActivity(intent);
        }
    }

}
