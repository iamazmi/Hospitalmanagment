package com.hospital.hospitalmanagment.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.hospital.hospitalmanagment.Adapter.AppointmentAdapter;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.loginsignup.UserOptionActivity;
import com.hospital.hospitalmanagment.model.Appointmentviewmodel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class DoctorHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAppointmentList;
    private TextView tvdocname,tvdocspec;
    private ImageView docimg;
    private MaterialCardView pendingCardview,approvedCardview;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CardView docoutv;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointmentviewmodel> patientAppointmentmodel;

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
        recyclerViewAppointmentList = findViewById(R.id.recycledrappoint);
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
        progressDialog.setMessage("Loading Appointments....");
        progressDialog.show();

        recyclerViewAppointmentList.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("Patiens");
        dbreference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if (patientAppointmentmodel != null){
                        patientAppointmentmodel.clear();
                    }
//                    Log.i("taskdata", "onComplete: "+task.getResult());

                    final String uidOfDoctor = firebaseAuth.getCurrentUser().getUid();
                    Iterable<DataSnapshot> iterable = task.getResult().getChildren();
                    patientAppointmentmodel = new ArrayList<>();

                    //iterating user each profile
                    iterable.forEach(userprofile -> {
//                       Log.i("taskdata", "onLoop: "+dataSnapshot);

                        //filter with current doctor appointment and ge
                        userprofile.child("Appointment").getChildren().forEach(doctorsappoint -> {
//                            Log.i("taskdata", "onLoopfilterdoctor: "+dataSnapshot1+" "+dataSnapshot1.getKey()+" this is uid of doc"+uidOfDoctor);

                            if(doctorsappoint.getKey().equals(uidOfDoctor)){
//                                Log.i("taskdata", "onLoopfilterdoctor: "+dataSnapshot1.getKey());

                                //now get doctorAppointment
                                doctorsappoint.getChildren().forEach(Appointment ->
                                {
                                    String timestampOfAppointment = Appointment.getKey();
                                    LocalDate localDateOfAppointment =  Instant.ofEpochMilli(Long.parseLong(timestampOfAppointment)).atZone(ZoneId.systemDefault()).toLocalDate();
//                                    filterdate.isAfter(localDateOfAppointment)
                                    LocalDate filterdate = LocalDate.now();
                                    if(filterdate != null &&
                                            (filterdate.getYear() == localDateOfAppointment.getYear()
                                                    && filterdate.getMonthValue() == localDateOfAppointment.getMonthValue()
                                                    && filterdate.getDayOfMonth() == localDateOfAppointment.getDayOfMonth()))
                                    {
                                        Appointmentviewmodel tempmodel = Appointment.getValue(Appointmentviewmodel.class);
//                                        Log.i("doctorhome check", "onComplete: "+"app");
                                        if (!(getResources().getString(R.string.approved)).equals(tempmodel.getStatus())) {
                                            return;
                                        }
                                        tempmodel.setPatienuid(userprofile.getKey());
                                        tempmodel.setPatitentName(userprofile.child("PaitentName").getValue().toString());
                                        if (userprofile.child("Profilepic").exists()) {
                                            tempmodel.setProfilepic(userprofile.child("Profilepic").getValue().toString());
                                        }
                                        tempmodel.setPhone(userprofile.child("Phone").getValue().toString());
                                        patientAppointmentmodel.add(tempmodel);
                                    }

                                });
                            }
                        });

                    });

                    //attaching
                    appointmentAdapter = new AppointmentAdapter(patientAppointmentmodel,DoctorHomeActivity.this);
                    recyclerViewAppointmentList.setAdapter(appointmentAdapter);
                    appointmentAdapter.notifyDataSetChanged();

//                    patientAppointmentmodel.forEach(appointmentviewmodel -> {
//                        Log.i("taskdata", "onComplete: ArrayListLoop "+appointmentviewmodel.toString());
//                    });
                }else {
                    Log.i("taskdata", "onComplete: fail");

                }
                progressDialog.dismiss();
            }
        });


    }//mm

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