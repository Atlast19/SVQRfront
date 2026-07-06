package com.example.sistemavalidacionqrs.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Usuarios.UsuarioRequest;
import com.example.sistemavalidacionqrs.model.Usuarios.UsuarioResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearUsuarioActivity extends AppCompatActivity {

    private EditText txtMatricula;
    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtEmail;
    private EditText txtPassword;

    private Button btnCrearUsuario;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        txtMatricula = findViewById(R.id.txtMatricula);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        btnCrearUsuario = findViewById(R.id.btnCrearUsuario);

        apiService = ApiClient
                .getClient(this)
                .create(ApiService.class);

        btnCrearUsuario.setOnClickListener(v -> crearUsuario());
    }

    private void crearUsuario() {

        String matricula = txtMatricula.getText().toString().trim();
        String nombre = txtNombre.getText().toString().trim();
        String apellido = txtApellido.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if (matricula.isEmpty()) {
            txtMatricula.setError("Ingrese la matrícula");
            return;
        }

        if (nombre.isEmpty()) {
            txtNombre.setError("Ingrese el nombre");
            return;
        }

        if (apellido.isEmpty()) {
            txtApellido.setError("Ingrese el apellido");
            return;
        }

        if (email.isEmpty()) {
            txtEmail.setError("Ingrese el correo");
            return;
        }

        if (password.isEmpty()) {
            txtPassword.setError("Ingrese la contraseña");
            return;
        }

        UsuarioRequest request = new UsuarioRequest();

        request.setMatricula(matricula);
        request.setNombre(nombre);
        request.setApellido(apellido);
        request.setEmail(email);
        request.setPassword(password);

        apiService.crearUsuario(request)
                .enqueue(new Callback<UsuarioResponse>() {

                    @Override
                    public void onResponse(
                            Call<UsuarioResponse> call,
                            Response<UsuarioResponse> response) {

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    CrearUsuarioActivity.this,
                                    "Usuario creado correctamente",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();

                        } else {

                            Toast.makeText(
                                    CrearUsuarioActivity.this,
                                    "No se pudo crear el usuario",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<UsuarioResponse> call,
                            Throwable t) {

                        Toast.makeText(
                                CrearUsuarioActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
