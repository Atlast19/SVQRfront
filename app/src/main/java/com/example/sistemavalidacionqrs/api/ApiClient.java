package com.example.sistemavalidacionqrs.api;

import android.content.Context;
import android.os.Build;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getClient(Context context) {

        if (retrofit == null) {

            String baseUrl;

            // Detectar si se está ejecutando en el emulador
            if (Build.FINGERPRINT.contains("generic")
                    || Build.FINGERPRINT.contains("emulator")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK")) {

                // Emulador Android Studio
                baseUrl = "http://10.0.2.2:8080/api/";

            } else {

                // 📌 REEMPLAZA esta IP por la IP local de tu computadora
                baseUrl = "http://192.168.16.255:8080/api/";
            }

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}