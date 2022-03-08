package com.hospital.hospitalmanagment.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.model.Appointmentviewmodel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.itemViewHolderclass> {

    private List<Appointmentviewmodel> DoctorAppointments = new ArrayList<>();
    private Context context;
    private HandleButtonofviewinterface handleButtonofviewinterface;

    public interface HandleButtonofviewinterface{
        void onChangeAppointmentStatus(String useruid,String appointmentno,String newstatus,String rqtime);
    }

    public void setInterfaceImplementions(HandleButtonofviewinterface forhandleclick){
        this.handleButtonofviewinterface = forhandleclick;
    }

    public AppointmentAdapter(List<Appointmentviewmodel> doctorAppointments, Context context, HandleButtonofviewinterface handleButtonofviewinterface) {
        DoctorAppointments = doctorAppointments;
        this.context = context;
        this.handleButtonofviewinterface = handleButtonofviewinterface;
    }
    public AppointmentAdapter(List<Appointmentviewmodel> doctorAppointments, Context context) {
        DoctorAppointments = doctorAppointments;
        this.context = context;
        this.handleButtonofviewinterface = handleButtonofviewinterface;
    }

    @NonNull
    @Override
    public AppointmentAdapter.itemViewHolderclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointmentdoctorview,parent,false);
        return new itemViewHolderclass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.itemViewHolderclass holder, int position) {

        Appointmentviewmodel appointmentm = DoctorAppointments.get(position);
        Glide.with(context).load(appointmentm.getProfilepic()).placeholder(R.drawable.patientpic).into(holder.appointPaitenPic);
        holder.pname.setText(appointmentm.getPatitentName());
        holder.Pphone.setText(appointmentm.getPhone());
        holder.pAppointstatus.setText(appointmentm.getStatus());
        holder.pAppointstatus.setTextColor(Color.YELLOW);
        LocalDate localDate = Instant.ofEpochMilli(Long.parseLong(appointmentm.getDateTimestamp())).atZone(ZoneId.systemDefault()).toLocalDate();
        if(appointmentm.getStatus().equals("Approved")){
            holder.timeslot.setText(appointmentm.getTimeSlot());
        }else {
            holder.timeslot.setText(("For : "+localDate.toString()+" : "+appointmentm.getTimeSlot()));
        }
        if(localDate.isBefore(LocalDate.now())){
            holder.approveapt.setEnabled(false);
        }
        holder.uuid = appointmentm.getPatienuid();
        if(appointmentm.getStatus().equals("Approved")){
            holder.btngrp.setVisibility(View.GONE);
            holder.pAppointNo.setText("Apt No : "+appointmentm.getDateTimestamp());
            holder.pAppointstatus.setTextColor(Color.GREEN);
        }

    }

    @Override
    public int getItemCount() {
        return DoctorAppointments.size();
    }

    public class itemViewHolderclass extends RecyclerView.ViewHolder
    {
        private ImageView appointPaitenPic;
        private TextView pname,Pphone,pAppointstatus,pAppointNo,timeslot;
        private LinearLayout btngrp;
        private Button cancelapt,approveapt;
        private String uuid;
        public itemViewHolderclass(@NonNull View itemView) {
            super(itemView);
            appointPaitenPic = itemView.findViewById(R.id.patienviewimg);
            pname = itemView.findViewById(R.id.appviewpatiename);
            Pphone = itemView.findViewById(R.id.appviewpatienphone);
            pAppointstatus = itemView.findViewById(R.id.reqstatus);
            pAppointNo = itemView.findViewById(R.id.appointnumber);
            timeslot = itemView.findViewById(R.id.rqtimeslot);
            btngrp = itemView.findViewById(R.id.buttongrp);
            cancelapt = itemView.findViewById(R.id.btnappointcancel);
            approveapt = itemView.findViewById(R.id.btnappointapprove);

            cancelapt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positon = getAbsoluteAdapterPosition();
                    if(positon != RecyclerView.NO_POSITION && handleButtonofviewinterface != null){
                     handleButtonofviewinterface.onChangeAppointmentStatus(uuid,(DoctorAppointments.get(positon).getDateTimestamp()),"Canceled",(DoctorAppointments.get(positon).getTimeSlot()));
//                        Toast.makeText(context,uuid,Toast.LENGTH_SHORT).show();
                    }
                }
            });

            approveapt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positon = getAbsoluteAdapterPosition();
                    if(positon != RecyclerView.NO_POSITION && handleButtonofviewinterface != null){
                        handleButtonofviewinterface.onChangeAppointmentStatus(uuid,(DoctorAppointments.get(positon).getDateTimestamp()),"Approved",(DoctorAppointments.get(positon).getTimeSlot()));
//                        Toast.makeText(context,uuid,Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }//


}//
