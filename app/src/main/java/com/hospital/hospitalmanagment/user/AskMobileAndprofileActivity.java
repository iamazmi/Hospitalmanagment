package com.hospital.hospitalmanagment.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.loginsignup.SignupActivity;
import com.hospital.hospitalmanagment.loginsignup.UserOptionActivity;

import java.io.IOException;
import java.util.Objects;

public class AskMobileAndprofileActivity extends AppCompatActivity {

    private TextInputLayout txtinplayoutphone;
    private TextInputEditText txtinpeditextphone;

    private Button UpdatePicNo;
    private CardView selectImg;
    private ImageView ProfilePic;

    private Uri imgpathuri;
    private final int PICK_IMAGE_REQUESTCODE = 70;
    private CoordinatorLayout coordinatorLayout;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private FirebaseStorage fireStorage;
    private StorageReference storeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_mobile_andprofile);

        txtinplayoutphone = findViewById(R.id.phonelayout);
        txtinpeditextphone = findViewById(R.id.phonelayouttxt);

        UpdatePicNo = findViewById(R.id.updatepicno);
        selectImg = findViewById(R.id.selectnewpic);
        ProfilePic = findViewById(R.id.profileimgview);
        coordinatorLayout = findViewById(R.id.picandnocoordinate);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();



        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg.startAnimation(AnimationUtils.loadAnimation(AskMobileAndprofileActivity.this,R.anim.clickeffect));
                selectImageMethod();

            }
        });

        UpdatePicNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(AskMobileAndprofileActivity.this,UserHomeActivity.class));
//                Toast.makeText(SignupActivity.this," "+txtinpeditextname.getText(),Toast.LENGTH_LONG).show();

                if(isAllDataMeetRequirment()){
                    progressDialog.setMessage("Updating Details...");
                    progressDialog.show();
//                    Toast.makeText(AskMobileAndprofileActivity.this,"all is good",Toast.LENGTH_LONG).show();
                    uploadPicUpdateNoPatient();
                }else{
//                    Toast.makeText(AskMobileAndprofileActivity.this,"Wrong",Toast.LENGTH_LONG).show();
                    txtinplayoutphone.setEnabled(true);
                }
            }
        });



    }//

    private void uploadPicUpdateNoPatient() {
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference dbref = firebaseDatabase.getReference("Patiens")
                    .child(firebaseAuth.getCurrentUser().getUid());

            dbref.child("Phone").setValue(txtinpeditextphone.getText().toString())
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
                                                                                    startActivity(new Intent(AskMobileAndprofileActivity.this, UserHomeActivity.class));
                                                                                    finish();
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
                                                    txtinplayoutphone.setEnabled(true);
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
                                    startActivity(new Intent(AskMobileAndprofileActivity.this, UserHomeActivity.class));
                                    finish();
                                    progressDialog.dismiss();
                                }
                            } else {
                                progressDialog.dismiss();
                                txtinplayoutphone.setEnabled(true);
                                Snackbar.make(coordinatorLayout, "Something Went Wrong" + task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }catch (Exception e){
            Snackbar.make(coordinatorLayout,"TRy catch"+e.getMessage(),Snackbar.LENGTH_LONG).show();
        }
    }

    private void selectImageMethod() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile pic from Device.."),PICK_IMAGE_REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                ProfilePic.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }

        }

    }//

    private boolean isAllDataMeetRequirment() {
        txtinplayoutphone.setEnabled(false);
        txtinplayoutphone.setError("");

        boolean result = true;

        if(txtinpeditextphone.getText().toString().trim().length() == 0 ){
            txtinplayoutphone.setError("phone No is Empty!");
            result = false;
        }
        else if(txtinpeditextphone.getText().length() != 10){
            txtinplayoutphone.setError("invalid phone no!");
            result = false;
        }
        return  result;

    }

}//