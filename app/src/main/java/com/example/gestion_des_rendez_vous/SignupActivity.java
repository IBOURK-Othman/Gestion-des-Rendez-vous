package com.example.gestion_des_rendez_vous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestion_des_rendez_vous.classes.UserClasse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        signupName= findViewById(R.id.signup_nom);
        signupEmail= findViewById(R.id.signup_email);
        signupPassword= findViewById(R.id.signup_password);
        signupButton= findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();

                UserClasse userClasse = new UserClasse(name, email,password);


                if(email != null && password != null){
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password)

                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        DatabaseReference dbRef = db.getReference("Users");

                                        FirebaseUser user = mAuth.getCurrentUser();

                                        dbRef.child(user.getUid()).setValue(userClasse).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText( SignupActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                            }
                                        });

                                    }else {
                                        Toast.makeText(SignupActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(SignupActivity.this, "Your Email or Password is Empty", Toast.LENGTH_SHORT).show();
                }





            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}