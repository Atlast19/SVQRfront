package com.example.sistemavalidacionqrs.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "SistemaValidacionQR";

    private static final String KEY_TOKEN = "token";
    private static final String KEY_USUARIO_ID = "usuarioId";
    private static final String KEY_MATRICULA = "matricula";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_APELLIDO = "apellido";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROL = "rol";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void guardarSesion(String token, Integer usuarioId,String matricula, String nombre,String apellido, String email, String rol) {

        editor.putString(KEY_TOKEN, token);
        editor.putInt(KEY_USUARIO_ID, usuarioId);
        editor.putString(KEY_MATRICULA, matricula);
        editor.putString(KEY_NOMBRE, nombre);
        editor.putString(KEY_APELLIDO, apellido);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROL, rol);

        editor.apply();
    }

    public String getToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

    public Integer getUsuarioId() {
        return preferences.getInt(KEY_USUARIO_ID, 0);
    }

    public String getNombre() {
        return preferences.getString(KEY_NOMBRE, "");
    }

    public String getApellido() {
        return preferences.getString(KEY_APELLIDO, "");
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, "");
    }

    public String getRol() {
        return preferences.getString(KEY_ROL, "");
    }

    public boolean estaLogueado() {
        return getToken() != null;
    }

    public void cerrarSesion() {
        editor.clear();
        editor.apply();
    }

    public String getMatricula() {return preferences.getString(KEY_MATRICULA, "");}
}
