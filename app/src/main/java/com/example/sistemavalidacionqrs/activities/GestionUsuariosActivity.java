package com.example.sistemavalidacionqrs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.adapter.UsuarioAdapter;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Usuarios.UsuarioResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestionUsuariosActivity extends AppCompatActivity {

    private RecyclerView recyclerUsuarios;
    private EditText txtBuscar;
    private UsuarioAdapter usuarioAdapter;
    private FloatingActionButton btnAgregarUsuario;

    private List<UsuarioResponse> listaUsuarios;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_usuarios);

        getWindow()
                .setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );

        btnAgregarUsuario = findViewById(R.id.btnAgregarUsuario);
        recyclerUsuarios = findViewById(R.id.recyclerUsuarios);
        txtBuscar = findViewById(R.id.txtBuscar);

        btnAgregarUsuario.setOnClickListener(v -> {

            Intent intent = new Intent(
                    GestionUsuariosActivity.this,
                    CrearUsuarioActivity.class
            );

            startActivity(intent);

        });

        // RecyclerView
        recyclerUsuarios.setLayoutManager(
                new LinearLayoutManager(this)
        );

        listaUsuarios = new ArrayList<>();

        usuarioAdapter = new UsuarioAdapter(listaUsuarios);

        recyclerUsuarios.setAdapter(usuarioAdapter);


        apiService = ApiClient
                .getClient(this)
                .create(ApiService.class);


        cargarUsuarios();


        txtBuscar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {

            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {

                usuarioAdapter.filtrar(
                        s.toString()
                );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        cargarUsuarios();
    }

    private void cargarUsuarios() {

        apiService.obtenerTodosLosUsuarios()
                .enqueue(new Callback<List<UsuarioResponse>>() {

                    @Override
                    public void onResponse(
                            Call<List<UsuarioResponse>> call,
                            Response<List<UsuarioResponse>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            usuarioAdapter.actualizarLista(
                                    response.body()
                            );

                        } else {

                            Toast.makeText(
                                    GestionUsuariosActivity.this,
                                    "No se pudieron cargar los usuarios",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<UsuarioResponse>> call,
                            Throwable t) {

                        Toast.makeText(
                                GestionUsuariosActivity.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
