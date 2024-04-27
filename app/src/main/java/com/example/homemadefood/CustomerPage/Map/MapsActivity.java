package com.example.homemadefood.CustomerPage.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homemadefood.CustomerPage.MainPage.CustomerHomepage;
import com.example.homemadefood.ProviderPage.data.RestaurantData;
import com.example.homemadefood.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.homemadefood.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, CustomerHomepage.class);
                startActivity(intent);
            }
        });
        ImageButton mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(v -> showMapTypesBottomSheetDialog());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
//            activateSearchButton();
            activateCurrentLocationButton();
        } else {
            Toast.makeText(this, "Could not find mapFragment!", Toast.LENGTH_LONG).show();
        }

        Button nearMeButton = findViewById(R.id.nearMe);
        nearMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, get the last known location
                    FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        // User's current location is available
                                        double userLatitude = location.getLatitude();
                                        double userLongitude = location.getLongitude();

                                        // Now you have the user's latitude and longitude, you can query Firestore
                                        // to find nearby restaurants based on these coordinates
                                        findNearbyRestaurants(userLatitude, userLongitude);
                                    } else {
                                        // Unable to retrieve the user's location
                                        Toast.makeText(MapsActivity.this, "Unable to retrieve your location", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // Permission is not granted, request the permission
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                }
            }
        });
    }

    private void showMapTypesBottomSheetDialog() {
        MapTypesBottomSheetFragment mapTypesBottomSheet = new MapTypesBottomSheetFragment();
        mapTypesBottomSheet.setGoogleMap(mMap); // Pass the GoogleMap instance
        mapTypesBottomSheet.show(getSupportFragmentManager(), null);
    }

    //    private void activateSearchButton() {
//        EditText txtAddress = findViewById(R.id.txtAddress);
//
//        Button btnSearch = findViewById(R.id.btnSearch);
//        btnSearch.setOnClickListener(v -> {
//            String searchString = String.valueOf(txtAddress.getText());
//            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//            LatLng ltln;
//
//            try {
//                List<Address> addresses = geocoder.getFromLocationName(searchString, 5);
//                for (Address a : addresses) {
//                    ltln = new LatLng(a.getLatitude(), a.getLongitude());
//                    mMap.clear();
//                    mMap.addMarker(new MarkerOptions().position(ltln));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(ltln));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            txtAddress.clearFocus();
//
//            // Hide keyboard
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//        });
//    }
//
    private void activateCurrentLocationButton() {
        ImageButton currentLocationButton = findViewById(R.id.currentLocationButton);
        currentLocationButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, get current location
                getCurrentLocation();
            } else {
                // Request location permission
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                        } else {
                            Toast.makeText(MapsActivity.this, "Unable to fetch current location", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get current location
//                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-33.8688, 151.2093);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker on Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void findNearbyRestaurants(double userLatitude, double userLongitude) {
        // Define the maximum distance for nearby restaurants (in kilometers)
        final double MAX_DISTANCE = 5.0; // Adjust this value as needed

        // Create a GeoPoint object for the user's location
        GeoPoint userLocation = new GeoPoint(userLatitude, userLongitude);

        // Query Firestore to find nearby restaurants within the maximum distance
        CollectionReference restaurantsRef = FirebaseFirestore.getInstance().collection("restaurants");
        restaurantsRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<RestaurantData> nearbyRestaurants = new ArrayList<>();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Get the restaurant data from the document
                            RestaurantData restaurant = document.toObject(RestaurantData.class);

                            // Calculate the distance between the user's location and the restaurant's location
                            double distance = calculateDistance(userLocation.getLatitude(), userLocation.getLongitude(),
                                    restaurant.getLatitude(), restaurant.getLongitude());

                            // Check if the restaurant is within the maximum distance
                            if (distance <= MAX_DISTANCE) {
                                // Add the nearby restaurant to the list
                                nearbyRestaurants.add(restaurant);
                            }
                        }

                        // Display the nearby restaurants to the user
                        displayNearbyRestaurants(nearbyRestaurants);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Log.e("Nearby Restaurants", "Error getting nearby restaurants", e);
                        Toast.makeText(MapsActivity.this, "Error getting nearby restaurants", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Calculate the distance between two locations using the Haversine formula
        double R = 6371; // Radius of the Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private void displayNearbyRestaurants(List<RestaurantData> nearbyRestaurants) {
        // Clear previous markers from the map
        mMap.clear();

        // Iterate through the list of nearby restaurants
        for (RestaurantData restaurant : nearbyRestaurants) {
            // Extract restaurant details
            String name = restaurant.getName();
            double latitude = restaurant.getLatitude();
            double longitude = restaurant.getLongitude();

            // Create a LatLng object for the restaurant's location
            LatLng restaurantLocation = new LatLng(latitude, longitude);

            // Add a marker for the restaurant on the map
            mMap.addMarker(new MarkerOptions()
                    .position(restaurantLocation)
                    .title(name));
        }
    }

}