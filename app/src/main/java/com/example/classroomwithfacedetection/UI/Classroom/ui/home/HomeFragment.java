package com.example.classroomwithfacedetection.UI.Classroom.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.classroomwithfacedetection.UI.Classroom.ClassroomDetail;
import com.example.classroomwithfacedetection.UI.Models.Post;
import com.example.classroomwithfacedetection.Untils.Constants;
import com.example.classroomwithfacedetection.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private String classID;
    private ArrayList<Post> posts;
    private PostAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        ClassroomDetail a = (ClassroomDetail) getActivity();
        classID = a.getClassID();
        posts = new ArrayList<>();
        adapter = new PostAdapter(getContext(),posts);
        binding.rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcv.setAdapter(adapter);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        posts.clear();
        Constants.CLASSROOM_DB.child(classID).child("POST").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isComplete()){
                            DataSnapshot dataSnapshot = task.getResult();
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                posts.add(data.getValue(Post.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}