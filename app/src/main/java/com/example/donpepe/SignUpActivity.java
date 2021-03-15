package com.example.donpepe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button signButton = (Button) findViewById(R.id.signUpActionButton);
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailText = (EditText) findViewById(R.id.emailText);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("loggedEmail", emailText.getText());
                intent.putExtra("loggedIn", "true");
                startActivity(intent);
            }
        });
    }
}