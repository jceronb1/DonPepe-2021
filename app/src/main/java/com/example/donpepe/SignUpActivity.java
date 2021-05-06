package com.example.donpepe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donpepe.helpers.AskPermission;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;

    private String address ;
    private Double lat;
    private Double lon;
    public static final int IMAGE_PICKER_REQUEST = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;
    public static final int ADDRESS_FOUND = 49935;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADDRESS_FOUND && resultCode == RESULT_OK && data != null){
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
        if(requestCode == IMAGE_PICKER_REQUEST && resultCode == RESULT_OK){
            ImageView image = (ImageView) findViewById(R.id.userimg);
            try {
                final Uri imageUri = data.getData();
                final InputStream inputStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                    image.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            ImageView image = (ImageView) findViewById(R.id.userimg);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        AskPermission.ask(
                this, Manifest.permission.CAMERA, "", REQUEST_IMAGE_CAPTURE
        );

        Button signButton = (Button) findViewById(R.id.signUpActionButton);
        Button login = (Button) findViewById(R.id.fromUpToLogin);
        Button searchAddrbutton = (Button) findViewById(R.id.searchAddrButton);
        Button cameraButton = (Button) findViewById(R.id.signUpCamButton);
        Button filesButton = (Button) findViewById(R.id.signUpFilesButton);

        filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/**");
                startActivityForResult(intent,IMAGE_PICKER_REQUEST);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });

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
        if(email.isEmpty()){
            EditText emailText = (EditText) findViewById(R.id.emailText);
            emailText.setError("email is required");
            return;
        }
        if(password.isEmpty()){
            EditText passwordText = (EditText) findViewById(R.id.passwordText);
            passwordText.setError("password is required. wtf are you doing");
            return;
        }
        if(phone.isEmpty()){
            EditText phoneText = (EditText) findViewById(R.id.phoneText);
            phoneText.setError("phone is required.");
            return;
        }
        if(address.isEmpty()){
            TextView addressText = (TextView) findViewById(R.id.addressText);
            addressText.setText("Address is required");
            return;
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permiso Denegado", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}