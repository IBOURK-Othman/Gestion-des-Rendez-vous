package com.example.gestion_des_rendez_vous.classes;

public class AppointmentClasse {
    private String doctorId;
    private String userId;
    private String date;
    private String time;
    private String description;


    public AppointmentClasse() {
    }

    public AppointmentClasse(String doctorId, String userId, String date, String time, String description) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public String getDoctorId() {
        return doctorId;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
