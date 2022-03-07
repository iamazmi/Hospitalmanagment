package com.hospital.hospitalmanagment.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        firebaseAuth = FirebaseAuth.getInstance();
        AppointmentsIsFrom = getIntent().getStringExtra("Appointmentfrom");

        testview  = findViewById(R.id.appointsfrom);
        testview.setText(AppointmentsIsFrom);

        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("Patiens");

//        Query query = dbreference.equalTo("Approved");

        dbreference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
//                    Log.i("taskdata", "onComplete: "+task.getResult());

                    final String uidOfDoctor = firebaseAuth.getCurrentUser().getUid();
                    Iterable<DataSnapshot> iterable = task.getResult().getChildren();
                    List<Appointmentviewmodel> patientAppointmentmodel = new ArrayList<>();

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
                                    tempmodel.setPatienuid(userprofile.getKey());
                                    tempmodel.setPatitentName(userprofile.child("PaitentName").getValue().toString());
                                    if(userprofile.child("Profilepic").exists()){
                                        tempmodel.setProfilepic(userprofile.child("Profilepic").getValue().toString());
                                    }
                                    tempmodel.setPhone(userprofile.child("Phone").getValue().toString());
                                    patientAppointmentmodel.add(tempmodel);
                                });

                            }
                        });

                   });

                    patientAppointmentmodel.forEach(appointmentviewmodel -> {
                        Log.i("taskdata", "onComplete: ArrayListLoop "+appointmentviewmodel.toString());
                    });


                }else {
                    Log.i("taskdata", "onComplete: fail");
                }
            }
        });

    }//


}//