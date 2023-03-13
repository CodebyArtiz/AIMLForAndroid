import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.classroomwithfacedetection.Untils.Constants;
import com.example.classroomwithfacedetection.Untils.FaceModel;
import com.example.classroomwithfacedetection.Untils.Untils;
import com.example.classroomwithfacedetection.databinding.ActivityAddFaceDataBinding;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.IOException;
import java.nio.MappedByteBuffer;

public class AddFaceData extends AppCompatActivity {

    private ActivityAddFaceDataBinding binding;
    private FaceDetector faceDetector;
    private Bitmap imageRedult;
    private final String PATH = "mobile_face_net.tflite";
    private MappedByteBuffer file;
    private final int MY_CAMERA_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        faceDetector = FaceDetection.getClient(new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build());

        binding = ActivityAddFaceDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            file = Untils.loadModelFile(AddFaceData.this,PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            binding.button.setEnabled(true);
        }else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            binding.button.setEnabled(false);
        }

        binding.button.setOnClickListener(v->{
            startActivityIfNeeded(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),1);
        });

        binding.add.setOnClickListener(v -> {

            // kiểm tra
            if (imageRedult!=null){
                faceDetector.process(InputImage.fromBitmap(imageRedult,0))
                        .addOnSuccessListener(faces -> {
                            if (faces.size()>0){
                                Bitmap bitmap = Bitmap.createBitmap(imageRedult);
                                FaceModel faceNetModel = new FaceModel(AddFaceData.this);
                                float[] em = faceNetModel.getFaceEmbedding(imageRedult,faces.get(0).getBoundingBox(),false);
                                binding.faceView.setImageBitmap(faceNetModel.cropRectFromBitmap(bitmap,faces.get(0).getBoundingBox(),false));
                                Constants.USER_DB.child(Constants.AUTH.getCurrentUser().getUid()).child("face_data").setValue(Untils.toArray(em));
                            }else {
                                Toast.makeText(AddFaceData.this, "Không tìm thấy khuôn mặt", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {

                        })
                        .addOnCompleteListener(task -> {

                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            Bundle extras = data.getExtras();
            imageRedult = (Bitmap) extras.get("data");
            binding.image.setImageBitmap(imageRedult);
            binding.add.setEnabled(true);
        }else {
            Toast.makeText(this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.button.setEnabled(true);
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                binding.button.setEnabled(false);
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}