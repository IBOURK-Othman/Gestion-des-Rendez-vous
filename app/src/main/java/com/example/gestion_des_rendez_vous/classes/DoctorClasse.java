package com.example.gestion_des_rendez_vous.classes;

public class DoctorClasse {

    String id, nom, specialty;
    boolean isAvailable;

    public DoctorClasse() {
    }



    public DoctorClasse(String nom, String specialty, boolean availability) {
        this.nom = nom;
        this.specialty = specialty;
        this.isAvailable = availability;
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
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
