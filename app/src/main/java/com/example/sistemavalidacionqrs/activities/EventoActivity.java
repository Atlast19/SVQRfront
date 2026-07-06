package com.example.sistemavalidacionqrs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.adapter.EventoAdapter;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Eventos.EventoResponse;
import com.example.sistemavalidacionqrs.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventoActivity extends AppCompatActivity {

    private RecyclerView recyclerEventos;

    private FloatingActionButton fabAgregarEvento;

    private ApiService apiService;

    private EventoAdapter adapter;

    private final List<EventoResponse> listaEventos =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        SessionManager sessionManager = new SessionManager(this);

        recyclerEventos = findViewById(R.id.recyclerEventos);

        fabAgregarEvento = findViewById(R.id.fabAgregarEvento);
        if (!"ADMINISTRADOR".equalsIgnoreCase(sessionManager.getRol())) {

            fabAgregarEvento.setVisibility(View.GONE);

        }

        recyclerEventos.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EventoAdapter(listaEventos);

        recyclerEventos.setAdapter(adapter);

        apiService = ApiClient.getClient(this).create(ApiService.class);

        cargarEventos();

        fabAgregarEvento.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            EventoActivity.this,
                            CrearEventoActivity.class
                    );

            startActivity(intent);

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        cargarEventos();
    }

    private void cargarEventos() {

        apiService.obtenerEventos()
                .enqueue(new Callback<List<EventoResponse>>() {

                    @Override
                    public void onResponse(
                            Call<List<EventoResponse>> call,
                            Response<List<EventoResponse>> response) {

                        if(response.isSuccessful()
                                && response.body() != null) {

                            listaEventos.clear();

                            listaEventos.addAll(
                                    response.body()
                            );

                            adapter.notifyDataSetChanged();

                        } else {

                            Toast.makeText(
                                    EventoActivity.this,
                                    "No se pudieron cargar los eventos",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<EventoResponse>> call,
                            Throwable t) {

                        Toast.makeText(
                                EventoActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
