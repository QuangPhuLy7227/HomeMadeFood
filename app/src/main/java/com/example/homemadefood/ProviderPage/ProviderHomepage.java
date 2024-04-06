package com.example.homemadefood.ProviderPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.homemadefood.R;
import com.example.homemadefood.databinding.ActivityLoginBinding;
import com.example.homemadefood.databinding.ActivityProviderHomepageBinding;

public class ProviderHomepage extends AppCompatActivity {
    ActivityProviderHomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_homepage);
        binding = ActivityProviderHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddRestaurant.class);
                startActivity(intent);
            }
        });
    }
}