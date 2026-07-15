package com.example.sistemavalidacionqrs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemavalidacionqrs.AdminMainActivity;
import com.example.sistemavalidacionqrs.MainActivity;
import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Login.AuthRequest;
import com.example.sistemavalidacionqrs.model.Login.AuthResponse;
import com.example.sistemavalidacionqrs.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnLogin;

    private TextView txtCrearCuenta;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.estaLogueado()) {

            Intent intent;

            if ("ADMINISTRADOR".equalsIgnoreCase(sessionManager.getRol())) {

                intent = new Intent(
                        LoginActivity.this,
                        AdminMainActivity.class
                );

            } else {

                intent = new Intent(
                        LoginActivity.this,
                        MainActivity.class
                );
            }

            startActivity(intent);
            finish();
            return;
        }


        txtCrearCuenta = findViewById(R.id.txtCrearCuenta);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        apiService = ApiClient
                .getClient(this)
                .create(ApiService.class);


        btnLogin.setOnClickListener(v -> iniciarSesion());
        txtCrearCuenta.setOnClickListener(v -> {Intent intent = new Intent(LoginActivity.this, CrearUsuarioActivity.class);startActivity(intent);});
    }

    private void iniciarSesion() {

        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if (email.isEmpty()) {
            txtEmail.setError("Ingrese el correo");
            return;
        }

        if (password.isEmpty()) {
            txtPassword.setError("Ingrese la contraseña");
            return;
        }

        AuthRequest request = new AuthRequest();
        request.setEmail(email);
        request.setPassword(password);

        apiService.login(request).enqueue(new Callback<AuthResponse>() {

            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    AuthResponse authResponse = response.body();

                    SessionManager sessionManager = new SessionManager(LoginActivity.this);

                    sessionManager.guardarSesion(
                            authResponse.getToken(),
                            authResponse.getUsuarioId(),
                            authResponse.getMatricula(),
                            authResponse.getNombre(),
                            authResponse.getApellido(),
                            authResponse.getEmail(),
                            authResponse.getRol()
                    );

                    Toast.makeText(
                            LoginActivity.this,
                            "Inicio de sesión exitoso",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent;

                    Log.d("SESSION", "Rol guardado: " + sessionManager.getRol());

                    if ("ADMINISTRADOR".equalsIgnoreCase(authResponse.getRol())) {

                        intent = new Intent(
                                LoginActivity.this,
                                AdminMainActivity.class
                        );

                    } else {

                        intent = new Intent(
                                LoginActivity.this,
                                MainActivity.class
                        );
                    }

                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(
                            LoginActivity.this,
                            "Correo o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {

                Toast.makeText(
                        LoginActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();

                Log.e("LOGIN_ERROR", t.getMessage());
            }
        });
    }
}
