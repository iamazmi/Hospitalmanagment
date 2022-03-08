package com.hospital.hospitalmanagment.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hospital.hospitalmanagment.Adapter.AppointmentAdapter;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.model.Appointmentviewmodel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
    private LocalDate filterdate;
    private CardView Datepickercv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        AppointmentsIsFrom = getIntent().getStringExtra("Appointmentfrom");
        coordinatorLayout = findViewById(R.id.appointmentlistcoordinate);
        Datepickercv = findViewById(R.id.filterdatecd);


        testview  = findViewById(R.id.appointsfrom);
        testview.setText(AppointmentsIsFrom);
        recyclerViewAppointmentList = findViewById(R.id.appointmentactoinrecycleview);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        final MaterialDatePicker materialDatePicker = materialDateBuilder.setTitleText("Select date").build();

        Datepickercv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(coordinatorLayout,"Press 'Cancel' For reset Filter",Snackbar.LENGTH_SHORT).show();
                materialDatePicker.show(getSupportFragmentManager(),"MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                filterdate = Instant.ofEpochMilli(Long.parseLong(selection.toString())).atZone(ZoneId.systemDefault()).toLocalDate();
                loadAppointments();
//                Snackbar.make(coordinatorLayout,""+Instant.ofEpochMilli(Long.parseLong(selection.toString()))
//                        .atZone(ZoneId.systemDefault()).toLocalDate(),Snackbar.LENGTH_SHORT).show();
            }
        });


        materialDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterdate = null;
                loadAppointments();
                Snackbar.make(coordinatorLayout,"filter clear",Snackbar.LENGTH_SHORT).show();
            }
        });
        loadAppointments();

    }//

    private void loadAppointments() {
        progressDialog.setMessage("Loading....");
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
                                   if(filterdate != null){
                                    Log.i("abba", "onComplete: " + "filter not null " + (filterdate != null)
                                            + "date of both " + filterdate.getYear() + " : " + localDateOfAppointment.getYear() + " match " + (filterdate.getYear() == localDateOfAppointment.getYear())
                                            + "month of both " + filterdate.getMonthValue() + " : " + localDateOfAppointment.getMonthValue() + " match " + (filterdate.getMonthValue() == localDateOfAppointment.getMonthValue())
                                            + "day of both " + filterdate.getDayOfMonth() + " : " + localDateOfAppointment.getDayOfMonth()
                                            + " match " + (filterdate.getDayOfMonth() == localDateOfAppointment.getDayOfMonth())+" key is "+Appointment.getKey());
                                }
                                    if(filterdate != null &&
                                            (filterdate.getYear() == localDateOfAppointment.getYear()
                                                    && filterdate.getMonthValue() == localDateOfAppointment.getMonthValue()
                                                    && filterdate.getDayOfMonth() == localDateOfAppointment.getDayOfMonth()))
                                    {

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
                                        return;
                                    }

                                    if(filterdate == null){
                                        if(localDateOfAppointment.isBefore(LocalDate.now())){
                                            Log.i("insideoncompleteAppointmaker", "onComplete: in is before current date appointment date "+Appointment.getKey());
                                            return;
                                        }
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
                                    }

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
            public void onChangeAppointmentStatus(String useruid, String appointmentno,String newstatus,String rqtime) {

                if(newstatus.equals(getResources().getString(R.string.approved))){
                    String[] timings = (rqtime.split(" ")[0].split("-"));
//
//                Snackbar.make(coordinatorLayout,"timings"+(rqtime.split(" ")[0].split("-")[0])+" and "+(rqtime.split(" ")[0].split("-")[1]),Snackbar.LENGTH_SHORT).show();
                    MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).setTitleText("Allocate time to Appointment").setHour(Integer.parseInt(timings[0])).build();
                    materialTimePicker.show(getSupportFragmentManager(),"fragment_appointime");

                    materialTimePicker.addOnPositiveButtonClickListener(dialog -> {

                        DatabaseReference patiendbRef = FirebaseDatabase.getInstance().getReference("Patiens")
                                .child(useruid)
                                .child("Appointment")
                                .child(firebaseAuth.getCurrentUser().getUid())
                                .child(appointmentno);
                        patiendbRef.child("Status").setValue(newstatus);

                        int newHour;
                        String AmorPm;
                        if(materialTimePicker.getHour() > 12){
                            newHour = (Math.abs(12-(materialTimePicker.getHour())));
                            AmorPm = "PM";
                        }else if(materialTimePicker.getHour() == 12){
                            newHour = materialTimePicker.getHour();
                            AmorPm = "PM";
                        }
                        else {
                            newHour = materialTimePicker.getHour();
                            AmorPm = "AM";
                        }
                        int newMinute = materialTimePicker.getMinute();
                        LocalDate ld = Instant.ofEpochMilli(Long.parseLong(appointmentno)).atZone(ZoneId.systemDefault()).toLocalDate();
                        Snackbar.make(coordinatorLayout,"Appointment Booked "+ld+" "+newHour+" : "+newMinute+" "+AmorPm,Snackbar.LENGTH_SHORT).show();
                        patiendbRef.child("timeSlot").setValue(("Apt Date : "+ld.toString()+" "+newHour+":"+newMinute+" "+AmorPm));
                        loadAppointments();
                    });

                }else {
                    DatabaseReference patiendbRef = FirebaseDatabase.getInstance().getReference("Patiens")
                            .child(useruid)
                            .child("Appointment")
                            .child(firebaseAuth.getCurrentUser().getUid())
                            .child(appointmentno);
                    patiendbRef.child("Status").setValue(newstatus);
                    Snackbar.make(coordinatorLayout,"Appointment Canceled.",Snackbar.LENGTH_SHORT).show();
                    loadAppointments();
                }

            }
        });

    }


}//