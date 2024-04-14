package com.example.homemadefood.ProviderPage;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.ProviderPage.adapter.RestaurantAdapter;
import com.example.homemadefood.ProviderPage.data.RestaurantData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.homemadefood.R;
import java.util.ArrayList;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {

    private static final String TAG = "RestaurantListActivity";
    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<RestaurantData> restaurantList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(this, restaurantList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();

        retrieveRestaurantData();
    }

    private void retrieveRestaurantData() {
        firestore.collection("restaurants")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            RestaurantData restaurantData = documentSnapshot.toObject(RestaurantData.class);
                            restaurantList.add(restaurantData);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error retrieving restaurants: " + e.getMessage());
                        Toast.makeText(RestaurantListActivity.this, "Failed to retrieve restaurants", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
