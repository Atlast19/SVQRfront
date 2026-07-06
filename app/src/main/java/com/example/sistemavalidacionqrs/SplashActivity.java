package com.example.sistemavalidacionqrs;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemavalidacionqrs.activities.LoginActivity;
import com.example.sistemavalidacionqrs.utils.SessionManager;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager =
                new SessionManager(this);

        Intent intent;

        if (sessionManager.estaLogueado()) {

            if ("ADMINISTRADOR".equalsIgnoreCase(
                    sessionManager.getRol())) {

                intent = new Intent(
                        this,
                        AdminMainActivity.class
                );

            } else {

                intent = new Intent(
                        this,
                        MainActivity.class
                );
            }

        } else {

            intent = new Intent(
                    this,
                    LoginActivity.class
            );
        }

        startActivity(intent);
        finish();
    }
}
