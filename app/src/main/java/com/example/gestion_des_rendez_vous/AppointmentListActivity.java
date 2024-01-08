package com.example.gestion_des_rendez_vous;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_des_rendez_vous.adapters.AppointmentListAdapter;
import com.example.gestion_des_rendez_vous.classes.AppointmentListClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AppointmentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentListAdapter appointmentListAdapter;
    private ArrayList<AppointmentListClass> appointmentArrayList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointmentlist);

        recyclerView = findViewById(R.id.recycleView3);
        appointmentArrayList = new ArrayList<>();
        appointmentListAdapter = new AppointmentListAdapter(this, appointmentArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appointmentListAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Get the currently signed-in user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Get the user ID
            String userId = currentUser.getUid();

            // Get appointments for the user from Firestore
            getAppointmentsForUser(userId);
        }
    }

    private void getAppointmentsForUser(String userId) {
        // Replace "appointments" with your actual Firestore collection name
        CollectionReference appointmentsCollection = db.collection("appointments");

        // Query appointments for the specific user
        appointmentsCollection.whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            appointmentArrayList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                // Convert Firestore document to AppointmentListClass
                                AppointmentListClass appointment = document.toObject(AppointmentListClass.class);
                                appointment.setAppointmentId(document.getId());
                                getDoctorName(appointment);
//                                appointmentArrayList.add(appointment);
                            }
//                            appointmentListAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    }

                });
    }

    private void getDoctorName(AppointmentListClass appointment) {
        // Replace "Doctors" with your actual Firestore collection name for doctors
        CollectionReference doctorsCollection = db.collection("Doctors");

        // Query to get the doctor's name
        doctorsCollection.document(appointment.getDoctorId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Set the doctor's name in the AppointmentListClass
                                appointment.setName(document.getString("nom"));

                                appointmentArrayList.add(appointment);
                                // Notify the adapter that the data has changed
                                appointmentListAdapter.notifyDataSetChanged();
                            } else {
                                Log.d("Firestore", "Doctor document does not exist");
                            }
                        } else {
                            Log.d("Firestore", "Error getting doctor document: ", task.getException());
                        }
                    }
                });
    }
}
