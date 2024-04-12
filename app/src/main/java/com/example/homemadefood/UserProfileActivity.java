package com.example.homemadefood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    // Shared Preferences constants
    private static final String SHARED_PREF_NAME = "homemadefood_shared_pref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Retrieve user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        String fullName = sharedPreferences.getString(KEY_FULL_NAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String phone = sharedPreferences.getString(KEY_PHONE, "");

        // Set user profile data to appropriate TextViews
        TextView usernameTextView = findViewById(R.id.userNameTextView);
        TextView userEmailFirstTextView = findViewById(R.id.userEmailFirstTextView);
        TextView userFullNameTextView = findViewById(R.id.userFullName);
        TextView userEmailSecondTextView = findViewById(R.id.userEmailSecondTextView);
        TextView userPhoneTextView = findViewById(R.id.userPhoneTextView);

        usernameTextView.setText(username);
        userEmailFirstTextView.setText(email);
        userFullNameTextView.setText(fullName);
        userEmailSecondTextView.setText(email);
        userPhoneTextView.setText(phone);
    }
}
