package com.example.classroomwithfacedetection.UI.Classroom;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classroomwithfacedetection.UI.Models.Post;
import com.example.classroomwithfacedetection.Untils.Constants;
import com.example.classroomwithfacedetection.databinding.ActivityAddPostBinding;


public class AddPost extends AppCompatActivity {

    private ActivityAddPostBinding binding;
    private String classID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        classID = getIntent().getStringExtra("classID");
        binding.edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0)
                    binding.create.setEnabled(true);
                else binding.create.setEnabled(false);
            }
        });
        binding.create.setOnClickListener(v -> {
            Post post = new Post(binding.edtTopic.getText().toString().trim(),binding.edtContent.getText().toString().trim());
            Constants.CLASSROOM_DB.child(classID).child("POST").child(post.getPostID()).setValue(post)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
        });
    }
}