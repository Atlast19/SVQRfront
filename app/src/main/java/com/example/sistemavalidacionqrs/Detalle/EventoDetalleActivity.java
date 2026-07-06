package com.example.sistemavalidacionqrs.Detalle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.activities.GenerarQrActivity;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Eventos.EventoResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventoDetalleActivity extends AppCompatActivity {

    private TextView txtNombreEvento;
    private TextView txtCodigoEvento;
    private TextView txtEstadoEvento;
    private TextView txtFechaInicio;
    private TextView txtFechaFin;
    private TextView txtDescripcion;

    private Button btnGenerarQr;

    private ApiService apiService;

    private Integer eventoId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalle);

        txtNombreEvento =
                findViewById(R.id.txtNombreEvento);

        txtCodigoEvento =
                findViewById(R.id.txtCodigoEvento);

        txtEstadoEvento =
                findViewById(R.id.txtEstadoEvento);

        txtFechaInicio =
                findViewById(R.id.txtFechaInicio);

        txtFechaFin =
                findViewById(R.id.txtFechaFin);

        txtDescripcion =
                findViewById(R.id.txtDescripcion);

        btnGenerarQr =
                findViewById(R.id.btnGenerarQr);

        apiService =
                ApiClient.getClient(this)
                        .create(ApiService.class);

        eventoId =
                getIntent().getIntExtra(
                        "eventoId",
                        0
                );

        cargarEvento();

        btnGenerarQr.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            EventoDetalleActivity.this,
                            GenerarQrActivity.class
                    );

            intent.putExtra(
                    "eventoId",
                    eventoId
            );

            startActivity(intent);
        });
    }

    private void cargarEvento() {

        apiService.obtenerEventoPorId(eventoId)
                .enqueue(new Callback<EventoResponse>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(
                            Call<EventoResponse> call,
                            Response<EventoResponse> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            EventoResponse evento = response.body();

                            txtNombreEvento.setText(evento.getNombre());
                            txtCodigoEvento.setText("Código: " + evento.getCodigo());
                            txtEstadoEvento.setText("Estado: " + evento.getEstado());
                            txtFechaInicio.setText("Fecha Inicio: " + formatearFecha(evento.getFechaInicio()));
                            txtFechaFin.setText("Fecha Fin: " + formatearFecha(evento.getFechaExpiracion()));
                            txtDescripcion.setText(evento.getDescripcion());

                        } else {

                            Toast.makeText(
                                    EventoDetalleActivity.this,
                                    "No se pudo cargar el evento",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<EventoResponse> call,
                            Throwable t) {

                        Toast.makeText(
                                EventoDetalleActivity.this,
                                "Error: "
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }


    private String formatearFecha(String fecha) {

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                LocalDateTime dateTime = LocalDateTime.parse(fecha);

                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy hh:mm a",
                                Locale.US
                        );

                return dateTime.format(formatter);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return fecha;
    }
}
