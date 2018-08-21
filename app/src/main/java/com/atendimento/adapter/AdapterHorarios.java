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
        TextView descricaoDia;

        public HorariosViewHoder(View itemView) {
            super(itemView);
            inicio       = itemView.findViewById(R.id.textViewInicioDia);
            fim          = itemView.findViewById(R.id.textViewFimDia);
            descricaoDia = itemView.findViewById(R.id.textViewDescricaoDia);
        }
    }

    @Override
    public HorariosViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.horarios_layout, parent, false);
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
        holder.descricaoDia.setText(horario.getDescricaoDia());
        holder.inicio.setText(horario.getHoraInicio());
        holder.fim.setText(horario.getHoraFinal());
    }

    @Override
    public int getItemCount() {
        return listahorarios.size();
    }
}
