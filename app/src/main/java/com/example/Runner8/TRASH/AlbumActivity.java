package com.example.Runner8.TRASH;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;

public class AlbumActivity extends AppCompatActivity {

    private final int GALLERY_CODE = 10;
    private FirebaseStorage storage;
    private ImageView photo;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        storage = FirebaseStorage.getInstance();
        photo = findViewById(R.id.profileImg);

        GradientDrawable drawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = (GradientDrawable) getApplicationContext().getDrawable(R.drawable.shape);
        }
        photo.setBackground(drawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            photo.setClipToOutline(true);
        }

        photo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, GALLERY_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE){
            Uri file = data.getData();
            StorageReference storageRef = storage.getReference();
            // child 안에는 이미지가 저장될 경로를 적는다.
            StorageReference riversRef = storageRef.child("photo.png");
            UploadTask uploadTask = riversRef.putFile(file);

            try{
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                photo.setImageBitmap(img);

            }catch (Exception e){
                e.printStackTrace();
            }

            uploadTask.addOnFailureListener(e -> {
                Toast.makeText(AlbumActivity.this,
                        "사진이 정상적으로 업로드 되지 않았습니다.", Toast.LENGTH_LONG).show();
            }).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(AlbumActivity.this,
                        "사진이 정상적으로 업로드 되었습니다.", Toast.LENGTH_LONG).show();
            });
        }
    }
}
