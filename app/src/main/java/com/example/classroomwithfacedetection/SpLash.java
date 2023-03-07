package com.example.classroomwithfacedetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.classroomwithfacedetection.UI.HOME.Home;
import com.example.classroomwithfacedetection.UI.Login.Login;
import com.google.android.gms.common.internal.Constants;

public class SpLash extends AppCompatActivity {

    private static final long DELAY_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_lash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Constants.AUTH.getCurrentUser()==null){
                    startActivity(new Intent(SpLash.this, Login.class));
                    finishAffinity();
                }else {
                    startActivity(new Intent(SpLash.this, Home.class));
                    finishAffinity();
                }
            }
        },DELAY_TIME);
    }
}