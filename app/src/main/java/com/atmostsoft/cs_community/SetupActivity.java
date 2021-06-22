package com.atmostsoft.cs_community;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private EditText etName;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private Uri mainImageUri = null;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        circleImageView = findViewById(R.id.setupImage);
        etName = findViewById(R.id.setUserName);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.setupProgressBar);
        btnSubmit = findViewById(R.id.setupbtn);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                if (!name.isEmpty() && mainImageUri!= null)
                {
                    String user_id = firebaseAuth.getCurrentUser().getUid();
                            StorageReference image_path =storageReference.child("profile_images").child(user_id+".jpg");
                            image_path.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful())
                                    {

                                        Map<String,String> usermap = new HashMap<>();

                                        usermap.put("name",name);
                                        usermap.put("image",mainImageUri.toString());

                                        firebaseFirestore.collection("Users").document(user_id).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(SetupActivity.this, "setting updated", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(SetupActivity.this,com.atmostsoft.cs_community.MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(SetupActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        Toast.makeText(SetupActivity.this, "image uploaded", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                    else
                                    {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(SetupActivity.this, error, Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                }
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED)
                    {
                        Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    else {
                        CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                            .start(com.atmostsoft.cs_community.SetupActivity.this);
                    }


                }
                else
                {
                    bringImaePicker();
                }
            }

        });
    }


    private void bringImaePicker() {
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                circleImageView.setImageURI(mainImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}