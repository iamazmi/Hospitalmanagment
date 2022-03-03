package com.hospital.hospitalmanagment.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hospital.hospitalmanagment.R;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ImageView PatientProfilePic;
    private TextInputLayout txtinplayoutname;
    private TextInputEditText txtinpeditextname;
    private Button updateProfileBtn,ChangePatienProfilePicbtn;

    private final int PICK_IMAGE_REQUESTCODE = 555;

    private CoordinatorLayout coordinatorLayout;

    private ProgressDialog progressDialog;

    private Uri imgpathuri;
    private FirebaseStorage fireStorage;
    private StorageReference storeRef;
    private TextView patienemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        patienemail = findViewById(R.id.patienemailaddr);

        PatientProfilePic = findViewById(R.id.patienprofileimgview);
        txtinplayoutname = findViewById(R.id.editpatiennameout);
        txtinpeditextname = findViewById(R.id.editpatiennameouttxt);

        coordinatorLayout = findViewById(R.id.patientupdateprofilecoordinate);

        //btn
        updateProfileBtn = findViewById(R.id.updateprofile);
        ChangePatienProfilePicbtn = findViewById(R.id.changeprofile);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        InitializeAllFields();

        txtinpeditextname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 20){
                    txtinplayoutname.setError("Should not more then 20 character");
//
                }else {
                    txtinplayoutname.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Isnamecorrect()){
                    progressDialog.setTitle("Updating detail...");
                    progressDialog.show();
                    updateUserData();
                }
                txtinplayoutname.setEnabled(true);
            }
        });

        ChangePatienProfilePicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Profile pic from Device.."),PICK_IMAGE_REQUESTCODE);
            }
        });


    }//

    private void InitializeAllFields() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userdbref = firebaseDatabase.getReference("Patiens").child(firebaseAuth.getCurrentUser().getUid());
        userdbref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().child("Profilepic").exists()){
                        Glide.with(UserProfileActivity.this).load(task.getResult().child("Profilepic").getValue().toString())
                                .placeholder(R.drawable.patientpic).into(PatientProfilePic);
                    }
                    patienemail.setText(firebaseAuth.getCurrentUser().getEmail());
                    txtinpeditextname.setText(task.getResult().child("PaitentName").getValue().toString());
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void updateUserData() {
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference dbref = firebaseDatabase.getReference("Patiens")
                    .child(firebaseAuth.getCurrentUser().getUid());

            dbref.child("PaitentName").setValue(txtinpeditextname.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (imgpathuri != null) {
                                    fireStorage = FirebaseStorage.getInstance();
                                    storeRef = fireStorage.getReference("PatiensProfileImage");

                                    storeRef.child(firebaseAuth.getCurrentUser().getUid()).putFile(imgpathuri)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                            if (task.isSuccessful()) {
                                                                String downloadurl = Objects.requireNonNull(task.getResult()).toString();
                                                                dbref.child("Profilepic").setValue(downloadurl)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    InitializeAllFields();
                                                                                } else {
                                                                                    Snackbar.make(coordinatorLayout, "upload profilepic failed!", Snackbar.LENGTH_LONG).show();
                                                                                }
                                                                                progressDialog.dismiss();
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Snackbar.make(coordinatorLayout, "upload name" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                                                                    }
                                                                });

                                                            } else {
                                                                Snackbar.make(coordinatorLayout, "fail getting download url", Snackbar.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Snackbar.make(coordinatorLayout, "upload fail " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                                                    progressDialog.dismiss();
                                                    txtinplayoutname.setEnabled(true);
                                                }
                                            })
                                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                    double progress
                                                            = (100.0
                                                            * snapshot.getBytesTransferred()
                                                            / snapshot.getTotalByteCount());
                                                    progressDialog.setMessage(
                                                            "Uploaded "
                                                                    + (int) progress + "%");
                                                }
                                            });

                                } else {
                                    progressDialog.dismiss();
                                }
                            } else {
                                progressDialog.dismiss();
                                txtinplayoutname.setEnabled(true);
                                Snackbar.make(coordinatorLayout, "Something Went Wrong" + task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }catch (Exception e){
            Snackbar.make(coordinatorLayout,"TRy catch"+e.getMessage(),Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean Isnamecorrect()
    {
        txtinplayoutname.setEnabled(false);
        txtinplayoutname.setError("");

        boolean result = true;

        if(txtinpeditextname.getText().toString().trim().length() == 0 ){
            txtinplayoutname.setError("Name is Empty!");
            result = false;
        }
        else if(txtinpeditextname.getText().length()>20){
            txtinplayoutname.setError("Should not more then 20 character");
            result = false;
        }
        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUESTCODE
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            // Get the Uri of data
            imgpathuri = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                imgpathuri);
                PatientProfilePic.setImageBitmap(bitmap);
            }catch (IOException e) {
                // Log the exception
                Snackbar.make(coordinatorLayout, ""+e.getMessage(), Snackbar.LENGTH_LONG).show();
            }

        }

    }//

}//