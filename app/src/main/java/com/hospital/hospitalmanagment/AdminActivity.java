package com.hospital.hospitalmanagment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hospital.hospitalmanagment.loginsignup.SignupActivity;
import com.hospital.hospitalmanagment.loginsignup.UserOptionActivity;
import com.hospital.hospitalmanagment.user.AskMobileAndprofileActivity;
import com.hospital.hospitalmanagment.user.UserHomeActivity;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class AdminActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private FirebaseAuth firebaseAuth;
    private ImageView DoctorProfilePic;

    private TextInputLayout txtinplayoutname;
    private TextInputEditText txtinpeditextname;

    private TextInputLayout txtinplayoutemail;
    private TextInputEditText txtinpeditextemail;

    private TextInputLayout txtinplayoutpass;
    private TextInputEditText txtinpeditextpass;

    private TextInputLayout txtinplayoutdocc;
    private TextInputEditText txtinpeditextdocc;

    private Button AddDoctorBtn,ChangeDoctorProfilePicbtn;

    private final int PICK_IMAGE_REQUESTCODE = 544;

    private CoordinatorLayout coordinatorLayout;

    private ProgressDialog progressDialog;

    private Uri imgpathuri;
    private FirebaseStorage fireStorage;
    private StorageReference storeRef;

    private  String adminEmail;
    private  String adminPassword;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        DoctorProfilePic = findViewById(R.id.doctorImage);

        firebaseAuth = FirebaseAuth.getInstance();
        materialToolbar = findViewById(R.id.admintoolbar);

        txtinplayoutname = findViewById(R.id.dname);
        txtinpeditextname = findViewById(R.id.dnametxt);

        txtinplayoutemail = findViewById(R.id.demail);
        txtinpeditextemail = findViewById(R.id.demailtxt);

        txtinplayoutpass = findViewById(R.id.dpass);
        txtinpeditextpass = findViewById(R.id.dpasstxt);

        txtinplayoutdocc = findViewById(R.id.docc);
        txtinpeditextdocc = findViewById(R.id.docctxt);

        coordinatorLayout = findViewById(R.id.admincoordinate);

        AddDoctorBtn = findViewById(R.id.addDcotor);
        ChangeDoctorProfilePicbtn = findViewById(R.id.addprofile);

        sharedPreferences = getSharedPreferences("adminSharedPref",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if((getIntent().getStringExtra("adminEmail") != null) && (getIntent().getStringExtra("adminPass") != null)){
           this.adminEmail = getIntent().getStringExtra("adminEmail");
            this.adminPassword = getIntent().getStringExtra("adminPass");
            editor.putString("adminEmail",getIntent().getStringExtra("adminEmail"));
            editor.putString("adlock",getIntent().getStringExtra("adminPass"));
            editor.commit();
        }else {
                this.adminEmail = sharedPreferences.getString("adminEmail","");
               this.adminPassword = sharedPreferences.getString("adlock","");
             }


        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

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


        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.adminlogout:

                        if(firebaseAuth.getCurrentUser() != null){
                            firebaseAuth.signOut();
                            editor.remove("adminEmail");
                            editor.remove("adlock");
                            editor.commit();
//                            Log.i("uid", "inside logout button: current user uid "+firebaseAuth.getCurrentUser().getUid());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(AdminActivity.this, UserOptionActivity.class));
                                    finish();
                                }
                            },4000);

                        }
