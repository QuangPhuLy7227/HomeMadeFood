package com.example.homemadefood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homemadefood.CustomerPage.MainPage.CustomerHomepage;
import com.example.homemadefood.ProviderPage.ProvidersHomePage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    ImageView logo;
    Button callSignup;
    TextView logoText, slogan;
    Button goBtn_Login;
    TextInputLayout username, password;
    RadioGroup usertype;
    FirebaseFirestore db;

    // Shared Preferences constants
    public static final String SHARED_PREF_NAME = "homemadefood_shared_pref";
    public static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");

        logo = findViewById(R.id.logoLogin);
        callSignup = findViewById(R.id.call_signup);
        logoText = findViewById(R.id.logo_text_login);
        slogan = findViewById(R.id.slogan_login);
        goBtn_Login = findViewById(R.id.go_login);

        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);
        usertype = findViewById(R.id.userType);

        goBtn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login_username = username.getEditText().getText().toString();
                String login_passwd = password.getEditText().getText().toString();
                int checkType = usertype.getCheckedRadioButtonId();

                if (!validateUsername() || !validatePassword() || checkType == -1) {
                    Toast.makeText(LoginActivity.this, "Please enter all fields to complete Sign Up!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (checkType == -1) {
                        Toast.makeText(LoginActivity.this, "Please choose your User Type!", Toast.LENGTH_SHORT).show();
                    }
                    isUser(usersCollection);
                }
            }
        });

        callSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void isUser(CollectionReference ref) {
        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();

        Query checkUser = ref.whereEqualTo("username", userEnteredUsername);
        checkUser.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    String passwordFromDB = documentSnapshot.getString("passwd");
                    String type = documentSnapshot.getString("type");
                    if (passwordFromDB.equals(userEnteredPassword)) {
                        username.setError(null);
                        username.setErrorEnabled(false);
                        String usernameFromDB = documentSnapshot.getString("username"); // Shared preferenced to local storage
                        String fullNameFromDB = documentSnapshot.getString("name");
                        String emailFromDB = documentSnapshot.getString("email");
                        String phoneFromDB = documentSnapshot.getString("phone");

                        // Save user data to SharedPreferences
                        saveUserDataToSharedPreferences(usernameFromDB, fullNameFromDB, emailFromDB, phoneFromDB);

                        int checkType = usertype.getCheckedRadioButtonId();
                        if (type.equals("cus")) {
                            if (checkType == R.id.customer) {
                                Intent intent = new Intent(LoginActivity.this, CustomerHomepage.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Incorrect User Type!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (type.equals("prov")) {
                            if (checkType == R.id.provider) {
                                Intent intent = new Intent(LoginActivity.this, ProvidersHomePage.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Incorrect User Type!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                } else {
                    username.setError("No such User exist");
                    username.requestFocus();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure
                Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to save user data to SharedPreferences
    private void saveUserDataToSharedPreferences(String username, String fullName, String email, String phone) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.apply();
    }

    // Function to retrieve user data from SharedPreferences
    private void retrieveUserDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        String fullName = sharedPreferences.getString(KEY_FULL_NAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String phone = sharedPreferences.getString(KEY_PHONE, "");
        // Use the retrieved user data as needed
    }

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}
