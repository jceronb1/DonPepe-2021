package com.example.donpepe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.donpepe.controllers.UsersController;
import com.example.donpepe.helpers.AskPermission;
import com.example.donpepe.models.Seller;
import com.example.donpepe.models.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    private String address ;
    private Double lat;
    private Double lon;
    private Bitmap mBitmap;
    private String imageUrl;
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
            this.lon = data.getDoubleExtra("lon", 99);
            this.lat = data.getDoubleExtra("lat", 99);
            TextView text = (TextView) findViewById(R.id.addressText);
            text.setText(this.address + " " + this.lat + "," + this.lon);
            Toast.makeText(getApplicationContext(), "Address is set to " + this.address + " " + this.lat + " " + this.lon, Toast.LENGTH_LONG).show();
        }
        if(requestCode == IMAGE_PICKER_REQUEST && resultCode == RESULT_OK){
            ImageView image = (ImageView) findViewById(R.id.userimg);
            try {
                final Uri imageUri = data.getData();
                final InputStream inputStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                mBitmap = selectedImage;
                image.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            ImageView image = (ImageView) findViewById(R.id.userimg);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mBitmap = imageBitmap;
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
                EditText passwordConfirmationText = (EditText) findViewById(R.id.passwordConfirmationText);
                EditText phoneText = (EditText) findViewById(R.id.phoneText);
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String passwordConfirmation = passwordConfirmationText.getText().toString();
                String phone = phoneText.getText().toString();
                createAccount(email, password, passwordConfirmation, phone);
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

    private void createAccount(String email, String password, String passwordConfirmation, String phone) {
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
        if(passwordConfirmation.isEmpty()){
            EditText passwordConfirmationText = (EditText) findViewById(R.id.passwordConfirmationText);
            passwordConfirmationText.setError("password confirmation is required. wtf are you doing");
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

        if(mBitmap == null){
            Toast.makeText(getApplicationContext(), "Please select a profile image ", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    StorageReference myStorageRef = mStorage.getReference().child(currentUser.getUid());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    UploadTask uploadTask = myStorageRef.putBytes(baos.toByteArray());
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri.toString();
                                        Seller newSeller = new Seller(
                                                currentUser.getUid(),
                                                email,
                                                password,
                                                passwordConfirmation,
                                                phone,
                                                address,
                                                lat,
                                                lon,
                                                imageUrl
                                        );
                                        System.out.println("SIGN UP LAT");
                                        System.out.println( newSeller.getLat());
                                        System.out.println("SIGN UP LON");
                                        System.out.println( newSeller.getLon());
                                        Call<ResponseBody> signUpCall =  UsersController.signUp(newSeller);
                                        signUpCall.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                System.out.println("Signed up SUCCESS");
                                                System.out.println(response.body());
                                                Gson gson = new Gson();
                                                try {
                                                    Session newSession = gson.fromJson(response.body().string(), Session.class);
                                                    SharedPreferences sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
                                                    SharedPreferences.Editor spe = sp.edit();
                                                    spe.putString("token", newSession.getToken());
                                                    spe.commit();
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                                System.out.println("Signed up FAILED");
                                                throwable.printStackTrace();
                                                Toast.makeText(getApplicationContext(), "Sign up failed. Try again", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            }else{
                                System.out.println("Image upload failed");
                                Toast.makeText(getApplicationContext(), "Failed to upload photo, please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Sign up failed. Try again", Toast.LENGTH_LONG).show();
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