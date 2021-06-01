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
import android.widget.Toast;

import com.example.donpepe.controllers.ProductsController;
import com.example.donpepe.helpers.AskPermission;
import com.example.donpepe.models.Product;
import com.example.donpepe.serializers.NewProductSerializer;
import com.example.donpepe.services.ProductsService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Time;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellActivity extends AppCompatActivity {

    static final int IMAGE_PICKER_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private Bitmap productBitmap;
    private String category;
    private String token;
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        currentUser = mAuth.getCurrentUser();
        SharedPreferences sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
        token = sp.getString("token", "");
        category = getIntent().getStringExtra("selectedCategory");
        AskPermission.ask(
                this, Manifest.permission.CAMERA, "", REQUEST_IMAGE_CAPTURE
        );

        Button cameraButton = (Button) findViewById(R.id.sellCamButton);
        Button filesButton = (Button) findViewById(R.id.sellFilesButton);

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

        Button createButton = (Button) findViewById(R.id.createProduct);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newNameText = (EditText) findViewById(R.id.newNameText);
                EditText newDescText = (EditText) findViewById(R.id.newDescriptionText);
                EditText newPriceText = (EditText) findViewById(R.id.newPriceText);
                Product newProduct = new Product();
                newProduct.setName(newNameText.getText().toString());
                newProduct.setDescription(newDescText.getText().toString());
                newProduct.setPrice(Integer.parseInt(newPriceText.getText().toString()) );
                StorageReference storageRef =  mStorage.getReference(currentUser.getUid() + "/products").child(String.valueOf(Calendar.getInstance().getTime()));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                productBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                UploadTask uploadTask = storageRef.putBytes(baos.toByteArray());
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newProduct.setImage(uri.toString());
                                Call<ResponseBody> callCreate = ProductsController.create(newProduct, category, token);
                                callCreate.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                        });
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = (ImageView) findViewById(R.id.newProductImage);
        switch (requestCode) {
            case 1:{
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                        productBitmap = selectedImage;
                        image.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            case 2: {
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    productBitmap = imageBitmap;
                    image.setImageBitmap(imageBitmap);
                }
            }
        }
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