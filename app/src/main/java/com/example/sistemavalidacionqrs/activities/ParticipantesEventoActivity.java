package com.example.sistemavalidacionqrs.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.adapter.ParticipanteAdapter;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Accesos.AccesoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParticipantesEventoActivity extends AppCompatActivity {

    private RecyclerView recyclerParticipantes;

    private ApiService apiService;

    private Integer eventoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participantes_evento);

        recyclerParticipantes =
                findViewById(R.id.recyclerParticipantes);

        recyclerParticipantes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        apiService =
                ApiClient.getClient(this)
                        .create(ApiService.class);

        eventoId =
                getIntent().getIntExtra(
                        "eventoId",
                        0
                );

        cargarParticipantes();
    }

    private void cargarParticipantes() {

        apiService.obtenerParticipantesEvento(eventoId)
                .enqueue(new Callback<List<AccesoResponse>>() {


                    @Override
                    public void onResponse(
                            Call<List<AccesoResponse>> call,
                            Response<List<AccesoResponse>> response) {


                        if(response.isSuccessful()
                                && response.body()!=null){


                            ParticipanteAdapter adapter =
                                    new ParticipanteAdapter(
                                            response.body()
                                    );


                            recyclerParticipantes
                                    .setAdapter(adapter);



                        }else{


                            Toast.makeText(
                                    ParticipantesEventoActivity.this,
                                    "No hay participantes registrados",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }



                    @Override
                    public void onFailure(
                            Call<List<AccesoResponse>> call,
                            Throwable t) {


                        Toast.makeText(
                                ParticipantesEventoActivity.this,
                                "Error: "
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();


                    }
                });
    }
}
