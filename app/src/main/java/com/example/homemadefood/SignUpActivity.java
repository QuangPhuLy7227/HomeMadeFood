package com.example.homemadefood;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.homemadefood.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    UserInfoDatabaseHelper userInfoDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userInfoDatabaseHelper = new UserInfoDatabaseHelper(this);
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.signupUsername.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirm.getText().toString();
                int checkID = binding.radio1.getCheckedRadioButtonId();
                String usertype = null;
                if (checkID == R.id.customer) {
                    usertype = "customer";
                } else if (checkID == R.id.provider) {
                    usertype = "provider";
                }

                if (username.equals("") || password.equals("") || confirmPassword.equals("") || checkID == -1) {
                    Toast.makeText(SignUpActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(confirmPassword)){
                        Boolean checkUserEmail = userInfoDatabaseHelper.checkEmail(username,usertype);
                        if (checkUserEmail == false) {
                            Boolean insert = userInfoDatabaseHelper.insertData(username, password, usertype);
                            if (insert == true) {
                                Toast.makeText(SignUpActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUpActivity.this, "Signup Fail! Please Signup again", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "User already exists! Please login.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Please retype your password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}