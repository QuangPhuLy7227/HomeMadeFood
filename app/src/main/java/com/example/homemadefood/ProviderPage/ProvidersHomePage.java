package com.example.homemadefood.ProviderPage;

import static com.example.homemadefood.LoginActivity.KEY_USERNAME;
import static com.example.homemadefood.LoginActivity.SHARED_PREF_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.homemadefood.ProviderPage.adapter.DrinkMenuAdapter;
import com.example.homemadefood.ProviderPage.adapter.FoodMenuAdapter;
import com.example.homemadefood.ProviderPage.adapter.RestaurantAdapter;
import com.example.homemadefood.ProviderPage.data.MenuData;
import com.example.homemadefood.ProviderPage.data.RestaurantData;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.LoginActivity;
import com.example.homemadefood.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProvidersHomePage extends AppCompatActivity {

    private ImageButton profileBtn;
    private Button addButton;
    private Button addMenButton;
    private TextView restaurantPlaceholder;
    private Button modifyResButton;
    private Button modifyMenButton;
    private TextView menuPlaceholder;

    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;

    private RecyclerView recyclerView1; // For drinks menu
    private RecyclerView recyclerView2; // For food menu
    private DrinkMenuAdapter drinkMenuAdapter;
    private FoodMenuAdapter foodMenuAdapter;
    private RestaurantAdapter adapter;
    private List<RestaurantData> restaurantList;
    private List<MenuData> drinkMenuList;
    private List<MenuData> foodMenuList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home_page);

        profileBtn = findViewById(R.id.profileButton);
        addButton = findViewById(R.id.addRestaurantButton);
        addMenButton = findViewById(R.id.addMenuButton);
        modifyResButton = findViewById(R.id.modifyResButton);
        modifyMenButton = findViewById(R.id.modifyMenuButton);

        // Initialize placeholders
        restaurantPlaceholder = findViewById(R.id.restaurantPlaceholder);
        menuPlaceholder = findViewById(R.id.menuPlaceholder);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();
        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);

        // Initialize menu adapters
        drinkMenuList = new ArrayList<>();
        foodMenuList = new ArrayList<>();

        drinkMenuAdapter = new DrinkMenuAdapter(this, drinkMenuList); // Assuming menuDataList is a List<MenuData>
        foodMenuAdapter = new FoodMenuAdapter(this, foodMenuList); // Assuming you have a MenuAdapter for food

        restaurantList = new ArrayList<>();

        adapter = new RestaurantAdapter(this, restaurantList); // Pass context here
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkUserData();

        // Set onClickListener for the profile button
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ProvidersHomePage.this, profileBtn);
                popupMenu.getMenuInflater().inflate(R.menu.profile_pop_up_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.viewAccount) {
                            // Handle view account click
                            // Add your code here to navigate to the View Account page
                            return true;
                        } else if (menuItem.getItemId() == R.id.changePassword) {
                            // Handle change password click
                            // Add your code here to navigate to the Change Password page
                            return true;
                        } else if (menuItem.getItemId() == R.id.logOut) {
                            // Handle log out click
                            logout(); // Assuming this method is for logging out
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        // Set onClickListener for the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start the AddRestaurantInfoActivity
                Intent intent = new Intent(ProvidersHomePage.this, AddRestaurantInfoActivity.class);

                // Start the activity
                startActivity(intent);
            }
        });

        modifyResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start the AddRestaurantInfoActivity
                Intent intent = new Intent(ProvidersHomePage.this, AddRestaurantInfoActivity.class);

                // Pass the restaurant data
                intent.putExtra("restaurantData", restaurantList.get(0)); // Assuming you have a single restaurant

                // Pass a flag indicating it's for modification
                intent.putExtra("isEditMode", true);

                // Start the activity
                startActivity(intent);
            }
        });
        addMenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the DemoAddRestaurantMenu
                Intent intent = new Intent(ProvidersHomePage.this, DemoAddRestaurantMenu.class);

//                // Pass a flag indicating it's for modification
//                intent.putExtra("isEditMode", true);

                // Start the activity
                startActivity(intent);
            }
        });

    }
    private void checkUserData() {
        // Retrieve the username of the current user
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, "");

        // Query Firestore to check if the user has data
        firestore.collection("restaurants")
                .whereEqualTo("addedBy", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // User has data, hide add button and show modify button
                        addButton.setVisibility(View.GONE);
                        modifyResButton.setVisibility(View.VISIBLE);
                        modifyMenButton.setVisibility(View.VISIBLE);
                        // Fetch restaurant data
                        fetchRestaurantData();
                        // Fetch Menu data
                        fetchMenuData();
                    } else {
                        // User does not have data, show placeholder views
                        showPlaceholderViews();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Log.e("ProvidersHomePage", "Error checking user data: " + e.getMessage());
                });
    }


    private void fetchRestaurantData() {
        // Retrieve the username of the current user
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        // Query Firestore for restaurant data added by the current user
        firestore.collection("restaurants")
                .whereEqualTo("addedBy", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert Firestore document to RestaurantData object
                        RestaurantData restaurantData = document.toObject(RestaurantData.class);
                        // Add restaurantData to the list
                        restaurantList.add(restaurantData);
                    }
                    // Notify the adapter that the data set has changed
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Log.e("ProvidersHomePage", "Error fetching restaurant data: " + e.getMessage());
                });
    }

    private void fetchMenuData() {
        // Retrieve the username of the current user
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, "");

        // Query Firestore for food menu data added by the current user
        firestore.collection("restaurants")
                .document(username)
                .collection("Menu")
                .document("Food_Menu")
                .collection("Items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert Firestore document to MenuData object
                        MenuData menuData = document.toObject(MenuData.class);
                        // Add menuData to the foodMenuList
                        foodMenuList.add(menuData);
                    }
                    // Notify the adapter that the data set has changed
                    foodMenuAdapter.notifyDataSetChanged();
                    // Set the adapter on the RecyclerView
                    recyclerView1.setAdapter(foodMenuAdapter);
                    // Set the layout manager on the RecyclerView
                    recyclerView1.setLayoutManager(new LinearLayoutManager(this));
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Log.e("ProvidersHomePage", "Error fetching food menu data: " + e.getMessage());
                });

        // Query Firestore for drink menu data added by the current user
        firestore.collection("restaurants")
                .document(username)
                .collection("Menu")
                .document("Drink_Menu")
                .collection("Items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert Firestore document to MenuData object
                        MenuData menuData = document.toObject(MenuData.class);
                        // Add menuData to the drinkMenuList
                        drinkMenuList.add(menuData);
                    }
                    // Notify the adapter that the data set has changed
                    drinkMenuAdapter.notifyDataSetChanged();
                    // Set the adapter on the RecyclerView
                    recyclerView2.setAdapter(drinkMenuAdapter);
                    // Set the layout manager on the RecyclerView
                    recyclerView2.setLayoutManager(new LinearLayoutManager(this));
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Log.e("ProvidersHomePage", "Error fetching drink menu data: " + e.getMessage());
                });
    }


    private void showPlaceholderViews() {
        // Show the placeholder views
        Log.d("ProvidersHomePage", "Showing placeholder views");
        restaurantPlaceholder.setVisibility(View.VISIBLE);
        menuPlaceholder.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
        addMenButton.setVisibility(View.VISIBLE);
    }


    // Method for logging out
    private void logout() {
        Intent intent = new Intent(ProvidersHomePage.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}