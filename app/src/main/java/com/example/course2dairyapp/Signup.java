package com.example.course2dairyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.course2dairyapp.databinding.ActivitySignupBinding;
import com.example.course2dairyapp.model.UserDiary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class Signup extends AppCompatActivity {

    private ActivitySignupBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    DatabaseReference reference;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(Signup.this);
//        setContentView(R.layout.activity_main);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        preferences = getSharedPreferences(DataAuth.SHARED_PREFERENCE, MODE_PRIVATE);
        editor = preferences.edit();
        dialog = new ProgressDialog(this);

        binding.signupdata.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = binding.name.getText().toString();
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                storeDataToFirebase(name ,email, password);
            }
        });

    }

    private void storeDataToFirebase(String name, String email, String password) {
        dialog.setMessage("Waiting . . .");
        dialog.show();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("Suksess", "Data berhasil di input");
                    FirebaseUser u = auth.getCurrentUser();
                    String unik = u.getUid();
                    UserDiary ud = new UserDiary(name, email, "");

                    reference.child(unik).setValue(ud).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("Sukses Database", "Sukses inserted database");
                                Toast.makeText(Signup.this, "Registrasi berhasil", Toast.LENGTH_LONG).show();

                                editor.putString(DataAuth.UNIQEU_ID, unik);
                                editor.commit();
                                finish();
                            }
                        }
                    });
                }else{
                    Log.d("Gagal", "Data gagal di input");
                    Toast.makeText(Signup.this, "Registrasi failed :"+task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();;
            }
        });
    }
}