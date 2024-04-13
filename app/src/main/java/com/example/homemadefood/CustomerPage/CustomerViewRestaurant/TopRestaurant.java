package com.example.homemadefood.CustomerPage.CustomerViewRestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.homemadefood.CustomerPage.MainPage.CustomerHomepage;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantPromotionModel;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantPromotionAdapter;
import com.example.homemadefood.R;

import java.util.ArrayList;

public class TopRestaurant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_restaurant);

        ImageButton backButton = findViewById(R.id.backButton);

        // Receive data from intent
        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<RestaurantPromotionModel> promotionList = intent.getParcelableArrayListExtra("promotion_list");
            if (promotionList != null) {
                // Set promotion list to RecyclerView adapter
                RecyclerView recyclerView = findViewById(R.id.promotionVerticalRecyclerView);
                RestaurantPromotionAdapter adapter = new RestaurantPromotionAdapter(this, promotionList, null, false);

                recyclerView.setAdapter(adapter);
            }
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopRestaurant.this, CustomerHomepage.class);
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
                finish(); // finish the current activity to apply animation
                startActivity(intent);
            }
        });
    }
}