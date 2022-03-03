package com.hospital.hospitalmanagment.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.user.AskMobileAndprofileActivity;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout txtinplayoutname;
    private TextInputEditText txtinpeditextname;

    private TextInputLayout txtinplayoutemail;
    private TextInputEditText txtinpeditextemail;

    private TextInputLayout txtinplayoutpass;
    private TextInputEditText txtinpeditextpass;


    private Button SignupBtn;

    private FirebaseAuth firebaseAuth;
    private CoordinatorLayout coordinatorLayout;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtinplayoutname = findViewById(R.id.signupusername);
        txtinpeditextname = findViewById(R.id.signupusernametxt);

        txtinplayoutemail = findViewById(R.id.signupemail);
        txtinpeditextemail = findViewById(R.id.signupemailtxt);

        txtinplayoutpass = findViewById(R.id.signupassword);
        txtinpeditextpass = findViewById(R.id.signupasswordtxt);

        coordinatorLayout = findViewById(R.id.signupcoordinatelayout);

        SignupBtn = findViewById(R.id.signupbtn);

         progressDialog = new ProgressDialog(this);

        txtinpeditextname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 20){
                    txtinplayoutname.setError("Should not more then 20 character");
                }else {
                    txtinplayoutname.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllDataMeetRequirment()){
                    progressDialog.setTitle("Creating Account...");
                    progressDialog.show();
                    Toast.makeText(SignupActivity.this,"all is good",Toast.LENGTH_SHORT).show();
                    firebaseAuth = FirebaseAuth.getInstance();
                    nowCreateUserAccount();

                }else{
                    Toast.makeText(SignupActivity.this,"Wrong",Toast.LENGTH_SHORT).show();
                    txtinplayoutname.setEnabled(true);
                    txtinplayoutemail.setEnabled(true);
                    txtinplayoutpass.setEnabled(true);
                }
            }
        });

    }//

    private void nowCreateUserAccount() {
        final String userEmail = txtinpeditextemail.getText().toString().trim();
        final String userPass = txtinpeditextpass.getText().toString().trim();

        this.firebaseAuth.createUserWithEmailAndPassword(userEmail,userPass)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //send email verification
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FirebaseDatabase.getInstance().getReference("Patiens")
                                                .child(firebaseAuth.getCurrentUser().getUid())
                                                .child("PaitentName").setValue(txtinpeditextname.getText().toString().trim())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        firebaseAuth.signOut();

                                                        Snackbar.make(coordinatorLayout,"Account Created Verification link Sent to "+userEmail,Snackbar.LENGTH_LONG).show();
                                                        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                                                        intent.putExtra("loginType","Patient");
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progressDialog.dismiss();
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        },2000);
                                                    }
                                                });

                                    }else {
                                        progressDialog.dismiss();
                                        Snackbar.make(coordinatorLayout,"Unable to send Verification link to"+userEmail+" Please contact to Admin.",Snackbar.LENGTH_LONG).show();
                                        firebaseAuth.signOut();
                                        txtinplayoutname.setEnabled(true);
                                        txtinplayoutemail.setEnabled(true);
                                        txtinplayoutpass.setEnabled(true);
                                    }
                                }
                            });
                        }else{
                            progressDialog.dismiss();
                            Snackbar.make(coordinatorLayout,"Something Went Wrong Unable to Create Account \n"+task.getException().getMessage(),Snackbar.LENGTH_LONG).show();
                            txtinplayoutname.setEnabled(true);
                            txtinplayoutemail.setEnabled(true);
                            txtinplayoutpass.setEnabled(true);
                        }
                    }
                });

    }



    private boolean isAllDataMeetRequirment() {

        txtinplayoutname.setEnabled(false);
        txtinplayoutemail.setEnabled(false);
        txtinplayoutpass.setEnabled(false);
        txtinplayoutname.setError("");
        txtinplayoutemail.setError("");
        txtinplayoutpass.setError("");

//        txtinpeditextname txtinpeditextemail txtinpeditextpass
        boolean result = true;

        if(txtinpeditextname.getText().toString().trim().length() == 0 ){
            txtinplayoutname.setError("Name is Empty!");
            result = false;
        }
        else if(txtinpeditextname.getText().length()>10){
            txtinplayoutname.setError("Should not more then 20 character");
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