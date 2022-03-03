package com.hospital.hospitalmanagment.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hospital.hospitalmanagment.R;

import java.util.regex.Pattern;

public class ForgetPasswordActivity extends AppCompatActivity {

    private TextInputLayout txtinplayoutemail;
    private TextInputEditText txtinpeditextemail;

    private Button resetEmailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        txtinplayoutemail = findViewById(R.id.forgetpasswordemail);
        txtinpeditextemail = findViewById(R.id.forgetpasswordemailtxt);

        resetEmailBtn = findViewById(R.id.forgetpasswordbtn);


        resetEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAllDataMeetRequirment()){
                    Toast.makeText(ForgetPasswordActivity.this,"all is good",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgetPasswordActivity.this,"Wrong",Toast.LENGTH_LONG).show();
                    txtinplayoutemail.setEnabled(true);
                    }

            }
        });


    }//

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