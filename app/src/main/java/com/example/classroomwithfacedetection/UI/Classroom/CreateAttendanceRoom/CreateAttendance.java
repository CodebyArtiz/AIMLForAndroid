package com.example.classroomwithfacedetection.UI.Classroom.CreateAttendanceRoom;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Messenger;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classroomwithfacedetection.R;
import com.example.classroomwithfacedetection.UI.Models.AttendanceRoom;
import com.example.classroomwithfacedetection.UI.Models.Classroom;
import com.example.classroomwithfacedetection.Untils.Constants;
import com.example.classroomwithfacedetection.databinding.ActivityCreateAttendanceBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;

import java.util.Date;

public class CreateAttendance extends AppCompatActivity {

    private ActivityCreateAttendanceBinding binding;
    private String classID;
    private Classroom classr;
    private boolean clicked = false;
    Messenger msgService;
    boolean isBound;
    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        classID = getIntent().getStringExtra("ClassID");
        unitUI();
        clickEvent();

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                isBound = true;
                msgService = new Messenger(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
            }
        };

    }

    private void clickEvent() {
        binding.bt.setOnClickListener(v -> {
            if (!clicked){
                binding.bt.setText(getString(R.string.stop));
                startAtten();
                Toast.makeText(this, "Bắt đầu điểm danh", Toast.LENGTH_SHORT).show();
                clicked = true;
            }else {
                binding.bt.setText(getString(R.string.start));
                binding.bt.setEnabled(false);
                countDownTimer.onFinish();
                countDownTimer.cancel();
                clicked = false;
            }
        });

        binding.closeActi.setOnClickListener(v -> {
            countDownTimer.onFinish();
            countDownTimer.cancel();
            finish();
        });

    }

    private void startAtten() {
        binding.time.setEnabled(false);
        String time = binding.time.getText().toString().trim();
        AttendanceRoom attendanceRoom = new AttendanceRoom(new Date().toString(),classr.getTen(),time);
        countDownTimer = new CountDownTimer(Integer.parseInt(time)*60*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                String times = String.valueOf(millisUntilFinished/60000)+":"+String.format("%d",(millisUntilFinished%60000)/1000);
                Constants.CLASSROOM_DB.child(classID).child(Constants.ATTEN_ROOM).child("time").setValue(times);
                binding.time.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.time.setText(times);
                    }
                });
            }
            public void onFinish() {
                Constants.CLASSROOM_DB.child(classID).child(Constants.ATTEN_ROOM).removeValue();
                binding.bt.setText(getString(R.string.start));
            }
        };
        Constants.CLASSROOM_DB.child(classID).child(Constants.ATTENDANCED).child(attendanceRoom.getNgay()).setValue(attendanceRoom);
        Constants.CLASSROOM_DB.child(classID).child(Constants.ATTEN_ROOM).setValue(attendanceRoom)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        countDownTimer.start();
                    }
                });
    }


    private void unitUI() {
//        DbUntils.getClassroom(classID, new DataRecive.Classroom.One() {
//            @Override
//            public void Classroom(Classroom classroom) {
//                classr = classroom;
//                binding.className.setVisibility(View.VISIBLE);
//                binding.className.setText(classroom.getTen());
//                binding.bt.setEnabled(true);
//            }
//        });

        Constants.CLASSROOM_DB.child(classID).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Classroom classroom = dataSnapshot.getValue(Classroom.class);
                        if (classroom!=null){
                            classr = classroom;
                            binding.className.setVisibility(View.VISIBLE);
                            binding.className.setText(classroom.getTen());
                            binding.bt.setEnabled(true);
                        }
                    }
                });


    }
}