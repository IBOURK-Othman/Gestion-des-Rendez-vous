package com.example.gestion_des_rendez_vous.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface; // Assurez-vous que c'est l'import correct
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_des_rendez_vous.R;
import com.example.gestion_des_rendez_vous.classes.AppointmentListClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

        holder.imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle cancellation logic here
                showCancelConfirmationDialog(appointmentList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentArrayList.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageCancel;
        TextView appointmentdate, appointmentheure, doctorname;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            appointmentdate = itemView.findViewById(R.id.AppointmentDate);
            appointmentheure = itemView.findViewById(R.id.AppointmentHeure);
            doctorname = itemView.findViewById(R.id.DoctorName);
            imageCancel = itemView.findViewById(R.id.imageCancel);
        }
    }

    private void showCancelConfirmationDialog(AppointmentListClass appointment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Annulation de la réservation");
        builder.setMessage("Voulez-vous vraiment annuler cette réservation ?");

        // Bouton pour confirmer l'annulation
        builder.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Supprimez l'élément de la base de données Firestore
                deleteAppointmentFromFirestore(appointment);

                // Fermez la boîte de dialogue
                dialog.dismiss();
            }
        });

        // Bouton pour annuler la boîte de dialogue
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Fermez simplement la boîte de dialogue sans prendre d'autres mesures
                dialog.dismiss();
            }
        });

        // Affichez la boîte de dialogue
        builder.show();
    }

    private void deleteAppointmentFromFirestore(AppointmentListClass appointment) {
        // Initialisez Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Remplacez "appointments" par votre nom de collection Firestore réelle
        CollectionReference appointmentsCollection = db.collection("appointments");

        // Récupérez l'ID du document que vous souhaitez supprimer
        String appointmentId = appointment.getAppointmentId();

        // Supprimez le document de la collection
        appointmentsCollection.document(appointmentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // La réservation a été supprimée avec succès
                        // Vous pouvez ajouter d'autres actions si nécessaire
                        appointmentArrayList.remove(appointment);
                        notifyDataSetChanged(); // Mettez à jour votre RecyclerView après la suppression
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Gérez les erreurs ici
                        Toast.makeText(context, "Error deleting appointment", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
