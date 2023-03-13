package com.example.classroomwithfacedetection.UI.PROFILE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.classroomwithfacedetection.R;
import com.example.classroomwithfacedetection.SpLash;
import com.example.classroomwithfacedetection.UI.AddFaceData.AddFaceData;
import com.example.classroomwithfacedetection.UI.Login.Login;
import com.example.classroomwithfacedetection.UI.Models.User;
import com.example.classroomwithfacedetection.UI.SIGNUP.Signup;
import com.example.classroomwithfacedetection.Untils.Constants;


public class PROFILE extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        unitUI();
        clickEvent();
        button =(Button) findViewById(R.id.Inforuser);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PROFILE.this, UserInfor.class);
                intent.putExtra("id",Constants.AUTH.getCurrentUser().getUid());
                startActivity(intent);
            }
        });
    }

    private void unitUI() {
        Constants.USER_DB.child(Constants.AUTH.getCurrentUser().getUid()).get()
                .addOnSuccessListener(dataSnapshot -> {
                    User user = dataSnapshot.getValue(User.class);
                    if (user!=null){
                        binding.name.setText(user.getTen());
                    }
                });
    }

    private void clickEvent() {
        binding.logout.setOnClickListener(v -> {
            Constants.AUTH.signOut();
            startActivity(new Intent(this, SpLash.class));
            finishAffinity();
        });
        binding.addface.setOnClickListener(v -> {
            startActivity(new Intent(this, AddFaceData.class));
        });



    }


}
