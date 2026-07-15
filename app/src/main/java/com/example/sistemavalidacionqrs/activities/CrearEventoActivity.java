package com.example.sistemavalidacionqrs.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Eventos.EventoRequest;
import com.example.sistemavalidacionqrs.model.Eventos.EventoResponse;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearEventoActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtDescripcion;
    private EditText txtFechaInicio;
    private EditText txtFechaExpiracion;

    private Button btnCrearEvento;
    private Button btnSeleccionarImagen;

    private ImageView imgEvento;

    private Uri imagenSeleccionada;

    private String fechaInicioSeleccionada;
    private String fechaExpiracionSeleccionada;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);


        txtNombre = findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaExpiracion = findViewById(R.id.txtFechaExpiracion);

        btnCrearEvento = findViewById(R.id.btnCrearEvento);
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);

        imgEvento = findViewById(R.id.imgEvento);


        apiService = ApiClient
                .getClient(this)
                .create(ApiService.class);

        btnSeleccionarImagen.setOnClickListener(v ->
                abrirGaleria());

        txtFechaInicio.setOnClickListener(v ->
                seleccionarFechaHora(true));

        txtFechaExpiracion.setOnClickListener(v ->
                seleccionarFechaHora(false));

        btnCrearEvento.setOnClickListener(v ->
                crearEvento());
    }

    private final ActivityResultLauncher<Intent> seleccionarImagenLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if(result.getResultCode() == RESULT_OK
                                && result.getData() != null){

                            imagenSeleccionada = result.getData().getData();

                            imgEvento.setImageURI(
                                    imagenSeleccionada
                            );
                        }
                    });

    private void abrirGaleria() {

        Intent intent =
                new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );

        seleccionarImagenLauncher.launch(intent);
    }

    private void seleccionarFechaHora(boolean inicio) {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view, year, month, day) -> {

                            TimePickerDialog timePickerDialog =
                                    new TimePickerDialog(
                                            this,
                                            (timeView, hour, minute) -> {

                                                String fechaApi =
                                                        String.format(
                                                                Locale.getDefault(),
                                                                "%04d-%02d-%02dT%02d:%02d:00",
                                                                year,
                                                                month + 1,
                                                                day,
                                                                hour,
                                                                minute
                                                        );

                                                String fechaVista =
                                                        String.format(
                                                                Locale.getDefault(),
                                                                "%02d/%02d/%04d %02d:%02d",
                                                                day,
                                                                month + 1,
                                                                year,
                                                                hour,
                                                                minute
                                                        );

                                                if(inicio){

                                                    fechaInicioSeleccionada =
                                                            fechaApi;

                                                    txtFechaInicio.setText(
                                                            fechaVista
                                                    );

                                                }else{

                                                    fechaExpiracionSeleccionada =
                                                            fechaApi;

                                                    txtFechaExpiracion.setText(
                                                            fechaVista
                                                    );
                                                }

                                            },
                                            calendar.get(Calendar.HOUR_OF_DAY),
                                            calendar.get(Calendar.MINUTE),
                                            true
                                    );

                            timePickerDialog.show();

                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

        datePickerDialog.show();
    }

    private void crearEvento() {

        EventoRequest request =
                new EventoRequest();

        request.setNombre(
                txtNombre.getText().toString().trim()
        );

        request.setDescripcion(
                txtDescripcion.getText().toString().trim()
        );

        request.setFechaInicio(
                fechaInicioSeleccionada
        );

        request.setFechaExpiracion(
                fechaExpiracionSeleccionada
        );

        if(imagenSeleccionada != null){

            request.setImagen(
                    convertirImagenBase64(imagenSeleccionada)
            );
        }

        Log.d(
                "EVENTO_REQUEST",
                new Gson().toJson(request)
        );
        apiService.crearEvento(request).enqueue(new Callback<EventoResponse>() {

                    @Override
                    public void onResponse(Call<EventoResponse> call, Response<EventoResponse> response) {

                        Log.d("EVENTO_HTTP", "Código: " + response.code());

                        if(response.isSuccessful()){

                            Toast.makeText(
                                    CrearEventoActivity.this,
                                    "Evento creado correctamente",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();

                        }else{
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
                                    CrearEventoActivity.this,
                                    "No se pudo crear el evento",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EventoResponse> call, Throwable t) {

                        Toast.makeText(
                                CrearEventoActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
        });
    }


    private String convertirImagenBase64(Uri uri){

        try{

            InputStream inputStream =
                    getContentResolver().openInputStream(uri);

            Bitmap bitmap =
                    BitmapFactory.decodeStream(inputStream);

            ByteArrayOutputStream baos =
                    new ByteArrayOutputStream();

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    80,
                    baos
            );

            byte[] imageBytes =
                    baos.toByteArray();

            return Base64.encodeToString(
                    imageBytes,
                    Base64.DEFAULT
            );

        }catch (Exception e){

            e.printStackTrace();

            return null;

        }

    }
}
