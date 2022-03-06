package com.hospital.hospitalmanagment.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hospital.hospitalmanagment.Adapter.DoctorsAdapter;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.model.DoctorModel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DoctorsListActivity extends AppCompatActivity {

    private RecyclerView doclistrecycleview;
    private DoctorsAdapter doctorsAdapter;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference doctorsdbReference;
    public Context doctorslistcontex;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onStart() {
        super.onStart();
        doctorsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        doctorsAdapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);
        coordinatorLayout = findViewById(R.id.doctorslistcoordinate);
        doclistrecycleview = findViewById(R.id.doctorsrecycleview);
        doclistrecycleview.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth = FirebaseAuth.getInstance();
        doctorsdbReference = FirebaseDatabase.getInstance().getReference("Doctors");
        doctorslistcontex = this;

        FirebaseRecyclerOptions<DoctorModel> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<DoctorModel>()
                .setQuery(doctorsdbReference,DoctorModel.class).build();
        doctorsAdapter = new DoctorsAdapter(firebaseRecyclerOptions,doctorslistcontex);
        doclistrecycleview.setAdapter(doctorsAdapter);

        doctorsAdapter.setOnDoctorviewItemClickListner(new DoctorsAdapter.onviewclickactionlistners() {
            @Override
            public void onbookappointclicklistner(String docname,String docspec,String uidofdoctor, int position) {
                Snackbar.make(coordinatorLayout, "uid is "+uidofdoctor+" and position is "+position, Snackbar.LENGTH_SHORT).show();
                LayoutInflater li = LayoutInflater.from(doctorslistcontex);
                View formalertview = li.inflate(R.layout.appointforminalertdialog,null);
                AlertDialog.Builder formalert = new AlertDialog.Builder(doctorslistcontex);

//                appointdrname appointdrspec
                final TextView tvdrname = formalertview.findViewById(R.id.appointdrname);
                tvdrname.setText(docname);

                final TextView tvdrspec = formalertview.findViewById(R.id.appointdrspec);
                tvdrspec.setText(docspec);

                final CalendarView appointcaledar = formalertview.findViewById(R.id.appointmentcalendarView);
                appointcaledar.setMinDate(appointcaledar.getDate());
                LocalDate date = LocalDate.now();
                Calendar cl = Calendar.getInstance();
                cl.set(date.getYear(),date.getMonthValue()-1,date.getDayOfMonth()+6);

                appointcaledar.setMaxDate(cl.getTimeInMillis());
                Snackbar.make(coordinatorLayout," year "+date.getYear()+" month : "+date.getMonthValue()+" date :"+(date.getDayOfMonth()+7),Snackbar.LENGTH_LONG).show();

                appointcaledar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(year,(month),dayOfMonth);

                        LocalDate date1 = Instant.ofEpochMilli(calendar1.getTimeInMillis()).atZone(ZoneId.systemDefault()).toLocalDate();
                        Snackbar.make(coordinatorLayout,"from local date obj "+date1.getYear()+" month "+date1.getMonthValue()+" day "+date1.getDayOfMonth(),Snackbar.LENGTH_SHORT).show();

                    }
                });

                final Spinner spinneroftime = formalertview.findViewById(R.id.timingspinner);
                List<String> Timings = Arrays.asList("7-9 AM", "9-11 AM","11-1 PM","2-4 PM","4-6 PM","6-8 PM","8-10 PM");
                ArrayAdapter<String> adapteroftiminglist = new ArrayAdapter<>(doctorslistcontex,R.layout.spinnertextview,Timings);
                adapteroftiminglist.setDropDownViewResource(R.layout.spinnertextview);
                spinneroftime.setAdapter(adapteroftiminglist);


                spinneroftime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        Snackbar.make(coordinatorLayout,""+parent.getItemAtPosition(position).toString(),Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                formalert.setView(formalertview);

                formalert
                        .setCancelable(false)
                        .setPositiveButton("Request Appointment", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = formalert.create();
                alertDialog.show();

            }
        });

    }//

}//