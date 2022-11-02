package com.isa.sch.ly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class Profile extends AppCompatActivity
{
    //Global Variables
    private Button btn_logout;
    private Button btn_upload;
    private ImageView imageProfile;

    private  Uri imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Initializing variables
        btn_logout = findViewById(R.id.btn_logOut);
        imageProfile = findViewById(R.id.profile_img);
        btn_upload = findViewById(R.id.btn_uploadImage);


        btn_logout.setOnClickListener((View v) -> {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Profile.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });

        imageProfile.setOnClickListener(view ->
        {
            Intent photoIntent = new Intent(Intent.ACTION_PICK);
            photoIntent.setType("image/*");
            startActivityForResult(photoIntent, 1);
        });

        btn_upload.setOnClickListener(view ->
        {
            uploadImage();
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null)
        {
            imagePath = data.getData();
            getImageInImageView();
        }
    }

    private void getImageInImageView()
    {
        //Transform URI to bitmap
        Bitmap bitmap = null;
        try
        {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        imageProfile.setImageBitmap(bitmap);
    }

    private void uploadImage()
    {
        ProgressDialog  progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        //
        FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID().toString()).putFile(imagePath).addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(uri ->
                {
                    // Got the download URL for 'users/me/profile.png'
                    updateProfilePicture(uri.toString());

                }).addOnFailureListener(exception ->
                {
                    //TODO
                    // Handle any errors
                });
                Toast.makeText(Profile.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(Profile.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }).addOnProgressListener(snapshot ->
        {
            double progress = 100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
            progressDialog.setMessage("Uploading... " + (int) progress + "%");
        });
    }

    private void updateProfilePicture(String url)
    {
        FirebaseDatabase.getInstance().getReference("User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profilePicture").setValue(url);
    }
}