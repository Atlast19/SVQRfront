package com.example.sistemavalidacionqrs.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Usuarios.UsuarioResponse;
import com.example.sistemavalidacionqrs.model.Usuarios.UsuarioUpdateRequest;
import com.example.sistemavalidacionqrs.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilActivity extends AppCompatActivity {


    private EditText etNombre;
    private EditText etApellido;
    private EditText etEmail;
    private EditText etPassword;

    private Button btnGuardar;


    private Integer usuarioId;


    private SessionManager sessionManager;
    private ApiService apiService;


    private boolean esAdminEditando = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);



        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnGuardar = findViewById(R.id.btnGuardar);



        sessionManager = new SessionManager(this);



        apiService = ApiClient
                .getClient(this)
                .create(ApiService.class);


        if(getIntent().hasExtra("usuarioId")){


            usuarioId =
                    getIntent()
                            .getIntExtra(
                                    "usuarioId",
                                    -1
                            );


            esAdminEditando = true;


            cargarUsuario(usuarioId);



        }else{


            usuarioId =
                    sessionManager.getUsuarioId();



            etNombre.setText(
                    sessionManager.getNombre()
            );


            etApellido.setText(
                    sessionManager.getApellido()
            );


            etEmail.setText(
                    sessionManager.getEmail()
            );

        }



        btnGuardar.setOnClickListener(v ->
                actualizarPerfil()
        );


    }




    private void cargarUsuario(Integer id){


        apiService.obtenerUsuarioPorId(id)
                .enqueue(new Callback<UsuarioResponse>() {


                    @Override
                    public void onResponse(
                            Call<UsuarioResponse> call,
                            Response<UsuarioResponse> response) {


                        if(response.isSuccessful()
                                && response.body()!=null){


                            UsuarioResponse usuario =
                                    response.body();



                            etNombre.setText(
                                    usuario.getNombre()
                            );


                            etApellido.setText(
                                    usuario.getApellido()
                            );


                            etEmail.setText(
                                    usuario.getEmail()
                            );


                        }

                    }



                    @Override
                    public void onFailure(
                            Call<UsuarioResponse> call,
                            Throwable t) {


                        Toast.makeText(
                                EditarPerfilActivity.this,
                                "Error cargando usuario",
                                Toast.LENGTH_SHORT
                        ).show();

                    }
                });

    }





    private void actualizarPerfil(){


        String nombre =
                etNombre.getText()
                        .toString()
                        .trim();



        String apellido =
                etApellido.getText()
                        .toString()
                        .trim();



        String email =
                etEmail.getText()
                        .toString()
                        .trim();



        String password =
                etPassword.getText()
                        .toString()
                        .trim();




        if(nombre.isEmpty()){

            etNombre.setError(
                    "Ingrese el nombre"
            );

            return;
        }




        if(apellido.isEmpty()){

            etApellido.setError(
                    "Ingrese el apellido"
            );

            return;
        }




        if(email.isEmpty()){

            etEmail.setError(
                    "Ingrese el correo"
            );

            return;
        }





        UsuarioUpdateRequest request =
                new UsuarioUpdateRequest();


        request.setNombre(nombre);
        request.setApellido(apellido);
        request.setEmail(email);
        request.setPassword(password);




        apiService.actualizarUsuario(
                        usuarioId,
                        request
                )
                .enqueue(new Callback<UsuarioResponse>() {



                    @Override
                    public void onResponse(
                            Call<UsuarioResponse> call,
                            Response<UsuarioResponse> response) {



                        if(response.isSuccessful()
                                && response.body()!=null){



                            UsuarioResponse usuario =
                                    response.body();





                            if(!esAdminEditando){



                                sessionManager.guardarSesion(
                                        sessionManager.getToken(),
                                        sessionManager.getUsuarioId(),
                                        sessionManager.getMatricula(),
                                        usuario.getNombre(),
                                        usuario.getApellido(),
                                        usuario.getEmail(),
                                        sessionManager.getRol()
                                );

                            }




                            Toast.makeText(
                                    EditarPerfilActivity.this,
                                    "Usuario actualizado correctamente",
                                    Toast.LENGTH_SHORT
                            ).show();



                            finish();



                        }else{


                            Toast.makeText(
                                    EditarPerfilActivity.this,
                                    "No fue posible actualizar",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }




                    @Override
                    public void onFailure(
                            Call<UsuarioResponse> call,
                            Throwable t) {


                        Toast.makeText(
                                EditarPerfilActivity.this,
                                "Error: "
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();


                    }
                });

    }

}
