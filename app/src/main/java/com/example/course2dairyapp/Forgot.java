package com.example.course2dairyapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.course2dairyapp.databinding.ActivityForgotBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class Forgot extends AppCompatActivity {

    ActivityForgotBinding binding;
    FirebaseAuth auth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.email.getText().toString();
                resetPassword(email);
            }
        });
    }

    private void resetPassword(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Forgot.this, "Silahkan periksa email anda", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Forgot.this, "Email tidak terdaftar di sistem", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
