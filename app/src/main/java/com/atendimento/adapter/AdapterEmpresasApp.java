package com.atendimento.adapter;

import android.content.Context;
import android.view.View;

import com.atendimento.R;
import com.atendimento.model.Empresa;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;


public class AdapterEmpresasApp extends AdapterGenerico {

    private List<Empresa> empresas;

    public AdapterEmpresasApp(List<Empresa> objects ,Context context) {
        super(context);
        this.empresas = objects;
    }

    @Override
    public void onBindViewHolder(final MyViewHoder holder, int position) {
        holder.progressBar.setVisibility(View.VISIBLE);
        Empresa empresa = empresas.get(position);
        holder.textViewTitulo.setText(empresa.getNome());
        holder.textViewSubTitulo.setText(empresa.getCategoria());
        Picasso.with(contexto).load(empresa.getUrlImagem()).error(R.drawable.atendimento).into(holder.circleImageViewImagemListagem, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.progressBar.setVisibility(View.GONE);
            }
        });

        if (empresa.getSelecionado()){  Picasso.with(contexto).load(R.drawable.checkmarkblue).resize(64,64).into(holder.circleImageViewSelecaoListagem);}
        else{   holder.circleImageViewSelecaoListagem.setImageDrawable(null);   }
    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }

    @Override
    public List getList() {
        return empresas;
    }
}
