package com.example.gestion_des_rendez_vous;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestion_des_rendez_vous.classes.AppointmentClasse;
import com.example.gestion_des_rendez_vous.classes.DoctorClasse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.ParseException;

import java.util.List;
import java.util.Locale;

public class AppointmentActivity extends AppCompatActivity {

    private EditText dateEditText, timeEditText, descriptionEditText;
    private int year, month, day, hour, minute;
    private FirebaseFirestore db;
    private String doctorId,userId; // Remplacez par l'ID du médecin réel
    private TextView appointmentDetailsTextView;
    private String delayedMessage = "";

    // Liste des heures autorisées pour les rendez-vous
    private static final String[] ALLOWED_HOURS = {
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        dateEditText = findViewById(R.id.editTextDate);
        timeEditText = findViewById(R.id.editTextTime);
        descriptionEditText = findViewById(R.id.editTextDescription);
        Button confirmButton = findViewById(R.id.buttonConfirm);
        appointmentDetailsTextView = findViewById(R.id.textViewAppointmentDetails);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userId = user.getUid();

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            doctorId = extras.getString("doctorId");
        }

        // Initialize current date and time
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // Set default values to the date and time EditText fields
        updateDateAndTime();

        // Date picker dialog
        dateEditText.setOnClickListener(v -> showDatePickerDialog());

        // Time picker dialog
        timeEditText.setOnClickListener(v -> showTimePickerDialog());

