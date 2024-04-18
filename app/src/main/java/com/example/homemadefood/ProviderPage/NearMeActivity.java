package com.example.homemadefood.ProviderPage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.R;
import com.example.homemadefood.ProviderPage.data.RestaurantData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NearMeActivity extends AppCompatActivity {

    private static final String TAG = "NearMeActivity";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private FirebaseFirestore firestore;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private RecyclerView recyclerView;
    private NearMeAdapter adapter;
    private List<RestaurantData> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize RecyclerView
//        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        adapter = new NearMeAdapter(restaurantList);
        recyclerView.setAdapter(adapter);

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
            getLocation();
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getLocation() {
        // Get the last known location of the device
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            lastKnownLocation = task.getResult();
                            // Once location is obtained, fetch nearby restaurants
                            fetchNearbyRestaurants();
                        } else {
                            Log.e(TAG, "Failed to get location.");
                            Toast.makeText(NearMeActivity.this,
                                    "Failed to get location. Please check your GPS settings.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void fetchNearbyRestaurants() {
        // Query Firestore for nearby restaurants
        firestore.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Extract restaurant data from Firestore document
                                String address = document.getString("address");
                                double latitude = document.getDouble("latitude");
                                double longitude = document.getDouble("longitude");
                                String zipCode = document.getString("zipCode");

                                // Create RestaurantData object
                                RestaurantData restaurantData = new RestaurantData(address, latitude, longitude, zipCode);

                                // Calculate distance to restaurant
                                double distance = calculateDistance(lastKnownLocation.getLatitude(),
                                        lastKnownLocation.getLongitude(),
                                        restaurantData.getLatitude(), restaurantData.getLongitude());

                                // Add restaurant to list if it's within 10km (adjust as needed)
                                if (distance <= 10) {
                                    restaurantList.add(restaurantData);
                                }
                            }
                            // Update RecyclerView
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "Error fetching nearby restaurants: ", task.getException());
                        }
                    }
                });
    }

    // Calculate distance between two locations using Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in kilometers
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the location
                getLocation();
            } else {
                // Permission denied, show a message and close the activity
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
