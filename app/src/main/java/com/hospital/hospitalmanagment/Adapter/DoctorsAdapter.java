package com.hospital.hospitalmanagment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.hospital.hospitalmanagment.R;
import com.hospital.hospitalmanagment.model.DoctorModel;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class DoctorsAdapter extends FirebaseRecyclerAdapter<DoctorModel,DoctorsAdapter.doctorsViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context context;
    private  onviewclickactionlistners interfacelistener;

    public DoctorsAdapter(@NonNull FirebaseRecyclerOptions<DoctorModel> options,Context context) {
        super(options);
        this.context =context;
    }

    @Override
    protected void onBindViewHolder(@NonNull doctorsViewHolder holder, int position, @NonNull DoctorModel model) {
//        holder.docpic.
        Glide.with(context).load(model.getProfilepic()).placeholder(R.drawable.doctor).into(holder.docpic);
        holder.docname.setText(model.getDoctorName());
        holder.docspec.setText(model.getSpecial());
        holder.docuid.setText(model.getUid());
    }

    @NonNull
    @Override
    public doctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctorview,parent,false);

        return new DoctorsAdapter.doctorsViewHolder(view);
    }

    public class doctorsViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView docpic;
        private TextView docname,docspec,docuid;
        private Button tryappointbtn;
        public doctorsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.docpic = itemView.findViewById(R.id.docpic);
            this.docname = itemView.findViewById(R.id.docname);
            this.docspec = itemView.findViewById(R.id.docspec);
            this.docuid = itemView.findViewById(R.id.docuid);
            this.tryappointbtn = itemView.findViewById(R.id.tryappointbtn);

            this.tryappointbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positon = getAbsoluteAdapterPosition();
                    if(positon != RecyclerView.NO_POSITION && interfacelistener != null){
                        interfacelistener.onbookappointclicklistner(docname.getText().toString(),docspec.getText().toString(),docuid.getText().toString(),positon);
                    }
                }
            });
        }

    }//imc

    public interface onviewclickactionlistners{
        void onbookappointclicklistner(String docname,String docspec,String uidofdoctor,int position);
    }

    public void setOnDoctorviewItemClickListner(onviewclickactionlistners interfaceimplimented){
        this.interfacelistener = interfaceimplimented;
    }

}//mc
