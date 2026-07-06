package com.example.sistemavalidacionqrs.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;

import com.example.sistemavalidacionqrs.model.QrTokens.GenerarQrResponse;
import com.example.sistemavalidacionqrs.utils.SessionManager;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GenerarQrActivity extends AppCompatActivity {


    private ImageView imgQr;
    private TextView txtExpiracion;
    private Button btnGenerar;

    private TextView txtNombre;
    private TextView txtMatricula;

    private ApiService apiService;
    private Integer eventoId;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generar_qr);


        imgQr = findViewById(R.id.imgQr);

        txtNombre = findViewById(R.id.txtNombre);

        txtMatricula = findViewById(R.id.txtMatricula);

        txtExpiracion = findViewById(R.id.txtExpiracion);

        btnGenerar = findViewById(R.id.btnGenerar);


        apiService = ApiClient.getClient(this).create(ApiService.class);

        sessionManager = new SessionManager(this);

        btnGenerar.setOnClickListener(v -> {
            generarQr();
        });
    }


    @SuppressLint("SetTextI18n")
    private void generarQr() {

        eventoId = getIntent().getIntExtra(
                "eventoId",
                -1
        );

        Integer usuarioId = sessionManager.getUsuarioId();

        txtNombre.setText(sessionManager.getNombre());

        txtMatricula.setText("Matricula: " + sessionManager.getMatricula());



        apiService.generarQr(usuarioId, eventoId).enqueue(new Callback<GenerarQrResponse>() {


            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<GenerarQrResponse> call, Response<GenerarQrResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    GenerarQrResponse qr = response.body();

                    mostrarQr(qr.getQrBase64());

                    txtExpiracion.setText("Válido hasta: " + formatearFecha(qr.getFechaExpiracion()));


                } else {

                    Toast.makeText(GenerarQrActivity.this, "Error generando QR", Toast.LENGTH_SHORT).show();

                }

            }


            @Override
            public void onFailure(Call<GenerarQrResponse> call, Throwable t) {

                Toast.makeText(GenerarQrActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }


        });

    }

    private String formatearFecha(String fechaIso) {

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                LocalDateTime fecha = LocalDateTime.parse(fechaIso);

                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern(
                                "dd MMM yyyy - hh:mm a"
                        );

                return fecha.format(formatter);
            }

            return fechaIso;

        } catch (Exception e) {

            return fechaIso;
        }
    }

    private void mostrarQr(String base64){

        byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        imgQr.setImageBitmap(bitmap);

    }


}