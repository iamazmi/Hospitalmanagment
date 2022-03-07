package com.hospital.hospitalmanagment.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.loginsignup.UserOptionActivity;

public class DoctorHomeActivity extends AppCompatActivity {

    private RecyclerView todayApproverecycle;
    private TextView tvdocname,tvdocspec;
    private ImageView docimg;
    private MaterialCardView pendingCardview,approvedCardview;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CardView docoutv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        this.tvdocname = findViewById(R.id.docname);
        this.tvdocspec = findViewById(R.id.docspeci);
        this.docimg = findViewById(R.id.docimg);
        pendingCardview = findViewById(R.id.cardpending);
        approvedCardview = findViewById(R.id.cardapproved);
        todayApproverecycle = findViewById(R.id.recycledrappoint);
        docoutv = findViewById(R.id.docout);
        firebaseAuth = FirebaseAuth.getInstance();

        initilizeDoctordata();
        initilizeDoctorAppointment();

        docoutv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(DoctorHomeActivity.this, UserOptionActivity.class));
                finish();
            }
        });

        pendingCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = getResources().getString(R.string.pending);
                startActivity(new Intent(DoctorHomeActivity.this,AppointmentsActivity.class).putExtra("Appointmentfrom",from));
            }
        });

        approvedCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = getResources().getString(R.string.approved);
                startActivity(new Intent(DoctorHomeActivity.this,AppointmentsActivity.class).putExtra("Appointmentfrom",from));
            }
        });

    }//


    private void initilizeDoctorAppointment() {

    }

    private void initilizeDoctordata() {

        DatabaseReference doctordbref = FirebaseDatabase.getInstance().getReference("Doctors").child(firebaseAuth.getCurrentUser().getUid());
        doctordbref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    tvdocname.setText(task.getResult().child("DoctorName").getValue().toString());
                    tvdocspec.setText(task.getResult().child("Special").getValue().toString());
                    if(task.getResult().child("Profilepic").exists()){
                        Glide.with(DoctorHomeActivity.this).load(task.getResult().child("Profilepic").getValue().toString()).placeholder(R.drawable.doctor).into(docimg);
                    }
                    progressDialog.dismiss();
                }
            }
        });

    }

}//