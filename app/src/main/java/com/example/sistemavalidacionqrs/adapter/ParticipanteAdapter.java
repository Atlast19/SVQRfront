package com.example.sistemavalidacionqrs.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemavalidacionqrs.R;
import com.example.sistemavalidacionqrs.model.Accesos.AccesoResponse;

import java.util.List;

public class ParticipanteAdapter extends RecyclerView.Adapter<ParticipanteAdapter.ParticipanteViewHolder> {


    private final List<AccesoResponse> listaParticipantes;



    public ParticipanteAdapter(
            List<AccesoResponse> listaParticipantes) {

        this.listaParticipantes = listaParticipantes;
    }




    @NonNull
    @Override
    public ParticipanteViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {


        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_participante,
                                parent,
                                false
                        );


        return new ParticipanteViewHolder(view);
    }





    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(
            @NonNull ParticipanteViewHolder holder,
            int position) {



        AccesoResponse participante =
                listaParticipantes.get(position);




        holder.txtNombreParticipante.setText(
                participante.getNombreCompleto()
        );



        holder.txtMatriculaParticipante.setText(

                "Matrícula: "
                        +
                        participante.getMatricula()

        );



        holder.txtFechaAcceso.setText(

                "Fecha acceso: "
                        +
                        participante.getFechaAcceso()

        );



        holder.txtEstadoAcceso.setText(

                "Estado: "
                        +
                        participante.getEstado()

        );



    }





    @Override
    public int getItemCount() {

        return listaParticipantes.size();

    }






    static class ParticipanteViewHolder
            extends RecyclerView.ViewHolder {


        TextView txtNombreParticipante;
        TextView txtMatriculaParticipante;
        TextView txtFechaAcceso;
        TextView txtEstadoAcceso;



        public ParticipanteViewHolder(
                @NonNull View itemView) {

            super(itemView);



            txtNombreParticipante =
                    itemView.findViewById(
                            R.id.txtNombreParticipante
                    );



            txtMatriculaParticipante =
                    itemView.findViewById(
                            R.id.txtMatriculaParticipante
                    );



            txtFechaAcceso =
                    itemView.findViewById(
                            R.id.txtFechaAcceso
                    );



            txtEstadoAcceso =
                    itemView.findViewById(
                            R.id.txtEstadoAcceso
                    );

        }
    }
}
