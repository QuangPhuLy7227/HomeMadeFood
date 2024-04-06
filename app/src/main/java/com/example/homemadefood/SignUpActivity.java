package com.example.homemadefood;

import static android.app.ProgressDialog.show;

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

import com.example.homemadefood.databinding.ActivitySignUpBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference, cusRef, provRef;

    ImageView logo;
    Button callLogin;
    TextView logoText, slogan;
    Button goBtn_signup;
    TextInputLayout regName, regUsername, regEmail, regPhone, regPass, regConfirmPass;
    RadioGroup regUserType;

//    ActivitySignUpBinding binding;
//    UserInfoDatabaseHelper userInfoDatabaseHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        userInfoDatabaseHelper = new UserInfoDatabaseHelper(this);
//        binding.signupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = binding.signupUsername.getText().toString();
//                String password = binding.signupPassword.getText().toString();
//                String confirmPassword = binding.signupConfirm.getText().toString();
//                int checkID = binding.radio1.getCheckedRadioButtonId();
//                String usertype = null;
//                if (checkID == R.id.customer) {
//                    usertype = "customer";
//                } else if (checkID == R.id.provider) {
//                    usertype = "provider";
//                }
//
//                if (username.equals("") || password.equals("") || confirmPassword.equals("") || checkID == -1) {
//                    Toast.makeText(SignUpActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (password.equals(confirmPassword)){
//                        Boolean checkUserEmail = userInfoDatabaseHelper.checkEmail(username,usertype);
//                        if (checkUserEmail == false) {
//                            Boolean insert = userInfoDatabaseHelper.insertData(username, password, usertype);
//                            if (insert == true) {
//                                Toast.makeText(SignUpActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Toast.makeText(SignUpActivity.this, "Signup Fail! Please Signup again", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(SignUpActivity.this, "User already exists! Please login.", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(SignUpActivity.this, "Please retype your password", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        logo = findViewById(R.id.logoSignup);
        callLogin = findViewById(R.id.call_login);
        logoText = findViewById(R.id.logo_text_signup);
        slogan = findViewById(R.id.slogan_signup);
        goBtn_signup = findViewById(R.id.go_signup);

        regName = findViewById(R.id.fullname);
        regUsername = findViewById(R.id.username_signup);
        regEmail = findViewById(R.id.email);
        regPhone = findViewById(R.id.phone);
        regPass = findViewById(R.id.password_signup);
        regConfirmPass = findViewById(R.id.confirm_password);
        regUserType = findViewById(R.id.userType);

        goBtn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                cusRef = reference.child("customers");
                provRef = reference.child("providers");

                //Get all values
                String name = regName.getEditText().getText().toString();
                String username = regUsername.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phone = regPhone.getEditText().getText().toString();
                String passwd = regPass.getEditText().getText().toString();
                String confirmPass = regConfirmPass.getEditText().getText().toString();
                String usertype = null;
                int checkType = regUserType.getCheckedRadioButtonId();
                if (checkType == -1){
                    Toast.makeText(SignUpActivity.this, "Please choose your User Type!", Toast.LENGTH_SHORT).show();
                } else if (checkType == R.id.customer) {
                    usertype = "cus";
                } else if (checkType == R.id.provider) {
                    usertype = "prov";
                }

                if (!validateName() || !validateUsername() || !validateEmail() || !validatePassword() || !validatePhoneNo() || checkType == -1) {
                    Toast.makeText(SignUpActivity.this, "Please enter all fields to complete Sign Up!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!validateConfirmPass()) {
                        Toast.makeText(SignUpActivity.this, "Please retype Password and Confirm Password", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        UserHelperClass userHelperClass = new UserHelperClass(name,username,email,phone,passwd);
                        if (checkType == R.id.customer) {
                            checkExistUsersAndAdd(cusRef,username,userHelperClass);
                        } else if (checkType == R.id.provider) {
                            checkExistUsersAndAdd(provRef,username,userHelperClass);
                        }
//                        Toast.makeText(SignUpActivity.this, "Sign Up success! Please LogIn to continue", Toast.LENGTH_LONG).show();
                        loginIntent();
                    }
                }
            }
        });

        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIntent();
            }
        });
    }

    private void loginIntent() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair<View,String>(logo, "logo_image");
        pairs[1] = new Pair<View,String>(logoText, "logo_text");
        pairs[2] = new Pair<View,String>(slogan, "slogan");
        pairs[3] = new Pair<View,String>(goBtn_signup, "go_button");
        pairs[4] = new Pair<View,String>(callLogin, "log_sign");

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,pairs);
        startActivity(intent, activityOptions.toBundle());
    }

    private Boolean validateName() {
        String val = regName.getEditText().getText().toString();
        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,15}\\z";
        if (val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() <= 3) {
            regUsername.setError("Username too short");
            return false;
        } else if (val.length() >= 15) {
            regUsername.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            regUsername.setError("White Spaces are not allowed");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = regPhone.getEditText().getText().toString();
        if (val.isEmpty()) {
            regPhone.setError("Field cannot be empty");
            return false;
        } else {
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = regPass.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
//                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if (val.isEmpty()) {
            regPass.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPass.setError("Password is too weak");
            return false;
        } else {
            regPass.setError(null);
            regPass.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateConfirmPass() {
        String val = regConfirmPass.getEditText().getText().toString();
        String pass = regPass.getEditText().getText().toString();
        if (!val.equals(pass)) {
            regConfirmPass.setError("Should match with Password");
            return false;
        } else {
            regConfirmPass.setError(null);
            regConfirmPass.setErrorEnabled(false);
            return true;
        }
    }

    private void checkExistUsersAndAdd(DatabaseReference ref, String username, UserHelperClass userHelperClass) {
        Query checkUser = ref.orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // User already exists
                    Toast.makeText(SignUpActivity.this, "User already Signed Up!", Toast.LENGTH_SHORT).show();
                } else {
                    ref.child(username).setValue(userHelperClass);
                    Toast.makeText(SignUpActivity.this, "Sign Up success! Please LogIn to continue", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(SignUpActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}