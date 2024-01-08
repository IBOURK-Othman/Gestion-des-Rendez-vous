package com.example.gestion_des_rendez_vous.classes;

import java.util.UUID;

public class AppointmentListClass {
    String doctorId;
    String userId;
    String date;
    String time;
    String nom;
    String appointmentId;

    public AppointmentListClass() {
    }

    public AppointmentListClass(String doctorId, String userId, String date, String time) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.date = date;
        this.time = time;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return nom;
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

    public void setName(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "AppointmentListClass{" +
                "doctorId='" + doctorId + '\'' +
                ", userId='" + userId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    // Setter method for appointmentId (if needed)
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    // Add a method to generate a unique appointmentId (you can customize this based on your needs)
    private String generateAppointmentId() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return appointmentId;
    }
}
