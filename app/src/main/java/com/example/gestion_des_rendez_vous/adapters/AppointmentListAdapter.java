package com.example.gestion_des_rendez_vous.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_des_rendez_vous.R;
import com.example.gestion_des_rendez_vous.classes.AppointmentClasse;
import com.example.gestion_des_rendez_vous.classes.AppointmentListClass;

import java.util.ArrayList;

public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.AppointmentViewHolder> {

    private final Context context;
    private final ArrayList<AppointmentListClass> appointmentArrayList;

    public AppointmentListAdapter(Context context, ArrayList<AppointmentListClass> appointmentArrayList) {
        this.context = context;
        this.appointmentArrayList = appointmentArrayList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item2, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        AppointmentListClass appointmentList = appointmentArrayList.get(position);

        holder.appointmentdate.setText(appointmentList.getDate());
        holder.appointmentheure.setText(appointmentList.getTime());
        holder.doctorname.setText(appointmentList.getName());
    }

    @Override
    public int getItemCount() {
        return appointmentArrayList.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView appointmentdate, appointmentheure, doctorname;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            appointmentdate= itemView.findViewById(R.id.AppointmentDate);
            appointmentheure = itemView.findViewById(R.id.AppointmentHeure);
            doctorname = itemView.findViewById(R.id.DoctorName);
        }
    }
}
