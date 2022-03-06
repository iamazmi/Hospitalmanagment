package com.hospital.hospitalmanagment.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.hospital.hospitalmanagment.R;

public class AppointmentsActivity extends AppCompatActivity {

    private TextView testview;
    private String AppointmentsIsFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        AppointmentsIsFrom = getIntent().getStringExtra("Appointmentfrom");
        testview  = findViewById(R.id.appointsfrom);

        testview.setText(AppointmentsIsFrom);


    }
}