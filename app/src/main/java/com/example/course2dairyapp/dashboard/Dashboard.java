package com.example.course2dairyapp.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.course2dairyapp.DataAuth;
import com.example.course2dairyapp.Signin;
import com.example.course2dairyapp.Signup;
import com.example.course2dairyapp.databinding.ActivityDashboardBinding;
import com.example.course2dairyapp.model.ModelDiary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements UpdateDiary{

    ActivityDashboardBinding binding;
    List<ModelDiary> diary;
    DatabaseReference reference;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LinearLayoutManager linearLayoutManager;
    AdapterDiary adapterDiary;
    ModelDiary modelDiary;
    ProgressDialog dialog;
    String unik;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        preferences = getSharedPreferences(DataAuth.SHARED_PREFERENCE, MODE_PRIVATE);
        unik = preferences.getString(DataAuth.UNIQEU_ID, "");
        reference = FirebaseDatabase.getInstance().getReference(DataAuth.DB_DATA_DIARY).child(DataAuth.UNIQEU_ID);
        diary = new ArrayList<ModelDiary>();
        linearLayoutManager = new LinearLayoutManager(this);
        dialog = new ProgressDialog(this);

        binding.rvDiary.setLayoutManager(linearLayoutManager);
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, AddDiary.class));
            }
        });

        showDiary();

    }

    private void showDiary() {
        dialog.setMessage("Please wait");
        dialog.show();
        diary.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren()){
                    modelDiary = s.getValue(ModelDiary.class);
                    diary.add(modelDiary);
                }
                adapterDiary = new AdapterDiary(Dashboard.this, diary);
                binding.rvDiary.setAdapter(adapterDiary);
                binding.noItem.setVisibility(View.GONE);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                binding.noItem.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void updateDairy(ModelDiary diary) {
        reference.child(modelDiary.getUserId()).setValue(diary).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Dashboard.this, "Data Berhasil" , Toast.LENGTH_SHORT).show();
                    showDiary();
                }else{
                    Toast.makeText(Dashboard.this, "Data Gagal" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void deleteDairy(ModelDiary diary) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showDiary();
    }
}
