package com.example.homemadefood.ProviderPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.homemadefood.CustomerPage.CustomerHomepage;
import com.example.homemadefood.R;
import com.example.homemadefood.databinding.ActivityLoginBinding;
import com.example.homemadefood.databinding.ActivityProviderHomepageBinding;

public class ProviderHomepage extends AppCompatActivity {
    TextView username, name;
    Button addRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_homepage);
        Intent intent = getIntent();
        String cusName = intent.getStringExtra("name");
        String cusUsername = intent.getStringExtra("username");

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        username.setText("UserName: " + cusUsername);
        name.setText("Welcome Back, " + cusName);
        addRes = findViewById(R.id.addRes);

        addRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddRestaurant.class);
                startActivity(intent);
            }
        });
    }
}