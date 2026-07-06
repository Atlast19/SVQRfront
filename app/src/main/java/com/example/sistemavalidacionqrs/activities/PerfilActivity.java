package com.example.sistemavalidacionqrs.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.utils.SessionManager;

public class PerfilActivity extends AppCompatActivity {

    private TextView txtNombre;
    private TextView txtMatricula;
    private TextView txtEmail;
    private TextView txtRol;

    private Button btnEditarPerfil;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        txtNombre = findViewById(R.id.txtNombre);
        txtMatricula = findViewById(R.id.txtMatricula);
        txtEmail = findViewById(R.id.txtEmail);
        txtRol = findViewById(R.id.txtRol);

        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);

        sessionManager = new SessionManager(this);

        // Cargar datos la primera vez
        cargarDatos();

        btnEditarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(
                    PerfilActivity.this,
                    EditarPerfilActivity.class
            );
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Volver a cargar los datos cada vez que esta Activity vuelve al frente
        cargarDatos();

        Log.d("PERFIL", "Nombre: " + sessionManager.getNombre());
        Log.d("PERFIL", "Apellido: " + sessionManager.getApellido());
        Log.d("PERFIL", "Email: " + sessionManager.getEmail());
    }

    @SuppressLint("SetTextI18n")
    private void cargarDatos() {

        txtNombre.setText(
                sessionManager.getNombre() + " " +
                        sessionManager.getApellido()
        );

        txtMatricula.setText(sessionManager.getMatricula());
        txtEmail.setText(sessionManager.getEmail());
        txtRol.setText(sessionManager.getRol());
    }
}
