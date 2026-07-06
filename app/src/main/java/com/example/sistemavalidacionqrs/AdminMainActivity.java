package com.example.sistemavalidacionqrs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemavalidacionqrs.activities.EscanearQrActivity;
import com.example.sistemavalidacionqrs.activities.EventoActivity;
import com.example.sistemavalidacionqrs.activities.GestionUsuariosActivity;
import com.example.sistemavalidacionqrs.activities.LoginActivity;
import com.example.sistemavalidacionqrs.activities.PerfilActivity;
import com.example.sistemavalidacionqrs.utils.SessionManager;

public class AdminMainActivity extends AppCompatActivity {

    private TextView txtNombreAdmin;
    private Button btnEventos;
    private Button btnGestionUsuarios;
    private Button btnPerfil;
    private Button btnCerrarSesion;

    private SessionManager sessionManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        sessionManager = new SessionManager(this);

        txtNombreAdmin = findViewById(R.id.txtNombreAdmin);

        btnEventos = findViewById(R.id.btnEventos);
        btnGestionUsuarios = findViewById(R.id.btnGestionUsuarios);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        txtNombreAdmin.setText(
                "Bienvenido, " +
                        sessionManager.getNombre() +
                        " " +
                        sessionManager.getApellido()
        );

        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, PerfilActivity.class);
            startActivity(intent);
        });

        btnEventos.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, EventoActivity.class);
            startActivity(intent);
        });

        btnGestionUsuarios.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, GestionUsuariosActivity.class);
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {

            sessionManager.cerrarSesion();

            Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        txtNombreAdmin.setText(
                "Bienvenido, " +
                        sessionManager.getNombre() +
                        " " +
                        sessionManager.getApellido()
        );

    }
}