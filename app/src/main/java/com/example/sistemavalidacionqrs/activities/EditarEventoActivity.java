package com.example.sistemavalidacionqrs.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Eventos.EventoRequest;
import com.example.sistemavalidacionqrs.model.Eventos.EventoResponse;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarEventoActivity extends AppCompatActivity {
    private EditText txtNombre;
    private EditText txtDescripcion;
    private EditText txtFechaInicio;
    private EditText txtFechaExpiracion;
    private Button btnGuardar;
    private Button btnSeleccionarImagen;

    private ImageView imgEvento;

    private Uri imagenSeleccionada;

    private String fechaInicioSeleccionada;
    private String fechaExpiracionSeleccionada;

    private String imagenActual;

    private Integer eventoId;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editar_evento);


        txtNombre = findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaExpiracion = findViewById(R.id.txtFechaExpiracion);



        btnGuardar = findViewById(R.id.btnGuardar);
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);

        imgEvento = findViewById(R.id.imgEvento);

        apiService =
                ApiClient
                        .getClient(this)
                        .create(ApiService.class);

        eventoId =
                getIntent().getIntExtra(
                        "eventoId",
                        0
                );

        configurarSpinner();

        cargarEvento();

        btnSeleccionarImagen.setOnClickListener(v ->
                abrirGaleria());

        txtFechaInicio.setOnClickListener(v ->
                seleccionarFechaHora(true));

        txtFechaExpiracion.setOnClickListener(v ->
                seleccionarFechaHora(false));

        btnGuardar.setOnClickListener(v ->
                actualizarEvento());

    }

    private final ActivityResultLauncher<Intent> seleccionarImagenLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            imagenSeleccionada =
                                    result.getData().getData();

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

        Calendar calendar =
                Calendar.getInstance();

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

                                                if (inicio) {

                                                    fechaInicioSeleccionada =
                                                            fechaApi;

                                                    txtFechaInicio.setText(
                                                            fechaVista
                                                    );

                                                } else {

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

    private void configurarSpinner() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        new String[]{
                                "ACTIVO",
                                "INACTIVO"
                        });

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

    }

    private void cargarEvento() {


        apiService.obtenerEventoPorId(eventoId)
                .enqueue(new Callback<EventoResponse>() {


                    @Override
                    public void onResponse(
                            Call<EventoResponse> call,
                            Response<EventoResponse> response) {


                        if(response.isSuccessful()
                                && response.body() != null){


                            EventoResponse evento =
                                    response.body();

                            txtNombre.setText(evento.getNombre());
                            txtDescripcion.setText(evento.getDescripcion());


                            imagenActual = evento.getImagen();



                            if (imagenActual != null && !imagenActual.isEmpty()) {

                                mostrarImagenBase64(imagenActual);

                            }



                            if(evento.getFechaInicio() != null){


                                fechaInicioSeleccionada =
                                        evento.getFechaInicio()
                                                .toString();


                                txtFechaInicio.setText(
                                        formatearFecha(
                                                evento.getFechaInicio()
                                        )
                                );

                            }



                            if(evento.getFechaExpiracion() != null){


                                fechaExpiracionSeleccionada =
                                        evento.getFechaExpiracion()
                                                .toString();


                                txtFechaExpiracion.setText(
                                        formatearFecha(
                                                evento.getFechaExpiracion()
                                        )
                                );

                            }

                        }

                    }



                    @Override
                    public void onFailure(
                            Call<EventoResponse> call,
                            Throwable t) {


                        Toast.makeText(
                                EditarEventoActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                });

    }



    private String formatearFecha(
            String fecha){


        try {


            LocalDateTime date =
                    null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                date = LocalDateTime.parse(
                        fecha
                );
            }


            DateTimeFormatter formatter =
                    null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm"
                );
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return date.format(formatter);
            }


        }catch(Exception e){


            return fecha;

        }

        return fecha;
    }




    private void actualizarEvento(){


        EventoRequest request =
                new EventoRequest();


        request.setNombre(
                txtNombre.getText()
                        .toString()
                        .trim()
        );


        request.setDescripcion(
                txtDescripcion.getText()
                        .toString()
                        .trim()
        );



        if(imagenSeleccionada != null){


            request.setImagen(
                    convertirImagenBase64(imagenSeleccionada)
            );


        }else{


            request.setImagen(imagenActual);

        }


        request.setFechaInicio(fechaInicioSeleccionada);
        request.setFechaExpiracion(fechaExpiracionSeleccionada);


        apiService.actualizarEvento(eventoId, request).enqueue(new Callback<EventoResponse>() {

                    @Override
                    public void onResponse(
                            Call<EventoResponse> call,
                            Response<EventoResponse> response) {



                        if(response.isSuccessful()){


                            Toast.makeText(
                                    EditarEventoActivity.this,
                                    "Evento actualizado correctamente",
                                    Toast.LENGTH_SHORT
                            ).show();



                            finish();



                        }else{

                            Toast.makeText(
                                    EditarEventoActivity.this,
                                    "No se pudo actualizar el evento",
                                    Toast.LENGTH_SHORT
                            ).show();


                        }

                    }




                    @Override
                    public void onFailure(
                            Call<EventoResponse> call,
                            Throwable t) {


                        Toast.makeText(
                                EditarEventoActivity.this,
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

    private void mostrarImagenBase64(String imagenBase64) {

        try {

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
