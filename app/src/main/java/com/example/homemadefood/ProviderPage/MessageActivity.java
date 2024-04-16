package com.example.homemadefood.ProviderPage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.homemadefood.R;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        // Add any additional setup for the message activity here

       TextView userTextView = findViewById(R.id.userTextView);
        String username = getIntent().getStringExtra("USERNAME");
        String message = "To " + username + ": ";
        userTextView.setText(message);


        Button backBtn = findViewById(R.id.backBtn);
        Button resetBtn = findViewById(R.id.resetBtn);
        EditText msgBox = findViewById(R.id.msgBox);

        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                msgBox.setText("");


            }

    } );


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
