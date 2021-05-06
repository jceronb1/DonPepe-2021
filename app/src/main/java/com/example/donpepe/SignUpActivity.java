package com.example.donpepe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    public static final Integer ADDRESS_FOUND = 49935;
    private String address ;
    private Double lat;
    private Double lon;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK && data != null){
            System.out.println("HERE");
            System.out.println(data.getStringExtra("address"));
            System.out.println(data.getDoubleExtra("lon", 99));
            System.out.println(data.getDoubleExtra("lat", 99));
            this.address = data.getStringExtra("address");
            this.lat = data.getDoubleExtra("lon", 99);
            this.lon = data.getDoubleExtra("lat", 99);
            TextView text = (TextView) findViewById(R.id.addressText);
            text.setText(this.address + " " + this.lat + "," + this.lon);
            Toast.makeText(getApplicationContext(), "Address is set to " + this.address + " " + this.lat + " " + this.lon, Toast.LENGTH_LONG);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button signButton = (Button) findViewById(R.id.signUpActionButton);
        Button login = (Button) findViewById(R.id.fromUpToLogin);
        Button searchAddrbutton = (Button) findViewById(R.id.searchAddrButton);
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailText = (EditText) findViewById(R.id.emailText);
                EditText passwordText = (EditText) findViewById(R.id.passwordText);
                EditText phoneText = (EditText) findViewById(R.id.phoneText);
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String phone = phoneText.getText().toString();
                createAccount(email, password, phone);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        searchAddrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivityForResult(intent, ADDRESS_FOUND);
            }
        });
    }

    private void createAccount(String email, String password, String phone) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //FirebaseUser user = mAuth.getCurrentUser();
                    // first save user to the database, then update profile url
                    /*
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri();
                     */
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Sign up failed. Try again", Toast.LENGTH_LONG);
                }
            }
        });
    }
}