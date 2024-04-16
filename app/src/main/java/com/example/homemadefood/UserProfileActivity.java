package com.example.homemadefood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.homemadefood.CustomerPage.MainPage.CustomerHomepage;
import com.example.homemadefood.ProviderPage.ProviderHomePage;

public class UserProfileActivity extends AppCompatActivity {

    // Shared Preferences constants
    private static final String SHARED_PREF_NAME = "homemadefood_shared_pref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static String state = "updateProfile";
    Button chooseUpdateProfile, chooseChangePassword, chooseAddPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Option button
        chooseUpdateProfile = findViewById(R.id.updateProfileButton);
        chooseChangePassword = findViewById(R.id.changePasswordButton);
        chooseAddPayment = findViewById(R.id.addPaymentButton);


        // Retrieve user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        String fullName = sharedPreferences.getString(KEY_FULL_NAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String phone = sharedPreferences.getString(KEY_PHONE, "");

        ImageButton backButton= findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, CustomerHomepage.class);
                startActivity(intent);
            }
        });


        // Set user profile data to appropriate TextViews
        TextView usernameTextView = findViewById(R.id.userNameTextView);
        TextView userEmailFirstTextView = findViewById(R.id.userEmailFirstTextView);

        usernameTextView.setText(username);
        userEmailFirstTextView.setText(email);

        showFragment(state);

        chooseUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "updateProfile";
                showFragment(state);
            }
        });

        chooseChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "changePassword";
                showFragment(state);
            }
        });

        chooseAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "addPayment";
                showFragment(state);
            }
        });
    }

    private void showFragment(String state) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (state.equals("updateProfile")) {
            UpdateProfileFragment updateProfileFragment = new UpdateProfileFragment();
            ft.replace(R.id.flContainer, updateProfileFragment);
        } else if (state.equals("changePassword")) {
            ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
            ft.replace(R.id.flContainer, changePasswordFragment);
        } else if (state.equals("addPayment")) {
            AddPaymentFragment addPaymentFragment = new AddPaymentFragment();
            ft.replace(R.id.flContainer, addPaymentFragment);
        }
        ft.commit();
    }
}
