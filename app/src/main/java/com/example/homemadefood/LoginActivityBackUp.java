package com.example.homemadefood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homemadefood.CustomerPage.MainPage.CustomerHomepage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivityBackUp extends AppCompatActivity {
    ImageView logo;
    Button callSignup;
    TextView logoText, slogan;
    Button goBtn_Login;
    TextInputLayout username, password;
    RadioGroup usertype;
    DatabaseReference reference, cusRef, provRef;
    FirebaseFirestore db;
//    ActivityLoginBinding binding;
//    UserInfoDatabaseHelper userInfoDatabaseHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityLoginBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        userInfoDatabaseHelper = new UserInfoDatabaseHelper(this);
//
//        binding.loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = binding.loginUsername.getText().toString();
//                String passsword = binding.loginPassword.getText().toString();
//                int checkID = binding.radio2.getCheckedRadioButtonId();
//                String usertype = null;
//                if (checkID == R.id.customer) {
//                    usertype = "customer";
//                } else if (checkID == R.id.provider) {
//                    usertype = "provider";
//                }
//
//                if (username.equals("") || passsword.equals("") || checkID == -1){
//                    Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
//                } else {
//                    Boolean checkAccounts = userInfoDatabaseHelper.checkEmailPassword(username, passsword, usertype);
//                    if (checkAccounts == true) {
//                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                        if (checkID == R.id.customer) {
//                            Intent intent = new Intent(getApplicationContext(), CustomerHomepage.class);
//                            startActivity(intent);
//                        } else if (checkID == R.id.provider) {
//                            Intent intent = new Intent(getApplicationContext(), ProviderHomepage.class);
//                            startActivity(intent);
//                        }
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Incorrect Email or Password!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//                startActivity(intent);
//            }
//        });
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        cusRef = reference.child("customers");
        provRef = reference.child("providers");

        goBtn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login_username = username.getEditText().getText().toString();
                String login_passwd = password.getEditText().getText().toString();
                int checkType = usertype.getCheckedRadioButtonId();

                if (!validateUsername() || !validatePassword() || checkType == -1) {
                    Toast.makeText(LoginActivityBackUp.this, "Please enter all fields to complete Sign Up!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (checkType == -1){
                        Toast.makeText(LoginActivityBackUp.this, "Please choose your User Type!", Toast.LENGTH_SHORT).show();
                    }
                    isUser(usersCollection);
                }
            }
        });

        callSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivityBackUp.this, SignUpActivity.class);
                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View,String>(logo, "logo_image");
                pairs[1] = new Pair<View,String>(logoText, "logo_text");
                pairs[2] = new Pair<View,String>(slogan, "slogan");
                pairs[3] = new Pair<View,String>(goBtn_Login, "go_button");
                pairs[4] = new Pair<View,String>(callSignup, "log_sign");

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivityBackUp.this,pairs);
                startActivity(intent, activityOptions.toBundle());
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
                        String usernameFromDB = documentSnapshot.getString("username"); // Shared preference to local storage
                        int checkType = usertype.getCheckedRadioButtonId();

                        if (type.equals("cus")) {
                            if (checkType == R.id.customer) {
                                Intent intent = new Intent(LoginActivityBackUp.this, CustomerHomepage.class);
                                intent.putExtra("username", usernameFromDB);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivityBackUp.this, "Incorrect User Type!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (type.equals("prov")) {
                            if (checkType == R.id.provider) {
                                Intent intent = new Intent(LoginActivityBackUp.this, ProviderHomePage.class);
                                intent.putExtra("username", usernameFromDB);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivityBackUp.this, "Incorrect User Type!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivityBackUp.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

}