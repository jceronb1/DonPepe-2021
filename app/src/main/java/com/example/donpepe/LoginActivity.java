package com.example.donpepe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.donpepe.controllers.UsersController;
import com.example.donpepe.models.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button signup = (Button) findViewById(R.id.signUpButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailText = (EditText) findViewById(R.id.emailText);
                EditText passwordText = (EditText) findViewById(R.id.passwordText);
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                signAccount(email, password);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signAccount(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    Call<ResponseBody> signInCall = UsersController.signIn(currentUser.getUid(), password);
                    signInCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            System.out.println("Sign In SUCCESS");
                            Gson gson = new Gson();
                            Session newSession = null;
                            try {
                                newSession = gson.fromJson(response.body().string(), Session.class);
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
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            System.out.println("Sign In FAILURE");
                            t.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Sign up failed. Try again", Toast.LENGTH_LONG);
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Sign up failed. Try again", Toast.LENGTH_LONG);
                }
            }
        });
    }
}