package com.example.sistemavalidacionqrs.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.adapter.AccesoAdapter;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Accesos.AccesoResponse;
import com.example.sistemavalidacionqrs.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialActivity extends AppCompatActivity {

    private RecyclerView recyclerHistorial;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accesohistorial);

        recyclerHistorial = findViewById(R.id.recyclerHistorial);
        recyclerHistorial.setLayoutManager(
                new LinearLayoutManager(this)
        );

        apiService = ApiClient
                .getClient(this)
                .create(ApiService.class);

        sessionManager = new SessionManager(this);

        cargarHistorial();
    }

    private void cargarHistorial() {

        Integer usuarioId = getIntent().getIntExtra(
                "usuarioId",
                sessionManager.getUsuarioId()
        );

        apiService.obtenerHistorial(usuarioId)
                .enqueue(new Callback<List<AccesoResponse>>() {

                    @Override
                    public void onResponse(
                            Call<List<AccesoResponse>> call,
                            Response<List<AccesoResponse>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            AccesoAdapter adapter =
                                    new AccesoAdapter(
                                            response.body()
                                    );

                            recyclerHistorial.setAdapter(adapter);

                        } else {

                            try {

                                Log.e(
                                        "EVENTO_ERROR",
                                        response.errorBody().string()
                                );

                            } catch (Exception e) {

                                Log.e(
                                        "EVENTO_ERROR",
                                        e.getMessage()
                                );
                            }

                            Toast.makeText(
                                    HistorialActivity.this,
                                    "No se pudo cargar el historial",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<AccesoResponse>> call,
                            Throwable t) {

                        Toast.makeText(
                                HistorialActivity.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}