package com.atendimento.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.atendimento.R;
import com.atendimento.model.Horario;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AdapterHorarios extends RecyclerView.Adapter<AdapterHorarios.HorariosViewHoder> {

    private List<Horario> listahorarios;
    private Context contexto;

    public AdapterHorarios(List<Horario> lista, Context context) {
        this.listahorarios = lista;
        this.contexto = context;
    }

    public class HorariosViewHoder extends RecyclerView.ViewHolder {

        TextView inicio;
        TextView fim;
        TextView situacao;

        public HorariosViewHoder(View itemView) {
            super(itemView);
            inicio       = itemView.findViewById(R.id.textViewListagemHorariosInicio);
            fim          = itemView.findViewById(R.id.textViewListagemHorariosFim);
            situacao     = itemView.findViewById(R.id.textViewListagemHorariosSituacao);
        }
    }

    @Override
    public HorariosViewHoder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horarios, parent, false);
        itemLista.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.setPressed(true);
                        break;
                    }
                }
                return false;
            }
        });
        return new HorariosViewHoder(itemLista);
    }

    @Override
    public void onBindViewHolder(HorariosViewHoder holder, int position) {
        Horario horario = listahorarios.get(position);
        holder.inicio.setText(horario.getHoraInicio());
        holder.fim.setText(horario.getHoraFinal());

        Date dataAtual          = Calendar.getInstance().getTime();
        int  diaSemana          = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String horaAtualTexto   = new SimpleDateFormat("HH:mm").format(dataAtual);

        if (horario.getDiaSemana() == diaSemana) {

            int hora    = Integer.parseInt(holder.inicio.getText().toString().substring(0, 2));
            int minutos = Integer.parseInt(holder.inicio.getText().toString().substring(3, 5));

            int horaAtual    = Integer.parseInt(horaAtualTexto.substring(0, 2));
            int minutosAtual = Integer.parseInt(horaAtualTexto.substring(3, 5));

            if (hora >= horaAtual || minutos >= minutosAtual) {
                holder.situacao.setText(": Marcado");
            } else {
                holder.situacao.setText(": Atrasado");
            }
        }
        else{
            holder.situacao.setText(": Disponível");
        }

    }

    @Override
    public int getItemCount() {
        return listahorarios.size();
    }


    public List getList() {
        return listahorarios;
    }
}
