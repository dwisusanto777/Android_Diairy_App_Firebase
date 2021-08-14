package com.example.course2dairyapp.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.course2dairyapp.databinding.AdapterDairyBinding;
import com.example.course2dairyapp.databinding.EditDiaryBinding;
import com.example.course2dairyapp.model.ModelDiary;
import com.google.firebase.database.Transaction;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterDiary extends RecyclerView.Adapter<AdapterDiary.MyViewHolder> {
    Context context;
    List<ModelDiary> diaries;
    AdapterDairyBinding binding;
    String keyId = "";
    String date;
    UpdateDiary diaryInterface;

    public AdapterDiary(Context context, List<ModelDiary> diaries) {
        this.context = context;
        this.diaries = diaries;
        diaryInterface = (UpdateDiary)context;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        return new MyViewHolder(AdapterDairyBinding.inflate(layoutInflater));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterDiary.MyViewHolder holder, int position) {
        ModelDiary m = diaries.get(position);
        String title = m.getTitle();
        String decription = m.getDescription();
        String date = m.getTanggal();

        holder.binding.title.setText(title);
        holder.binding.description.setText(decription);
        holder.binding.date.setText(date);
        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDiary();
            }
        });
        holder.binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelDiary m = diaries.get(position);
                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                EditDiaryBinding binding = EditDiaryBinding.inflate(layoutInflater);
                editDiary(binding, m);
            }
        });
    }

    private void editDiary(EditDiaryBinding diaryBinding, ModelDiary diary) {
        Dialog dialog = new Dialog(context);
        View view = diaryBinding.getRoot();
        dialog.setContentView(view);
        dialog.show();

        diaryBinding.title.setText(diary.getTitle());
        diaryBinding.deskripsi.setText(diary.getDescription());
        keyId = diary.getUserId();

        diaryBinding.editDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                Calendar c = Calendar.getInstance();

                date = sdf.format(c.getTime());

                String title = diaryBinding.title.getText().toString();
                String deskripsi = diaryBinding.deskripsi.getText().toString();

                ModelDiary mdEdit = new ModelDiary(title, deskripsi, date, keyId);
                diaryInterface.updateDairy(mdEdit);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("EditBerhasil", "Edit Berhasil");
                    }
                }, null, 3000);
            }
        });
    }

    private void deleteDiary() {

    }

    @Override
    public int getItemCount() {
        return diaries.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterDairyBinding binding;

        public MyViewHolder(AdapterDairyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
