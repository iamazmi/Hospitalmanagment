package com.hospital.hospitalmanagment.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hospital.hospitalmanagment.Adapter.AppointmentAdapter;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.model.Appointmentviewmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppointmentsActivity extends AppCompatActivity {

    private TextView testview;
    private String AppointmentsIsFrom;
    private FirebaseAuth firebaseAuth;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerViewAppointmentList;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointmentviewmodel> patientAppointmentmodel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        firebaseAuth = FirebaseAuth.getInstance();
        AppointmentsIsFrom = getIntent().getStringExtra("Appointmentfrom");
        coordinatorLayout = findViewById(R.id.appointmentlistcoordinate);

        Snackbar.make(coordinatorLayout,AppointmentsIsFrom+" Appointments",Snackbar.LENGTH_SHORT).show();

        testview  = findViewById(R.id.appointsfrom);
        testview.setText(AppointmentsIsFrom);
        recyclerViewAppointmentList = findViewById(R.id.appointmentactoinrecycleview);



    loadAppointments();

    }//

    private void loadAppointments() {

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
                                doctorsappoint.getChildren().forEach(Appointment -> {
                                    Appointmentviewmodel tempmodel = Appointment.getValue(Appointmentviewmodel.class);
                                    if (!AppointmentsIsFrom.equals(tempmodel.getStatus())) {
                                        return;
                                    }
                                    tempmodel.setPatienuid(userprofile.getKey());
                                    tempmodel.setPatitentName(userprofile.child("PaitentName").getValue().toString());
                                    if (userprofile.child("Profilepic").exists()) {
                                        tempmodel.setProfilepic(userprofile.child("Profilepic").getValue().toString());
                                    }
                                    tempmodel.setPhone(userprofile.child("Phone").getValue().toString());
                                    patientAppointmentmodel.add(tempmodel);
                                });
                            }
                        });

                    });

                    //attaching
                    appointmentAdapter = new AppointmentAdapter(patientAppointmentmodel,AppointmentsActivity.this,sendhandleimplimentation());
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

    }

    private AppointmentAdapter.HandleButtonofviewinterface sendhandleimplimentation(){

        return (new AppointmentAdapter.HandleButtonofviewinterface() {
            @Override
            public void onChangeAppointmentStatus(String useruid, String appointmentno,String newstatus) {

            }
        });

    }


}//