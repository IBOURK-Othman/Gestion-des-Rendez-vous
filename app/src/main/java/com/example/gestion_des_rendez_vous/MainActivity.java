package com.example.gestion_des_rendez_vous;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.gestion_des_rendez_vous.adapters.DoctorAdapter;
import com.example.gestion_des_rendez_vous.classes.DoctorClasse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DoctorClasse> docArrayList;
    DoctorAdapter myAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recyclerView = findViewById(R.id.recycleView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        docArrayList = new ArrayList<DoctorClasse>();
        myAdapter = new DoctorAdapter(MainActivity.this, docArrayList);
        recyclerView.setAdapter(myAdapter);

        EventchangeListner();





    }

    private void EventchangeListner() {


        db.collection("Doctors")
                .orderBy("nom", Query.Direction.ASCENDING)
                        .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                DoctorClasse doctor = document.toObject(DoctorClasse.class);
                                                doctor.setId(document.getId());
                                                docArrayList.add(doctor);
                                                myAdapter.notifyDataSetChanged();
                                            }
                                        }else{
                                            Toast.makeText(MainActivity.this, "FirebaseFirestore ERROR", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
    }
}