package com.atendimento.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atendimento.R;
import com.atendimento.model.Endereco;

import java.util.List;

public class AdapterEnderecos extends RecyclerView.Adapter<AdapterEnderecos.EnderecoViewHolder>  {

    private List<Endereco> listaEnderecos;
    private Context contexto;

    public AdapterEnderecos(Context contexto, List<Endereco> lista) {
        this.contexto       = contexto;
        this.listaEnderecos = lista;
    }


    public class EnderecoViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewTextoPesquisado;
        public TextView textViewEndereco;
        public TextView textViewCidade;

        public EnderecoViewHolder(View itemView) {
            super(itemView);
            textViewTextoPesquisado = itemView.findViewById(R.id.textViewListagemEnderecoPesquisa);
            textViewEndereco        = itemView.findViewById(R.id.textViewListagemEnderecosEncontrado);
            textViewCidade          = itemView.findViewById(R.id.textViewListagemEnderecoCidade);
        }
    }

    @NonNull
    @Override
    public EnderecoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_endereco,parent, false);
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
        return new EnderecoViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull EnderecoViewHolder holder, int position) {
        Endereco endereco = listaEnderecos.get(position);
        holder.textViewTextoPesquisado.setText(endereco.getTextoPesquisado());
        holder.textViewEndereco.setText(endereco.getEndereco());
        holder.textViewCidade.setText(endereco.getCidade());
    }

    @Override
    public int getItemCount() {
        return listaEnderecos.size();
    }


}
