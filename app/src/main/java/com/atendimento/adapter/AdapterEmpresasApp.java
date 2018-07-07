package com.atendimento.adapter;

import android.content.Context;
import com.atendimento.R;
import com.atendimento.model.Empresa;
import com.squareup.picasso.Picasso;
import java.util.List;

public class AdapterEmpresasApp extends AdapterGenerico {

    private List<Empresa> empresas;

    public AdapterEmpresasApp(List<Empresa> objects ,Context context) {
        super(context);
        this.empresas = objects;
    }

    @Override
    public void onBindViewHolder(MyViewHoder holder, int position) {
        Empresa empresa = empresas.get(position);
        holder.textViewTitulo.setText(empresa.getNome());
        holder.textViewSubTitulo.setText(empresa.getCategoria());
        getImages(empresa.getUrlImagem(), holder.circleImageViewImagemListagem);
        if (empresa.getSelecionado()){  Picasso.with(contexto).load(R.drawable.checkmarkblue).resize(64,64).into(holder.circleImageViewSelecaoListagem);  }
        else{   holder.circleImageViewSelecaoListagem.setImageDrawable(null);   }
    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }
}
