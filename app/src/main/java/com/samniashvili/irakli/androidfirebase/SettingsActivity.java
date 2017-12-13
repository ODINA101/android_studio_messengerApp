package com.samniashvili.irakli.androidfirebase;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
public class SettingsActivity extends AppCompatActivity {
    private static final int gallery_pick = 1;
 private DatabaseReference mUserData;
 private FirebaseUser mCurrentUser;
    //android layout
    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;
    private ProgressBar progressName;
    private Button StatusBtn;
    private Button imageBtn;
    private String currentUid;
    private StorageReference mImageStorage;
    private ProgressDialog mProgressDialog;

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = mCurrentUser.getUid();
        mUserData = FirebaseDatabase.getInstance().getReference("Users").child(currentUid);
        mDisplayImage = findViewById(R.id.circleImageView);
        mName = findViewById(R.id.settings_name);
        mStatus = findViewById(R.id.settings_status);
        progressName = findViewById(R.id.progressBar2);
        progressName.animate();
        StatusBtn = findViewById(R.id.settings_status_btn);
        mImageStorage = FirebaseStorage.getInstance().getReference();
        StatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status_val = mStatus.getText().toString();
                Intent st = new Intent(SettingsActivity.this, StatusActivity.class);
                st.putExtra("status_val", status_val);
                startActivity(st);
            }
        });
        mUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);

                Picasso.with(SettingsActivity.this).load(image).into(mDisplayImage);

                progressName.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageBtn = findViewById(R.id.settings_image_btn);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), gallery_pick);


             /*  CropImage.activity()
                       .setGuidelines(CropImageView.Guidelines.ON)
                       .start(SettingsActivity.this);*/
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == gallery_pick && resultCode == RESULT_OK) {


            Uri imageUrl = data.getData();
            CropImage.activity(imageUrl)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mProgressDialog = new ProgressDialog(SettingsActivity.this);
                mProgressDialog.setTitle("Uploading Image...");
                mProgressDialog.setMessage("Please wait while we upload and process the image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                String current_user_id = mCurrentUser.getUid();
                Uri resultUri = result.getUri();
                StorageReference filepath = mImageStorage.child("profile_image").child(current_user_id + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            String downloadURL = task.getResult().getDownloadUrl().toString();
                            mUserData.child("image").setValue(downloadURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mProgressDialog.dismiss();
                                        Toast.makeText(SettingsActivity.this, "profile has been updated", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SettingsActivity.this, "Error in uploading", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
