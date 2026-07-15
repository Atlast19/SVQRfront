package com.example.sistemavalidacionqrs.Detalle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.activities.EditarEventoActivity;
import com.example.sistemavalidacionqrs.activities.EscanearQrActivity;
import com.example.sistemavalidacionqrs.activities.ParticipantesEventoActivity;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Eventos.EventoResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventoDetalleAdminActivity extends AppCompatActivity {

    private TextView txtNombreEvento;
    private TextView txtCodigoEvento;
    private TextView txtEstadoEvento;
    private TextView txtFechaInicio;
    private TextView txtFechaFin;
    private TextView txtDescripcion;
    private Button btnEscanearQr;
    private Button btnParticipantes;
    private Button btnEditarEvento;
    private ImageView imgEvento;
    private Button btnEliminarEvento;

    private ApiService apiService;

    private Integer eventoId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalle_admin);

        txtNombreEvento = findViewById(R.id.txtNombreEvento);
        txtCodigoEvento = findViewById(R.id.txtCodigoEvento);
        txtEstadoEvento = findViewById(R.id.txtEstadoEvento);
        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFin = findViewById(R.id.txtFechaFin);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        imgEvento = findViewById(R.id.imgEvento);
        btnEscanearQr = findViewById(R.id.btnEscanearQr);
        btnParticipantes = findViewById(R.id.btnParticipantes);
        btnEditarEvento = findViewById(R.id.btnEditarEvento);
        btnEliminarEvento = findViewById(R.id.btnEliminarEvento);


        apiService = ApiClient
                .getClient(this)
                .create(ApiService.class);

        eventoId = getIntent().getIntExtra(
                "eventoId",
                -1
        );

        btnEliminarEvento.setOnClickListener(v -> {
            confirmarEliminar();
        });

        apiService = ApiClient
                .getClient(this)
                .create(ApiService.class);

        cargarEvento();

        btnEscanearQr.setOnClickListener(v -> {
            Intent intent = new Intent(
                    EventoDetalleAdminActivity.this,
                    EscanearQrActivity.class
            );

            intent.putExtra(
                    "eventoId",
                    eventoId
            );

            startActivity(intent);
        });

        btnParticipantes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    EventoDetalleAdminActivity.this,
                    ParticipantesEventoActivity.class
            );

            intent.putExtra(
                    "eventoId",
                    eventoId
            );

            startActivity(intent);
        });

        btnEditarEvento.setOnClickListener(v -> {

            Intent intent = new Intent(
                    EventoDetalleAdminActivity.this,
                    EditarEventoActivity.class
            );

            intent.putExtra(
                    "eventoId",
                    eventoId
            );

            startActivity(intent);
        });


    }

    private void confirmarEliminar() {

        new AlertDialog.Builder(this)

                .setTitle("Eliminar evento")

                .setMessage(
                        "¿Está seguro de que desea eliminar este evento?"
                )

                .setPositiveButton(
                        "Eliminar",
                        (dialog, which) -> eliminarEvento()
                )

                .setNegativeButton(
                        "Cancelar",
                        null
                )

                .show();

    }

    private void eliminarEvento() {

        apiService.eliminarEvento(eventoId)

                .enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    EventoDetalleAdminActivity.this,
                                    "Evento eliminado correctamente",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();

                        } else {

                            Toast.makeText(
                                    EventoDetalleAdminActivity.this,
                                    "No se pudo eliminar el evento",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<Void> call,
                            Throwable t) {

                        Toast.makeText(
                                EventoDetalleAdminActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                });

    }

    private void cargarEvento() {

        apiService.obtenerEventoPorId(eventoId)
                .enqueue(new Callback<EventoResponse>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(
                            Call<EventoResponse> call,
                            @NonNull Response<EventoResponse> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            EventoResponse evento = response.body();

                            txtNombreEvento.setText(evento.getNombre());
                            txtCodigoEvento.setText("Código: " + evento.getCodigo());
                            txtEstadoEvento.setText("Estado: " + evento.getEstado());
                            txtFechaInicio.setText("Fecha Inicio: " + formatearFecha(evento.getFechaInicio()));
                            txtFechaFin.setText("Fecha Fin: " + formatearFecha(evento.getFechaExpiracion()));
                            txtDescripcion.setText(evento.getDescripcion());

                            mostrarImagen(evento.getImagen());

                        } else {

                            Toast.makeText(
                                    EventoDetalleAdminActivity.this,
                                    "No se pudo cargar el evento",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<EventoResponse> call,
                            Throwable t) {

                        Toast.makeText(
                                EventoDetalleAdminActivity.this,
                                t.getMessage(),
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

    private void mostrarImagen(String imagenBase64) {

        try {

            if (imagenBase64 == null || imagenBase64.isEmpty()) {
                return;
            }

            byte[] bytes = Base64.decode(
                    imagenBase64,
                    Base64.DEFAULT
            );

            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    bytes,
                    0,
                    bytes.length
            );

            imgEvento.setImageBitmap(bitmap);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
