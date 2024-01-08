package com.example.gestion_des_rendez_vous.classes;

public class DoctorClasse {

    String id, nom, specialty;
    boolean available;

    public DoctorClasse() {
    }



    public DoctorClasse(String nom, String specialty, boolean availability) {
        this.nom = nom;
        this.specialty = specialty;
        this.available = availability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
