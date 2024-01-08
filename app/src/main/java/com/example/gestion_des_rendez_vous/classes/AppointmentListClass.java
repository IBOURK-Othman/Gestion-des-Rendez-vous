package com.example.gestion_des_rendez_vous.classes;


public class AppointmentListClass {
    String doctorId;
    String userId;
    String date;
    String time;
    String nom;

    public AppointmentListClass(){}
    public AppointmentListClass(String doctorId, String userId, String date, String time) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.nom= nom;
    }

    public String getDoctorId() {
        return doctorId;
    }
    public String getName(){return nom;}

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String nom) {this.nom=nom;
    }
}

