package com.hospital.hospitalmanagment.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hospital.hospitalmanagment.Adapter.HistoryAppointAdatper;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.ViewUtils.ViewUtils;
import com.hospital.hospitalmanagment.model.HistoryAppointModel;
import com.hospital.hospitalmanagment.model.Level;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private List<HistoryAppointModel> appointments;
    private ProgressDialog progressDialog;
    private RecyclerView rv;
    private HistoryAppointAdatper historyAppointAdatper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        rv = (RecyclerView) findViewById(R.id.rv);
        ViewUtils.handleVerticalLines(findViewById(R.id.view_line_2));

        initilizeRecyclewithdata();

    }//

    private void initilizeRecyclewithdata() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Patiens").child(firebaseAuth.getCurrentUser().getUid());

        dbref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){

                    if(appointments != null){
                        appointments.clear();
                    }
                    appointments = new ArrayList<>();
                    if(task.getResult().child("Appointment").exists()){

                        task.getResult().child("Appointment").getChildren().forEach(docsappointment -> {
                            String docuid = docsappointment.getKey();
                            appointments.add(new HistoryAppointModel(docuid, Level.LEVEL_ONE));
                            docsappointment.getChildren().forEach(appointment ->{
                                HistoryAppointModel appointofdoc = new HistoryAppointModel();

                                appointofdoc.setPatName(task.getResult().child("PaitentName").getValue().toString());

                                String statusofappoint = appointment.child("Status").getValue().toString();

                               appointofdoc.setAppointStatus(statusofappoint);

                                if(statusofappoint.equals(getResources().getString(R.string.approved))){
                                    appointofdoc.setAppointDateTime(appointment.child("timeSlot").getValue().toString());
                                }else{
                                    LocalDate ld = Instant.ofEpochMilli(Long.parseLong(appointment.child("DateTimestamp").getValue().toString())).atZone(ZoneId.systemDefault()).toLocalDate();
                                    String slotime = appointment.child("timeSlot").getValue().toString();
                                    appointofdoc.setAppointDateTime((ld+" "+slotime));
                                }
                                appointofdoc.setLvl(Level.LEVEL_TWO);
                                appointments.add(appointofdoc);
                            });

                        });
                        historyAppointAdatper =new HistoryAppointAdatper(HistoryActivity.this,appointments);
                        rv.setAdapter(historyAppointAdatper);
                        historyAppointAdatper.notifyDataSetChanged();

                    }//if

                    progressDialog.dismiss();
                }else{
                    progressDialog.setTitle("Getting Error : "+task.getException().getMessage());
                    progressDialog.dismiss();
                }
            }
        });

    }


}//