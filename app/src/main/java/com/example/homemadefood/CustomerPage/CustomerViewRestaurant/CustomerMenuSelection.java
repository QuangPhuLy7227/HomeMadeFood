package com.example.homemadefood.CustomerPage.CustomerViewRestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homemadefood.CustomerPage.MainPage.CustomerHomepage;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantDataModel;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantMenuDrinkModel;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantMenuSelectionAdapter;
import com.example.homemadefood.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomerMenuSelection extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private RecyclerView recyclerView;
    private RestaurantMenuSelectionAdapter adapter;
    private List<RestaurantMenuDrinkModel> drinkList = new ArrayList<>();

//    private static final String SHARED_PREF_NAME = "homemadefood_shared_pref";
//    private static final String KEY_USERNAME = "username";

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
        recyclerView = findViewById(R.id.drinkRecyclerView);

//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
//        String providerUsername = sharedPreferences.getString(KEY_USERNAME, "");

//        if (!providerUsername.isEmpty()) {
//            fetchDrinkMenu(providerUsername);
//        } else {
//            Toast.makeText(CustomerMenuSelection.this, "Provider not logged in", Toast.LENGTH_SHORT).show();
//        }

        RestaurantDataModel restaurantData = getIntent().getParcelableExtra("restaurant_data");

        if (restaurantData != null) {
            Glide.with(this).load(restaurantData.getRestaurantImageUri()).into(restaurantImage);
            restaurantNameTextView.setText(restaurantData.getName());
            ratingTextView.setText(String.valueOf(restaurantData.getRating()));
            totalRatingTextView.setText(String.valueOf(restaurantData.getTotalRating()));
            String resName = restaurantData.getName();
            fetchDrinkMenu(resName);
        } else {
            Toast.makeText(CustomerMenuSelection.this, "No Data", Toast.LENGTH_SHORT).show();
        }

        closeButton.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(CustomerMenuSelection.this, CustomerHomepage.class));
        });
    }

    private void fetchDrinkMenu(String restaurantName) {
        CollectionReference MenuRef = mFirestore.collection("restaurants")
                .document(restaurantName)
                .collection("Menu");
        Query drinkQuery = MenuRef.whereEqualTo("category", "Drink");
        drinkQuery.get().addOnSuccessListener(queryDocumentSnapshots1 -> {
            drinkList.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots1) {
                String drinkImageUri = documentSnapshot.getString("image");
                String drinkName = documentSnapshot.getString("itemName");
                float drinkPrice = documentSnapshot.getDouble("price").floatValue();

                RestaurantMenuDrinkModel drinkModel = new RestaurantMenuDrinkModel(drinkImageUri, drinkName, drinkPrice);
                drinkList.add(drinkModel);
            }

            adapter = new RestaurantMenuSelectionAdapter(CustomerMenuSelection.this, drinkList);
            recyclerView.setAdapter(adapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(CustomerMenuSelection.this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);

        }).addOnFailureListener(e -> {
            Toast.makeText(CustomerMenuSelection.this, "Failed to fetch drink menu items", Toast.LENGTH_SHORT).show();
        });

//        mFirestore.collection("restaurants")
//                .whereEqualTo("name", restaurantName)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if (!queryDocumentSnapshots.isEmpty()) {
//                        DocumentSnapshot restaurantDoc = queryDocumentSnapshots.getDocument(restaurantName);
//
//                        CollectionReference drinkMenuRef = mFirestore.collection("restaurants")
//                                .document(restaurantName)
//                                .collection("Menu");
//
//                        Query drinkQuery = drinkMenuRef.whereEqualTo("category", "Drink");
//
//                        drinkQuery.get().addOnSuccessListener(queryDocumentSnapshots1 -> {
//                            drinkList.clear();
//                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots1) {
//                                String drinkImageUri = documentSnapshot.getString("image");
//                                String drinkName = documentSnapshot.getString("itemName");
//                                float drinkPrice = documentSnapshot.getDouble("price").floatValue();
//
//                                RestaurantMenuDrinkModel drinkModel = new RestaurantMenuDrinkModel(drinkImageUri, drinkName, drinkPrice);
//                                drinkList.add(drinkModel);
//                            }
//
//                            adapter = new RestaurantMenuSelectionAdapter(CustomerMenuSelection.this, drinkList);
//                            recyclerView.setAdapter(adapter);
//                            GridLayoutManager gridLayoutManager = new GridLayoutManager(CustomerMenuSelection.this, 2);
//                            recyclerView.setLayoutManager(gridLayoutManager);
//
//                        }).addOnFailureListener(e -> {
//                            Toast.makeText(CustomerMenuSelection.this, "Failed to fetch drink menu items", Toast.LENGTH_SHORT).show();
//                        });
//
//                    } else {
//                        Toast.makeText(CustomerMenuSelection.this, "No restaurant found for this provider", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(CustomerMenuSelection.this, "Error fetching restaurant", Toast.LENGTH_SHORT).show();
//                });
    }
}

