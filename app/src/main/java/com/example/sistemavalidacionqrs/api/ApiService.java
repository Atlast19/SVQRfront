package com.example.sistemavalidacionqrs.api;

import com.example.sistemavalidacionqrs.model.Accesos.AccesoResponse;
import com.example.sistemavalidacionqrs.model.Eventos.EventoRequest;
import com.example.sistemavalidacionqrs.model.Eventos.EventoResponse;
import com.example.sistemavalidacionqrs.model.Login.AuthRequest;
import com.example.sistemavalidacionqrs.model.Login.AuthResponse;
import com.example.sistemavalidacionqrs.model.QrTokens.GenerarQrResponse;
import com.example.sistemavalidacionqrs.model.Usuarios.UsuarioRequest;
import com.example.sistemavalidacionqrs.model.Usuarios.UsuarioResponse;
import com.example.sistemavalidacionqrs.model.Usuarios.UsuarioUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("qr/generar/{usuarioId}/{eventoId}")
    Call<GenerarQrResponse> generarQr(@Path("usuarioId") Integer usuarioId, @Path("eventoId") Integer eventoId);

    @GET("accesos/usuarios/{usuarioId}")
    Call<List<AccesoResponse>> obtenerHistorial(@Path("usuarioId") Integer usuarioId);

    @POST("accesos/registrar")
    Call<AccesoResponse> registrarAcceso(@Query("token") String token);

    @POST("usuarios/CreateUsuarios")
    Call<UsuarioResponse> crearUsuario(@Body UsuarioRequest request);

    @PUT("usuarios/UpdateUsuarioById/{id}") Call<UsuarioResponse> actualizarUsuario(@Path("id") Integer id, @Body UsuarioUpdateRequest request);

    @GET("usuarios/GetAllUsuarios") Call<List<UsuarioResponse>> obtenerTodosLosUsuarios();

    @GET("usuarios/GetUsuarioById/{id}")
    Call<UsuarioResponse> obtenerUsuarioPorId(@Path("id") Integer id);

    @DELETE("usuarios/DeleteUsuario/{id}") Call<Void> eliminarUsuario(@Path("id") Integer id);

    @GET("eventos/GetAllEventos") Call<List<EventoResponse>> obtenerEventos();

    @GET("eventos/GetEventoById/{id}") Call<EventoResponse> obtenerEventoPorId(@Path("id") Integer id);

    @GET("eventos/GetParticipantesEvento/{eventoId}") Call<List<AccesoResponse>> obtenerParticipantesEvento(@Path("eventoId") Integer eventoId);

    @POST("eventos/CreateEvento") Call<EventoResponse> crearEvento(@Body EventoRequest request);

    @PUT("eventos/UpdateEvento/{id}") Call<EventoResponse> actualizarEvento(@Path("id") Integer id, @Body EventoRequest request);


}

