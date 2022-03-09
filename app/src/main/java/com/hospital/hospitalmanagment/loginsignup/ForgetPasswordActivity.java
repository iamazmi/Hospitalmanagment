package com.hospital.hospitalmanagment.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.hospital.hospitalmanagment.R;

import java.util.regex.Pattern;

public class ForgetPasswordActivity extends AppCompatActivity {

    private TextInputLayout txtinplayoutemail;
    private TextInputEditText txtinpeditextemail;
    private FirebaseAuth firebaseAuth;
    private Button resetEmailBtn;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        txtinplayoutemail = findViewById(R.id.forgetpasswordemail);
        txtinpeditextemail = findViewById(R.id.forgetpasswordemailtxt);

        resetEmailBtn = findViewById(R.id.forgetpasswordbtn);

        firebaseAuth = FirebaseAuth.getInstance();
        coordinatorLayout = findViewById(R.id.forgcoordinate);

        resetEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAllDataMeetRequirment()){
//                    Toast.makeText(ForgetPasswordActivity.this,"all is good",Toast.LENGTH_LONG).show();
                    sendforgetpass();
                }else{
//                    Toast.makeText(ForgetPasswordActivity.this,"Wrong",Toast.LENGTH_LONG).show();
                    txtinplayoutemail.setEnabled(true);
                    }

            }
        });


    }//

    private void sendforgetpass() {
        firebaseAuth.sendPasswordResetEmail(txtinpeditextemail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Snackbar.make(coordinatorLayout,"Passowrd Reset link sent check your mail.",Snackbar.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(ForgetPasswordActivity.this,UserOptionActivity.class));
                            finish();
                        }
                    },1000);
                }else{
                    Snackbar.make(coordinatorLayout,"Password reset fail "+task.getException().getMessage(),Snackbar.LENGTH_LONG).show();
                    txtinplayoutemail.setEnabled(true);
                    txtinplayoutemail.setError("");
                }
            }
        });
    }

    private boolean isAllDataMeetRequirment() {
        txtinplayoutemail.setEnabled(false);
        txtinplayoutemail.setError("");

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

        return result;
    }

}//