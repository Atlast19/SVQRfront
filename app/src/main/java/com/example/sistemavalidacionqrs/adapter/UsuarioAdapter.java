package com.example.sistemavalidacionqrs.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sistemavalidacionqrs.R;

import com.example.sistemavalidacionqrs.activities.EditarPerfilActivity;
import com.example.sistemavalidacionqrs.activities.HistorialActivity;
import com.example.sistemavalidacionqrs.api.ApiClient;
import com.example.sistemavalidacionqrs.api.ApiService;
import com.example.sistemavalidacionqrs.model.Usuarios.UsuarioResponse;


import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private final List<UsuarioResponse> listaUsuarios;
    private final List<UsuarioResponse> listaOriginal;

    public UsuarioAdapter(List<UsuarioResponse> listaUsuarios) {

        this.listaUsuarios = listaUsuarios;

        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaUsuarios);
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_usuario,
                        parent,
                        false
                );

        return new UsuarioViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(
            @NonNull UsuarioViewHolder holder,
            int position) {

        UsuarioResponse usuario = listaUsuarios.get(position);

        holder.txtNombre.setText(
                usuario.getNombre() + " " + usuario.getApellido()
        );

        holder.txtMatricula.setText(
                "Matrícula: " + usuario.getMatricula()
        );

        holder.txtEmail.setText(
                "Correo: " + usuario.getEmail()
        );


        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    holder.itemView.getContext(),
                    HistorialActivity.class
            );

            intent.putExtra(
                    "usuarioId",
                    usuario.getId()
            );

            holder.itemView.getContext()
                    .startActivity(intent);

        });


        holder.btnEditar.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            holder.itemView.getContext(),
                            EditarPerfilActivity.class
                    );

            intent.putExtra(
                    "usuarioId",
                    usuario.getId()
            );

            holder.itemView.getContext()
                    .startActivity(intent);

        });


        holder.btnEstado.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(
                            holder.itemView.getContext()
                    );

            builder.setTitle(
                    "Desactivar usuario"
            );

            builder.setMessage(
                    "¿Desea desactivar este usuario?"
            );

            builder.setPositiveButton(
                    "Aceptar",
                    (dialog, which) -> eliminarUsuario(
                            usuario.getId(),
                            holder
                    )
            );

            builder.setNegativeButton(
                    "Cancelar",
                    null
            );

            builder.show();

        });

    }

    @Override
    public int getItemCount() {

        return listaUsuarios.size();

    }


    public void actualizarLista(
            List<UsuarioResponse> usuarios) {

        listaUsuarios.clear();
        listaUsuarios.addAll(usuarios);

        listaOriginal.clear();
        listaOriginal.addAll(usuarios);

        notifyDataSetChanged();
    }


    public void filtrar(String texto) {

        listaUsuarios.clear();

        if (texto == null || texto.trim().isEmpty()) {

            listaUsuarios.addAll(listaOriginal);

        } else {

            texto = texto.toLowerCase().trim();

            for (UsuarioResponse usuario : listaOriginal) {

                String nombreCompleto =
                        (usuario.getNombre() + " " + usuario.getApellido())
                                .toLowerCase();

                String matricula =
                        usuario.getMatricula().toLowerCase();

                if (nombreCompleto.contains(texto)
                        || matricula.contains(texto)) {

                    listaUsuarios.add(usuario);

                }

            }

        }

        notifyDataSetChanged();

    }

    private void eliminarUsuario(
            Integer id,
            UsuarioViewHolder holder) {

        ApiService apiService =
                ApiClient.getClient(
                                holder.itemView.getContext()
                        )
                        .create(ApiService.class);

        apiService.eliminarUsuario(id)
                .enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response) {

                        if (response.isSuccessful()) {

                            int posicion =
                                    holder.getAdapterPosition();

                            if (posicion != RecyclerView.NO_POSITION) {

                                listaUsuarios.remove(posicion);

                                listaOriginal.clear();
                                listaOriginal.addAll(listaUsuarios);

                                notifyItemRemoved(posicion);

                            }

                            Toast.makeText(
                                    holder.itemView.getContext(),
                                    "Usuario eliminado",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            Toast.makeText(
                                    holder.itemView.getContext(),
                                    "No se pudo eliminar",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t) {

                        Toast.makeText(
                                holder.itemView.getContext(),
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                });

    }

    static class UsuarioViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtNombre;
        TextView txtMatricula;
        TextView txtEmail;

        ImageButton btnEditar;
        ImageButton btnEstado;

        public UsuarioViewHolder(
                @NonNull View itemView) {

            super(itemView);

            txtNombre =
                    itemView.findViewById(
                            R.id.txtNombre
                    );

            txtMatricula =
                    itemView.findViewById(
                            R.id.txtMatricula
                    );

            txtEmail =
                    itemView.findViewById(
                            R.id.txtEmail
                    );

            btnEditar =
                    itemView.findViewById(
                            R.id.btnEditar
                    );

            btnEstado =
                    itemView.findViewById(
                            R.id.btnEstado
                    );

        }

    }
}