//                        startActivity(new Intent(AdminActivity.this, UserOptionActivity.class));
//                        finish();
                        break;
                    case R.id.adminFAQ:
                        startActivity(new Intent(AdminActivity.this, AddFaqActivity.class));
                        break;
                    case R.id.adminaddhospital:
                        startActivity(new Intent(AdminActivity.this, AddHospitalActivity.class));
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });


        AddDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllDataMeetRequirment()){
                    progressDialog.setTitle("Creating Account...");
                    progressDialog.show();
                    nowCreateDoctorAccount();
//                    Log.i("uid", "onComplete of outside of callbacks: current user uid "+firebaseAuth.getCurrentUser().getUid());
                }
                txtinplayoutname.setEnabled(true);
                txtinplayoutemail.setEnabled(true);
                txtinplayoutpass.setEnabled(true);
                txtinplayoutdocc.setEnabled(true);
            }
        });

        ChangeDoctorProfilePicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Profile pic from Device.."),PICK_IMAGE_REQUESTCODE);
            }
        });

    }//

    private void loginAgaintoAdmin(){
        firebaseAuth.signOut();
        firebaseAuth.signInWithEmailAndPassword(this.adminEmail,this.adminPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Snackbar.make(coordinatorLayout, "Admin Rereshed.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void nowCreateDoctorAccount() {
        final String userEmail = txtinpeditextemail.getText().toString().trim();
        final String userPass = txtinpeditextpass.getText().toString().trim();
        final String userName = txtinpeditextname.getText().toString().trim();
        final String userSpec = txtinpeditextdocc.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    DatabaseReference dbdcotorref = FirebaseDatabase.getInstance().getReference("Doctors").child(task.getResult().getUser().getUid());
                    dbdcotorref.child("DoctorName").setValue(userName);
                    dbdcotorref.child("Special").setValue(userSpec);
                    dbdcotorref.child("uid").setValue(firebaseAuth.getCurrentUser().getUid());

                    if (imgpathuri != null) {
                        fireStorage = FirebaseStorage.getInstance();
                        storeRef = fireStorage.getReference("DoctorsProfileImage");

                        storeRef.child(firebaseAuth.getCurrentUser().getUid()).putFile(imgpathuri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {
                                                    String downloadurl = Objects.requireNonNull(task.getResult()).toString();
                                                    dbdcotorref.child("Profilepic").setValue(downloadurl);
                                                    loginAgaintoAdmin();
                                                } else {
                                                    Snackbar.make(coordinatorLayout, "fail getting download url", Snackbar.LENGTH_LONG).show();
                                                }
                                                progressDialog.dismiss();
                                                clearUriAndFields();
//                                                Log.i("uid", "onComplete of uripicupload: current user uid "+firebaseAuth.getCurrentUser().getUid());
//                                                Snackbar.make(coordinatorLayout, "update current user Uid is "+firebaseAuth.getCurrentUser().getUid(), Snackbar.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(coordinatorLayout, "upload fail " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                                        progressDialog.dismiss();
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
                        loginAgaintoAdmin();
                        clearUriAndFields();
                        progressDialog.dismiss();
//                        Log.i("uid", "onComplete of uripic not upload: current user uid "+firebaseAuth.getCurrentUser().getUid());
//                        Snackbar.make(coordinatorLayout, "update current user Uid is "+firebaseAuth.getCurrentUser().getUid(), Snackbar.LENGTH_LONG).show();

                    }
                }else{
                    Snackbar.make(coordinatorLayout, "Unable to create Account "+task.getException().getMessage(), Snackbar.LENGTH_LONG).show();

                }

            }
        });


    }

    private void clearUriAndFields(){
        txtinpeditextname.setText("");
        txtinpeditextdocc.setText("");
        txtinpeditextemail.setText("");
        txtinpeditextpass.setText("");
        if(imgpathuri != null){
            imgpathuri = null;
            DoctorProfilePic.setImageDrawable(getDrawable(R.drawable.doctor));
        }

    }


    private boolean isAllDataMeetRequirment() {

        txtinplayoutname.setEnabled(false);
        txtinplayoutemail.setEnabled(false);
        txtinplayoutpass.setEnabled(false);
        txtinplayoutdocc.setEnabled(false);

        txtinplayoutname.setError("");
        txtinplayoutemail.setError("");
        txtinplayoutpass.setError("");
        txtinplayoutdocc.setError("");

        boolean result = true;

        if(txtinpeditextname.getText().toString().trim().length() == 0 ){
            txtinplayoutname.setError("Name is Empty!");
            result = false;
        }
        else if(txtinpeditextname.getText().length()>20){
            txtinplayoutname.setError("Should not more then 20 character");
            result = false;
        }

        if(txtinpeditextdocc.getText().toString().trim().length() == 0 ){
            txtinplayoutdocc.setError("specialization  is Empty!");
            result = false;
        }else if(txtinpeditextdocc.getText().length()>20){
            txtinplayoutdocc.setError("Should not more then 20 character");
            result = false;
        }

        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if(txtinpeditextemail.getText().toString().trim().length() == 0){

            txtinplayoutemail.setError("Email is Empty!.");
            result = false;
        }
        else if(!(Pattern.compile(regex).matcher(txtinpeditextemail.getText()).matches())){
            txtinplayoutemail.setError("Invalid email address.");
            result = false;
        }

        if(txtinpeditextpass.getText().toString().trim().length() == 0)
        {
            txtinplayoutpass.setError("Password is Empty!.");
            result = false;
        }
        else if(txtinpeditextpass.getText().toString().trim().length() < 8){
            txtinplayoutpass.setError("Password Should 8 char long!.");
            result = false;
        }
        else if(!txtinpeditextpass.getText().chars().anyMatch(Character::isUpperCase))
        {
            txtinplayoutpass.setError("should contain at least one capital letter.");
            result = false;
        }
        else if(!( txtinpeditextpass.getText().toString().contains("@") || txtinpeditextpass.getText().toString().contains("#") || txtinpeditextpass.getText().toString().contains("%")))
        {
            txtinplayoutpass.setError("should contain any of them (@ # %).");
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
                DoctorProfilePic.setImageBitmap(bitmap);
            }catch (IOException e) {
                // Log the exception
                Snackbar.make(coordinatorLayout, ""+e.getMessage(), Snackbar.LENGTH_LONG).show();
            }

        }

    }//

}//