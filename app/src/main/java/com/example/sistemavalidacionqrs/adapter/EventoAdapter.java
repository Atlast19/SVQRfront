package com.example.sistemavalidacionqrs.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemavalidacionqrs.Detalle.EventoDetalleActivity;
import com.example.sistemavalidacionqrs.Detalle.EventoDetalleAdminActivity;
import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.model.Eventos.EventoResponse;
import com.example.sistemavalidacionqrs.utils.SessionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private final List<EventoResponse> listaEventos;

    public EventoAdapter(List<EventoResponse> listaEventos) {
        this.listaEventos = listaEventos;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_evento,
                        parent,
                        false
                );

        return new EventoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(
            @NonNull EventoViewHolder holder,
            int position) {

        EventoResponse evento = listaEventos.get(position);

        holder.txtNombreEvento.setText(evento.getNombre());
        holder.txtCodigoEvento.setText("Código: " + evento.getCodigo());
        holder.txtEstadoEvento.setText("Estado: " + evento.getEstado());
        holder.txtFechaEvento.setText("Inicio: " + formatearFecha(evento.getFechaInicio()));


        holder.itemView.setOnClickListener(v -> {

            SessionManager sessionManager =
                    new SessionManager(
                            holder.itemView.getContext()
                    );

            Intent intent;

            if ("ADMINISTRADOR".equalsIgnoreCase(
                    sessionManager.getRol())) {

                intent = new Intent(
                        holder.itemView.getContext(),
                        EventoDetalleAdminActivity.class
                );

            } else {

                intent = new Intent(
                        holder.itemView.getContext(),
                        EventoDetalleActivity.class
                );
            }

            intent.putExtra(
                    "eventoId",
                    evento.getId()
            );

            holder.itemView
                    .getContext()
                    .startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public static class EventoViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtNombreEvento;
        TextView txtCodigoEvento;
        TextView txtEstadoEvento;
        TextView txtFechaEvento;

        public EventoViewHolder(
                @NonNull View itemView) {

            super(itemView);

            txtNombreEvento =
                    itemView.findViewById(
                            R.id.txtNombreEvento
                    );

            txtCodigoEvento =
                    itemView.findViewById(
                            R.id.txtCodigoEvento
                    );

            txtEstadoEvento =
                    itemView.findViewById(
                            R.id.txtEstadoEvento
                    );

            txtFechaEvento =
                    itemView.findViewById(
                            R.id.txtFechaEvento
                    );
        }
    }

    public void actualizarLista(List<EventoResponse> nuevosEventos) {

        this.listaEventos.clear();

        this.listaEventos.addAll(nuevosEventos);

        notifyDataSetChanged();

    }

    private String formatearFecha(String fecha) {

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                LocalDateTime dateTime = LocalDateTime.parse(fecha);

                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy hh:mm a",
                                Locale.US
                        );

                return dateTime.format(formatter);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return fecha;
    }
}