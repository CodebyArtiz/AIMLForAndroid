package com.example.classroomwithfacedetection.UI.HOME.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.classroomwithfacedetection.UI.Models.Classroom;
import com.example.classroomwithfacedetection.Untils.Constants;
import com.example.classroomwithfacedetection.databinding.ItemsClassBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class CLassroomAdapter extends RecyclerView.Adapter<CLassroomAdapter.ItemHolder> {
    private ItemsClassBinding binding;
    private ArrayList<Classroom> classrooms;
    private ItemOclick itemOclick;

    public CLassroomAdapter(ArrayList<Classroom> classrooms) {this.classrooms = classrooms;}

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        binding = ItemsClassBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ItemHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ItemHolder holder, int position) {
        Classroom classroom = classrooms.get(position);
        binding.ten.setText(classroom.getTen());
        binding.phan.setText(classroom.getPhan());
        binding.phong.setText(classroom.getPhong().isEmpty() ? "" : "Phòng: "+classroom.getPhong());
        Constants.USER_DB.child(classroom.getUid_nguoiTao()).child("ten").get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        binding.gv.setText(dataSnapshot.getValue(String.class));
                    }
                });
    }
    public void setItemOnClick(ItemOclick itemOclick){
        this.itemOclick = itemOclick;
    }

    @Override
    public int getItemCount() {
        return classrooms.size();
    }


    protected class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ItemHolder(@androidx.annotation.NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            itemOclick.onCick(classrooms.get(getPosition()));
        }
    }

    public interface ItemOclick{
        public void onCick(Classroom classroom);
    }
}
