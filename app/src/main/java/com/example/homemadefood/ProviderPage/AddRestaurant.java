package com.example.homemadefood.ProviderPage;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homemadefood.R;
import com.example.homemadefood.RestaurantDataModel;
import com.example.homemadefood.RestaurantDatabaseHelper;
import com.example.homemadefood.databinding.ActivityAddRestaurantBinding;
import com.example.homemadefood.databinding.ActivityProviderHomepageBinding;

public class AddRestaurant extends AppCompatActivity {
    RestaurantDatabaseHelper restaurantDatabaseHelper;

    ActivityAddRestaurantBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        binding = ActivityAddRestaurantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        restaurantDatabaseHelper = new RestaurantDatabaseHelper(this);

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resName = binding.resName.getText().toString();
                String resLocation = binding.resLocation.getText().toString();
                String resContact = binding.resContact.getText().toString();
                double resRating;
                if (binding.resRating.getText().toString().equals("")){
                    resRating = 5.0;
                } else {
                    resRating = Double.parseDouble(binding.resRating.getText().toString());
                }
                String provId = binding.provId.getText().toString();
                boolean dineIn = binding.dineIn.isChecked();
                boolean open = binding.open.isChecked();
                //Add if-else to check missing info
                if (resName .equals("") || resLocation.equals("") || resContact.equals("") || provId.equals("")){
                    Toast.makeText(AddRestaurant.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    RestaurantDataModel restaurantDataModel = new RestaurantDataModel(resName, resLocation, resContact, resRating, provId, dineIn, open);
                    boolean insert = restaurantDatabaseHelper.addRes(restaurantDataModel);
                    if (insert) {
                        Toast.makeText(AddRestaurant.this, "Successfully Add Your Restaurant", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddRestaurant.this, "Cannot Add Yur Restaurant! Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
