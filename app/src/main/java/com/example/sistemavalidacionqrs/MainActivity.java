package com.example.sistemavalidacionqrs;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemavalidacionqrs.activities.EscanearQrActivity;
import com.example.sistemavalidacionqrs.activities.EventoActivity;
import com.example.sistemavalidacionqrs.activities.GenerarQrActivity;
import com.example.sistemavalidacionqrs.activities.HistorialActivity;
import com.example.sistemavalidacionqrs.activities.LoginActivity;
import com.example.sistemavalidacionqrs.activities.PerfilActivity;
import com.example.sistemavalidacionqrs.adapter.EventoAdapter;
import com.example.sistemavalidacionqrs.utils.SessionManager;


public class MainActivity extends AppCompatActivity {

    private TextView txtBienvenida;
    private TextView txtRol;
    private Button btnCerrarSesion;
    private Button btnEventos;
    private Button btnHistorial;
    private Button btnPerfil;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEventos = findViewById(R.id.btnEventos);
        btnHistorial = findViewById(R.id.btnHistorial);
        txtBienvenida = findViewById(R.id.txtBienvenida);

        btnPerfil = findViewById(R.id.btnPerfil);
        txtRol = findViewById(R.id.txtRol);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        SessionManager session = new SessionManager(this);

        txtBienvenida.setText("Bienvenido " + session.getNombre());
        txtRol.setText("Rol: " + session.getRol());

        btnEventos.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, EventoActivity.class);
            startActivity(intent);
        });

        btnPerfil.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
            startActivity(intent);
        });

        btnHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistorialActivity.class);
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {

            session.cerrarSesion();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            finish();
        });
    }
}