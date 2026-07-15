package com.example.sistemavalidacionqrs.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Accesos.AccesoResponse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EscanearQrActivity extends AppCompatActivity {


    private Button btnEscanear;
    private TextView txtResultado;


    private static final int REQUEST_CAMERA = 100;


    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escanear_qr);


        btnEscanear = findViewById(R.id.btnEscanear);
        txtResultado = findViewById(R.id.txtResultado);



        apiService =
                ApiClient
                        .getClient(this)
                        .create(ApiService.class);



        btnEscanear.setOnClickListener(v -> {

            iniciarEscaneo();

        });

    }



    private void iniciarEscaneo(){


        IntentIntegrator integrator =
                new IntentIntegrator(this);


        integrator.setDesiredBarcodeFormats(
                IntentIntegrator.QR_CODE
        );


        integrator.setPrompt(
                "Escanee el QR del estudiante"
        );


        integrator.setCameraId(0);


        integrator.setBeepEnabled(true);


        integrator.setOrientationLocked(false);


        integrator.initiateScan();


    }





    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {


        super.onActivityResult(
                requestCode,
                resultCode,
                data);



        IntentResult result =
                IntentIntegrator.parseActivityResult(
                        requestCode,
                        resultCode,
                        data);



        if(result != null){


            if(result.getContents()!=null){


                String token =
                        result.getContents();



                validarAcceso(token);


            }

        }


    }



    private void validarAcceso(String token){


        apiService.registrarAcceso(token)
                .enqueue(new Callback<AccesoResponse>() {


                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(
                            Call<AccesoResponse> call,
                            Response<AccesoResponse> response) {



                        if(response.isSuccessful()
                                && response.body()!=null){


                            AccesoResponse acceso =
                                    response.body();



                            txtResultado.setText(

                                    "✓ ACCESO APROBADO\n\n"+
                                            "Matrícula: "
                                            + acceso.getMatricula()
                                            + "\n\nEstado: "
                                            + acceso.getEstado()

                            );


                        }else{


                            txtResultado.setText(
                                    "✗ Acceso denegado"
                            );

                        }


                    }



                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFailure(
                            Call<AccesoResponse> call,
                            Throwable t) {


                        txtResultado.setText(
                                "Error conexión: "
                                        + t.getMessage()
                        );


                    }


                });


    }

}
