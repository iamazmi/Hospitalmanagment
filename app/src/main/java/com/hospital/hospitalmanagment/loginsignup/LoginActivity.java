package com.hospital.hospitalmanagment.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.hospital.hospitalmanagment.AdminActivity;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.doctor.DoctorActivity;
import com.hospital.hospitalmanagment.user.AskMobileAndprofileActivity;
import com.hospital.hospitalmanagment.user.UserHomeActivity;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private TextView txtvRedirectsignup;
    private TextView txtvRedirectForgetPass;

    private TextInputLayout txtinplayoutemail;
    private TextInputEditText txtinpeditextemail;

    private TextInputLayout txtinplayoutpass;
    private TextInputEditText txtinpeditextpass;

    private Button LogibBtn;

    private String UserType;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtvRedirectsignup = findViewById(R.id.txtnewaccount);
        txtvRedirectForgetPass = findViewById(R.id.txtforgetpass);

        txtinplayoutemail = findViewById(R.id.loginemail);
        txtinpeditextemail = findViewById(R.id.loginemailtxt);

        txtinplayoutpass = findViewById(R.id.loginpassword);
        txtinpeditextpass = findViewById(R.id.loginpasswordtxt);

        coordinatorLayout = findViewById(R.id.logincoordinate);

        LogibBtn = findViewById(R.id.loginbtn);

        progressDialog = new ProgressDialog(this);

        try {
            this.UserType = getIntent().getStringExtra("loginType");
            if(!this.UserType.equals("Patient")){
                txtvRedirectsignup.setVisibility(View.INVISIBLE);
//                txtvRedirectForgetPass.setVisibility(View.INVISIBLE);
            }
//            Toast.makeText(LoginActivity.this, "UserType = " + this.UserType, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(LoginActivity.this, "UserType error = " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        txtvRedirectForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
            }
        });
        txtvRedirectsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
//                finish();
            }
        });

        LogibBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
                if(isAllDataMeetRequirment()){
                    progressDialog.setTitle("Login Account...");
                    progressDialog.show();
                    firebaseAuth = FirebaseAuth.getInstance();
                    singInUserToAccount();
//                    Toast.makeText(LoginActivity.this,"all is good",Toast.LENGTH_LONG).show();
                }else{
//                    Toast.makeText(LoginActivity.this,"Wrong",Toast.LENGTH_LONG).show();
                    txtinplayoutemail.setEnabled(true);
                    txtinplayoutpass.setEnabled(true);
                }

            }
        });

    }//

    private void singInUserToAccount() {
        final String userEmail = txtinpeditextemail.getText().toString().trim();
        final String userPass = txtinpeditextpass.getText().toString().trim();

        this.firebaseAuth.signInWithEmailAndPassword(userEmail,userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful())
                        {
                            if(UserType.equals("Docter")){
                                //check login one is admin ?
                                FirebaseDatabase.getInstance().getReference("Doctors").
                                        child(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(task.isSuccessful()){
                                            if(task.getResult().child("Admin").exists()){

                                                startActivity(new Intent(LoginActivity.this, AdminActivity.class)
                                                        .putExtra("adminEmail",userEmail).putExtra("adminPass",userPass));
                                                finish();
                                            }else if(task.getResult().child("DoctorName").exists()){
                                                startActivity(new Intent(LoginActivity.this, DoctorActivity.class));
                                                finish();
                                            }else {
                                                firebaseAuth.signOut();
//                                                Snackbar.make(coordinatorLayout,"inValid User Account!!",Snackbar.LENGTH_LONG).show();
                                                Toast.makeText(LoginActivity.this,"Invalid Login User!!",Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(LoginActivity.this, UserOptionActivity.class));
                                                finish();
                                            }
                                        }else{
                                            txtinplayoutemail.setEnabled(true);
                                            txtinplayoutpass.setEnabled(true);
                                        }
                                        progressDialog.dismiss();
                                    }
                                });
//                                firebaseAuth.signOut();
//                                Snackbar.make(coordinatorLayout,"wait doctor pages is under construction",Snackbar.LENGTH_LONG).show();
                            }else{
                                // ye doctor me check krna baad me
                                //
                                if(firebaseAuth.getCurrentUser().isEmailVerified())
                                {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            //here we have to check user has mobile no or not then according to redirect check letar
                                            FirebaseDatabase.getInstance().getReference("Patiens").
                                                    child(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    if(task.isSuccessful()){

                                                        if(task.getResult().child("Admin").exists()){
                                                            startActivity(new Intent(LoginActivity.this, AdminActivity.class)
                                                                    .putExtra("adminEmail",userEmail).putExtra("adminPass",userPass));
                                                            finish();
                                                        }
                                                        else if(task.getResult().child("Phone").exists()){
                                                            startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
                                                            finish();
                                                        }else if(!(task.getResult().child("Phone").exists())){
                                                            startActivity(new Intent(LoginActivity.this, AskMobileAndprofileActivity.class));
                                                            finish();
                                                        }else{
                                                            firebaseAuth.signOut();
//                                                            Snackbar.make(coordinatorLayout,"inValid User Account!!",Snackbar.LENGTH_LONG).show();
                                                            Toast.makeText(LoginActivity.this,"Invalid Login User!!",Toast.LENGTH_LONG).show();
                                                            startActivity(new Intent(LoginActivity.this, UserOptionActivity.class));
                                                            finish();
                                                        }
                                                        progressDialog.dismiss();

                                                    }else{
                                                        Snackbar.make(coordinatorLayout,"login not successfull",Snackbar.LENGTH_LONG).show();
                                                    }

                                                }
                                            });
                                        }
                                    },2000);
                                }else{
                                    firebaseAuth.signOut();
                                    progressDialog.dismiss();
                                    Snackbar.make(coordinatorLayout,"Email is not Verified Please Verify email first.",Snackbar.LENGTH_LONG).show();
                                    txtinplayoutemail.setEnabled(true);
                                    txtinplayoutpass.setEnabled(true);
                                }
                            }

                        }else{
                            progressDialog.dismiss();
                            Snackbar.make(coordinatorLayout,"Unable to Login "+task.getException().getMessage(),Snackbar.LENGTH_LONG).show();
                            txtinplayoutemail.setEnabled(true);
                            txtinplayoutpass.setEnabled(true);
                        }


                    }
                });

    }

    private boolean isAllDataMeetRequirment() {


        txtinplayoutemail.setEnabled(false);
        txtinplayoutpass.setEnabled(false);

        txtinplayoutemail.setError("");
        txtinplayoutpass.setError("");

//        txtinpeditextname txtinpeditextemail txtinpeditextpass
        boolean result = true;

        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if(txtinpeditextemail.getText().toString().trim().length() == 0){

            txtinplayoutemail.setError("Email is Empty!.");
            result = false;
        }
        else if(!(Pattern.compile(regex).matcher(txtinpeditextemail.getText()).matches())){
            txtinplayoutemail.setError("Invalid email address.");
            result = false;
        }

//        txtinpeditextpass
//                check contain capital letter
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
}//