package com.hospital.hospitalmanagment.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hospital.hospitalmanagment.model.Appointmentviewmodel;

import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.itemViewHolderclass> {

    private List<Appointmentviewmodel> DoctorAppointments = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public AppointmentAdapter.itemViewHolderclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.itemViewHolderclass holder, int position) {

    }

    @Override
    public int getItemCount() {
        return DoctorAppointments.size();
    }

    public class itemViewHolderclass extends RecyclerView.ViewHolder
    {
        public itemViewHolderclass(@NonNull View itemView) {
            super(itemView);
        }
    }//


}//
