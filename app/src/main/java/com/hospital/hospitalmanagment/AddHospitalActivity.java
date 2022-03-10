package com.hospital.hospitalmanagment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddHospitalActivity extends AppCompatActivity {
    private TextInputLayout hsnamel;
    private TextInputEditText hsname;
    private TextInputLayout hsaddl;
    private TextInputEditText hsadd;

    private Button Addhosq;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital);

        firebaseAuth = FirebaseAuth.getInstance();
        Addhosq = findViewById(R.id.addhospitalbtn);
        hsnamel = findViewById(R.id.hoslnameout);
        hsname = findViewById(R.id.hoslname);

        hsaddl = findViewById(R.id.hosladdout);
        hsadd = findViewById(R.id.hosladd);

        progressDialog = new ProgressDialog(this);

        Addhosq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notempty()){
                    progressDialog.setMessage("Adding");
                    progressDialog.show();
                    String timestamp = (Calendar.getInstance().getTimeInMillis()+"");
                    FirebaseDatabase.getInstance().getReference("Hospitals").child(timestamp)
                            .child("hospitalName").setValue(hsname.getText().toString().trim());
                    FirebaseDatabase.getInstance().getReference("Hospitals").child(timestamp)
                            .child("hospitalAddress").setValue(hsadd.getText().toString().trim());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hsname.setText("");
                            hsadd.setText("");
                            progressDialog.dismiss();
                        }
                    },500);
                }
            }
        });

    }//

    private boolean notempty() {
        boolean res = true;
        hsnamel.setError("");
        hsaddl.setError("");

        if(hsname.getText().toString().equals("")){
            res = false;
            hsnamel.setError("Field is Empty!");
        }

        if(hsadd.getText().toString().equals("")){
            res = false;
            hsaddl.setError("Field is Empty!");
        }
        return  res;
    }


}//