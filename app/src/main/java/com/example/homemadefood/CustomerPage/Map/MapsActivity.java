package com.example.homemadefood.CustomerPage.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

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
}