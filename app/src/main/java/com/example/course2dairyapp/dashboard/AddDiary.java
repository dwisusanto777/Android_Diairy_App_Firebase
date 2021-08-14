package com.example.course2dairyapp.dashboard;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.course2dairyapp.DataAuth;
import com.example.course2dairyapp.Signup;
import com.example.course2dairyapp.databinding.AddDiaryBinding;
import com.example.course2dairyapp.model.ModelDiary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDiary extends AppCompatActivity {

    AddDiaryBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String unikId;
    String keyUnik;
    DatabaseReference reference;
    ProgressDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        binding = AddDiaryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        preferences = getSharedPreferences(DataAuth.SHARED_PREFERENCE, MODE_PRIVATE);
        unikId = preferences.getString(DataAuth.UNIQEU_ID, "");
        reference = FirebaseDatabase.getInstance().getReference(DataAuth.DB_DATA_DIARY).child(unikId);
        dialog = new ProgressDialog(AddDiary.this);

        binding.addDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Sedang mengirim data . . .");
                dialog.show();
                String title = binding.title.getText().toString();
                String desc = binding.deskripsi.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                Calendar c = Calendar.getInstance();
                String fixData = sdf.format(c.getTime());

                keyUnik = reference.push().getKey();

                ModelDiary modelDiary = new ModelDiary(title, desc, fixData, keyUnik);
                reference.child(keyUnik).setValue(modelDiary).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddDiary.this, "Data berhasil di input", Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(AddDiary.this, "Registrasi failed :"+task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
