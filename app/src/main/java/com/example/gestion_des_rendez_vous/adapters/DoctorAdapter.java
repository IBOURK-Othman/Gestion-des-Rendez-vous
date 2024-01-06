package com.example.gestion_des_rendez_vous.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_des_rendez_vous.AppointmentActivity;
import com.example.gestion_des_rendez_vous.R;
import com.example.gestion_des_rendez_vous.classes.DoctorClasse;

import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder>{

    Context context;
    ArrayList<DoctorClasse> DoctorArrayList;

    public DoctorAdapter(Context context, ArrayList<DoctorClasse> doctorArrayList) {
        this.context = context;
        this.DoctorArrayList = doctorArrayList;
    }

    @NonNull
    @Override
    public DoctorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_doctor,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.MyViewHolder holder, int position) {

        DoctorClasse doc = DoctorArrayList.get(position);
        holder.Nom.setText(doc.getNom());
        holder.specialty.setText(doc.getSpecialty());
        String availability = doc.isAvailable() ? "" : "";
        holder.availability.setText(availability);

        holder.cardViewDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(context, AppointmentActivity.class);
            intent.putExtra("doctorId", doc.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return DoctorArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Nom, specialty, availability;
        CardView cardViewDoctor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Nom = itemView.findViewById(R.id.firstNameTextView);
            specialty = itemView.findViewById(R.id.specialtyTextView);
            availability = itemView.findViewById(R.id.availabilityTextView);
            cardViewDoctor = itemView.findViewById(R.id.cardViewDoctor);
        }
    }
}
