package com.example.course2dairyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.course2dairyapp.dashboard.Dashboard;
import com.example.course2dairyapp.databinding.ActivitySigninBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class Signin extends AppCompatActivity {
    private ActivitySigninBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    DatabaseReference reference;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FirebaseApp.initializeApp(Signin.this);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences(DataAuth.SHARED_PREFERENCE, MODE_PRIVATE);
        editor = preferences.edit();
        reference = FirebaseDatabase.getInstance().getReference("userDiary");
        dialog = new ProgressDialog(this);

        binding.signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                storeLoginUser(email, password);
            }
        });
        binding.signupdata.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Signin.this, Signup.class);
                startActivity(i);
            }
        });
        binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signin.this, Forgot.class));
            }
        });

    }

    private void storeLoginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    editor.putBoolean(DataAuth.AUTO_LOGIN, true);
                    editor.apply();
                    startActivity(new Intent(Signin.this, Dashboard.class));
                    Toast.makeText(Signin.this, "Sign in berhasil", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(Signin.this, "User id dan password salah", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}