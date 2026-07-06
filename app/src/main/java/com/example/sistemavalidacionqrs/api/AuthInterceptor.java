package com.example.sistemavalidacionqrs.api;

import android.content.Context;
import android.util.Log;

import com.example.sistemavalidacionqrs.utils.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        SessionManager sessionManager = new SessionManager(context);

        String token = sessionManager.getToken();

        Request request = chain.request();

        Log.d(
                "AUTH",
                "Token enviado: " + token
        );

        if (token != null && !token.isEmpty()) {

            request = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
        }

        return chain.proceed(request);
    }
}
