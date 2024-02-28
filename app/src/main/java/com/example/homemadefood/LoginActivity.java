package com.example.homemadefood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.homemadefood.CustomerPage.CustomerHomepage;
import com.example.homemadefood.ProviderPage.ProviderHomepage;
import com.example.homemadefood.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.loginUsername.getText().toString();
                String passsword = binding.loginPassword.getText().toString();
                int checkID = binding.radio2.getCheckedRadioButtonId();
                String usertype = null;
                if (checkID == R.id.customer) {
                    usertype = "customer";
                } else if (checkID == R.id.provider) {
                    usertype = "provider";
                }

                if (username.equals("") || passsword.equals("") || checkID == -1){
                    Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkAccounts = databaseHelper.checkEmailPassword(username, passsword, usertype);
                    if (checkAccounts == true) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        if (checkID == R.id.customer) {
                            Intent intent = new Intent(getApplicationContext(), CustomerHomepage.class);
                            startActivity(intent);
                        } else if (checkID == R.id.provider) {
                            Intent intent = new Intent(getApplicationContext(), ProviderHomepage.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect Email or Password!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}