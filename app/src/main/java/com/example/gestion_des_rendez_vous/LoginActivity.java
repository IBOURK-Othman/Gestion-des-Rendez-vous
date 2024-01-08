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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        loginEmail = findViewById(R.id.login_emil);
        loginPassword = findViewById(R.id.login_password);
        signupRedirectText = findViewById(R.id.signRedirectText);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() || !validatePassword()){
                    Toast.makeText(LoginActivity.this, "Accès refusé.", Toast.LENGTH_SHORT).show();
                }else

                {
                    checkUser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }
    public Boolean validateUsername(){
        String val= loginEmail.getText().toString();
        if (val.isEmpty()){
            loginEmail.setError("Le nom d'utilisateur ne peut pas être vide.");
            return false;
        }else {
            loginEmail.setError(null);
            return  true;
        }
    }

    public Boolean validatePassword(){
        String val= loginPassword.getText().toString();
        if (val.isEmpty()){
            loginPassword.setError("Le mot de passe ne peut pas être vide.");
            return false;
        }else {
            loginPassword.setError(null);
            return  true;
        }
    }
    public  void  checkUser(){
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();




        if(email != null && password != null){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)

                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText( getApplicationContext(), "Vous avez connecté avec succès !", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                finish();

                            }else {
                                Toast.makeText(getApplicationContext(), "Votre adresse e-mail ou votre mot de passe est incorrect. ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(getApplicationContext(), "Votre adresse e-mail ou votre mot de passe est vide", Toast.LENGTH_SHORT).show();
        }



    }
}