        // Confirm button click listener (save appointment to Firestore)
        confirmButton.setOnClickListener(v -> checkAndSaveAppointment());
    }

    // Date picker listener
    private void showDatePickerDialog() {
        new DatePickerDialog(this, dateSetListener, year, month, day).show();
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = (view, selectedYear, selectedMonth, selectedDay) -> {
        year = selectedYear;
        month = selectedMonth;
        day = selectedDay;
        updateDateAndTime();
    };

    // Time picker listener
    private void showTimePickerDialog() {
        new TimePickerDialog(this, timeSetListener, hour, minute, true).show();
    }

    private final TimePickerDialog.OnTimeSetListener timeSetListener = (view, selectedHour, selectedMinute) -> {
        hour = selectedHour;
        minute = selectedMinute;
        updateDateAndTime();
    };

    // Update date and time in the EditText fields
    private void updateDateAndTime() {
        String dateFormat = "dd/MM/yyyy";
        String timeFormat = "HH:mm";

        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        SimpleDateFormat timeFormatter = new SimpleDateFormat(timeFormat, Locale.getDefault());

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(year, month, day, hour, minute);

        dateEditText.setText(dateFormatter.format(newCalendar.getTime()));
        timeEditText.setText(timeFormatter.format(newCalendar.getTime()));
    }

    // Method to check if the time is within allowed hours
    private boolean isTimeWithinAllowedRange(String selectedTime) {
        // Vérifier si l'heure sélectionnée fait partie des heures autorisées
        for (String allowedHour : ALLOWED_HOURS) {
            if (allowedHour.equals(selectedTime)) {
                return true;
            }
        }
        return false;
    }

    // Method to check if the time slot is available and save the appointment
    private void checkAndSaveAppointment() {
        String selectedDate = dateEditText.getText().toString();
        String selectedTime = timeEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        // Check if the selected time is within allowed hours
        if (isTimeWithinAllowedRange(selectedTime)) {
            // Check if the selected date is not a Saturday or Sunday
            if (!isWeekend(selectedDate)) {
                checkTimeSlotAvailability(selectedDate, selectedTime, description);
            } else {
                Toast.makeText(this, "Les rendez-vous ne sont pas autorisés les samedis et dimanches.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Les rendez-vous ne sont pas autorisés à cette heure.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to check if the time slot is available
    private void checkTimeSlotAvailability(String selectedDate, String selectedTime, String description) {
        // Assume you have a collection "appointments" in Firestore
        CollectionReference appointmentsCollection = db.collection("appointments");

        // Query appointments for the doctor and date
        Query query = appointmentsCollection
                .whereEqualTo("doctorId", doctorId)
                .whereEqualTo("date", selectedDate);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<AppointmentClasse> existingAppointments = task.getResult().toObjects(AppointmentClasse.class);

                if (isTimeSlotAvailable(existingAppointments, selectedTime)) {
                    // Time slot is available, save the appointment
                    saveAppointmentToFirestore(selectedDate, selectedTime, description);
                } else {
                    // The time slot is not available, suggest choosing another date
                    suggestChoosingAnotherDate(existingAppointments, selectedTime, description);
                }
            } else {
                // Handle the error
                Toast.makeText(this, "Erreur lors de la vérification de la disponibilité", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to suggest choosing another date if all time slots are taken
    private void suggestChoosingAnotherDate(List<AppointmentClasse> existingAppointments, String selectedTime, String description) {
        if (areAllTimeSlotsTaken(existingAppointments)) {
            // Suggest choosing another date
            showSuggestAnotherDateDialog();
        } else {
            // Suggest choosing another time on the same day
            showSuggestAnotherTimeDialog(existingAppointments, selectedTime, description);
        }
    }

    // Method to check if all time slots for a day are taken
    private boolean areAllTimeSlotsTaken(List<AppointmentClasse> existingAppointments) {
        int totalSlotsPerDay = ALLOWED_HOURS.length; // Total allowed time slots in a day
        return existingAppointments.size() >= totalSlotsPerDay;
    }

    // Method to show a message that the day is fully booked
    private void showSuggestAnotherDateDialog() {
        // Implement a Toast to suggest choosing another date
        String message = "Tous les rendez-vous pour cette journée sont pris. Veuillez choisir une autre date.";
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();

        // Schedule a task to cancel the Toast after 30 seconds
        new android.os.Handler().postDelayed(() -> toast.cancel(), 30000);
    }

    // Method to suggest choosing another time on the same day
    private void showSuggestAnotherTimeDialog(List<AppointmentClasse> existingAppointments, String selectedTime, String description) {
        Toast.makeText(AppointmentActivity.this, "Veuillez choisir un autre créneau, cette heure est déjà pris", Toast.LENGTH_SHORT).show();
    }

    // Method to increment time by 30 minutes
    private String incrementTimeBy30Minutes(String selectedTime) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timeFormatter.parse(selectedTime));

            // Increment by 30 minutes
            calendar.add(Calendar.MINUTE, 30);

            return timeFormatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return selectedTime;
    }

    // Method to save appointment to Firestore
    private void saveAppointmentToFirestore(String selectedDate, String selectedTime, String description) {
        // Create an instance of the class Appointment with the appointment data
        AppointmentClasse appointment = new AppointmentClasse(doctorId, userId, selectedDate, selectedTime, description);

        // Add the document to Firestore
        db.collection("appointments")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {
                    // Success: The appointment was successfully saved
                    // You can add other actions to perform after saving here
                    showAppointmentSavedMessage(selectedDate, selectedTime, description);
                })
                .addOnFailureListener(e -> {
                    // Failure: An error occurred while saving the appointment
                    // You can handle the error here
                    Toast.makeText(this, "Erreur lors de l'enregistrement du rendez-vous", Toast.LENGTH_SHORT).show();
                });
    }

    // Method to show a message that the appointment is saved
    private void showAppointmentSavedMessage(String selectedDate, String selectedTime, String description) {
        String message = "Rendez-vous enregistré avec succès !\nDate: " + selectedDate + "\nTime: " + selectedTime;

        // Show appointment details in TextView
        showAppointmentDetails(selectedDate, selectedTime, description);

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Schedule a task to clear the message after 30 seconds
        new android.os.Handler().postDelayed(() -> clearToastMessage(), 30000);
    }

    // Method to show a message that the appointment is delayed
    private void showAppointmentDelayedMessage(String selectedDate, String selectedTime) {
        String delayMessage = "L'heure sélectionnée n'est pas disponible. Le rendez-vous a été retardé de 30 minutes.";
        String message = "Date: " + selectedDate + "\nTime: " + selectedTime + "\n" + delayMessage;

        // Show appointment details in TextView
        showAppointmentDetails(selectedDate, selectedTime, delayMessage);

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Schedule a task to clear the message after 30 seconds
        new android.os.Handler().postDelayed(() -> clearToastMessage(), 30000);
    }

    // Method to clear the Toast message
    private void clearToastMessage() {
        Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toast.cancel();
    }

    // Method to show appointment details in TextView
    private void showAppointmentDetails(String selectedDate, String selectedTime, String additionalInfo) {
        String details = "Détails du rendez-vous:\nDate: " + selectedDate + "\nTime: " + selectedTime + "\n" + additionalInfo;
        appointmentDetailsTextView.setText(details);
    }

    // Method to check if the time slot is available in the existing appointments
    private boolean isTimeSlotAvailable(List<AppointmentClasse> existingAppointments, String selectedTime) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        try {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.setTime(timeFormatter.parse(selectedTime));

            for (AppointmentClasse appointment : existingAppointments) {
                Calendar existingCalendar = Calendar.getInstance();
                existingCalendar.setTime(timeFormatter.parse(appointment.getTime()));

                // Check if the time slot is available
                if (Math.abs(selectedCalendar.getTimeInMillis() - existingCalendar.getTimeInMillis()) < 30 * 60 * 1000) {
                    return false; // Not available
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true; // Available
    }

    // Nouvelle méthode pour vérifier si une date est un samedi ou dimanche
    private boolean isWeekend(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(date));

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Calendar.DAY_OF_WEEK renvoie 1 pour dimanche, 2 pour lundi, etc.
            return (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // En cas d'erreur, considérez la date comme n'étant pas un week-end
        }
    }
}