package com.hospital.hospitalmanagment.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.loginsignup.UserOptionActivity;

public class DoctorActivity extends AppCompatActivity {

    Button logoutdr;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        logoutdr = findViewById(R.id.drlogout);
        firebaseAuth = FirebaseAuth.getInstance();

        logoutdr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(DoctorActivity.this, UserOptionActivity.class));
                finish();
            }
        });

    }
}