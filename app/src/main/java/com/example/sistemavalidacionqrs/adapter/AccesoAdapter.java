package com.example.sistemavalidacionqrs.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.model.Accesos.AccesoResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AccesoAdapter extends RecyclerView.Adapter<AccesoAdapter.AccesoViewHolder> {

    private final List<AccesoResponse> listaAccesos;

    public AccesoAdapter(List<AccesoResponse> listaAccesos) {
        this.listaAccesos = listaAccesos;
    }

    @NonNull
    @Override
    public AccesoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_acceso, parent, false);

        return new AccesoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AccesoViewHolder holder, int position) {

        AccesoResponse acceso = listaAccesos.get(position);

        holder.txtEstado.setText("Estado: " + acceso.getEstado());

        if (acceso.getFechaAcceso() != null &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            LocalDateTime fecha =
                    LocalDateTime.parse(
                            acceso.getFechaAcceso()
                    );

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy hh:mm a"
                    );

            holder.txtFecha.setText(
                    "Fecha: " + fecha.format(formatter)
            );

        } else {

            holder.txtFecha.setText("Fecha: N/D");
        }

        holder.txtIp.setText("IP: " + acceso.getIpAddress());
        holder.txtDispositivo.setText("Dispositivo: " + acceso.getDispositivo());
    }

    @Override
    public int getItemCount() {
        return listaAccesos.size();
    }

    public static class AccesoViewHolder extends RecyclerView.ViewHolder {

        TextView txtEstado;
        TextView txtFecha;
        TextView txtIp;
        TextView txtDispositivo;

        public AccesoViewHolder(@NonNull View itemView) {
            super(itemView);

            txtEstado = itemView.findViewById(R.id.txtEstado);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtIp = itemView.findViewById(R.id.txtIp);
            txtDispositivo = itemView.findViewById(R.id.txtDispositivo);
        }
    }
}