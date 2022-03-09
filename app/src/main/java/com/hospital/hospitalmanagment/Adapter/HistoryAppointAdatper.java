package com.hospital.hospitalmanagment.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.ViewUtils.ViewUtils;
import com.hospital.hospitalmanagment.model.HistoryAppointModel;
import com.hospital.hospitalmanagment.model.Level;

import java.util.ArrayList;
import java.util.List;

public class HistoryAppointAdatper extends RecyclerView.Adapter<HistoryAppointAdatper.RvViewHolder>{

    List<HistoryAppointModel> data = new ArrayList<>();
    Context mContext;
    public HistoryAppointAdatper(Context con,List<HistoryAppointModel> list) {
        this.data  = list;
        mContext = con;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RvViewHolder(LayoutInflater.from(mContext).inflate(R.layout.historyappointment, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewHolder holder, int position) {

        HistoryAppointModel hisapmodel = data.get(position);
        if(hisapmodel.getLvl() == 1) {
//            Toast.makeText(mContext,"docuid "+hisapmodel.getDocIUid(),Toast.LENGTH_LONG).show();
            FirebaseDatabase.getInstance().getReference("Doctors").child(hisapmodel.getDocIUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()){
                                holder.docorpatname.setText(task.getResult().child("DoctorName").getValue().toString());
                                holder.dateofappointorSpec.setText(task.getResult().child("Special").getValue().toString());
                                if(task.getResult().child("Profilepic").exists()){
                                    Glide.with(mContext).load(task.getResult().child("Profilepic").getValue().toString()).placeholder(R.drawable.doctor).into(holder.docpic);
                                }
                            }
                        }
                    });

            holder.appointstatus.setVisibility(View.GONE);
//            Glide.with(mContext).load(hisapmodel)
        }else{
            holder.marker.setVisibility(View.GONE);
            holder.docorpatname.setText(hisapmodel.getPatName());
            holder.dateofappointorSpec.setText(hisapmodel.getAppointDateTime());
            if(hisapmodel.getAppointStatus().equals("Approved")){
                holder.appointstatus.setTextColor(Color.GREEN);
            }else if(hisapmodel.getAppointStatus().equals("Pending")){
                holder.appointstatus.setTextColor(Color.YELLOW);
            }else{
                holder.appointstatus.setTextColor(Color.RED);
            }
            holder.appointstatus.setText(("Status :- "+hisapmodel.getAppointStatus()));
        }
        holder.setLevel(hisapmodel.getLvl());

    }
    public void addItem(HistoryAppointModel item) {
        data.add(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {

        private ImageView docpic;
        private TextView docorpatname,dateofappointorSpec,appointstatus;
        private  View itemView;
       private CardView marker;

        public RvViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            marker =itemView.findViewById(R.id.appointviewimgdoc);
            docpic = itemView.findViewById(R.id.doctirviewimg);
            docorpatname = itemView.findViewById(R.id.appviewdocname);
            dateofappointorSpec = itemView.findViewById(R.id.appviewdocphone);
            appointstatus =  itemView.findViewById(R.id.apreqstatus);

        }//
        public void setLevel(int level) {
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT);
            params.setMargins(2,5,2,5);
            if (level == Level.LEVEL_TWO) {
                params.setMarginStart(ViewUtils.getLevelOneMargin());
//                marker.setBackground(ContextCompat.getDrawable(mContext,R.drawable.marker_c));
            }

            itemView.setLayoutParams(params);
        }


    }


}//
