package com.atendimento.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.atendimento.R;
import com.atendimento.model.Empresa;
import com.squareup.picasso.Picasso;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmpresasAdapter extends RecyclerView.Adapter<EmpresasAdapter.MyViewHoder> {

    private List<Empresa> empresas;
    private Context context;

    public EmpresasAdapter(List<Empresa> objects, Context context){
        this.context  = context;
        this.empresas = objects;
    }

    public class MyViewHoder extends RecyclerView.ViewHolder {

        private TextView textViewNome;
        private TextView textViewCategoria;
        private CircleImageView circleImageViewEmpresa;

        public MyViewHoder(View itemView) {
            super(itemView);
            textViewNome           = itemView.findViewById(R.id.textViewNomeEmpresa);
            textViewCategoria      = itemView.findViewById(R.id.textViewCategoria);
            circleImageViewEmpresa = itemView.findViewById(R.id.circleImageEmpresasAdapter);
        }
    }

    @Override
    public MyViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_empresas,parent,false);
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
        return new MyViewHoder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHoder holder, int position) {
        Empresa empresa = empresas.get(position);
        holder.textViewNome.setText(empresa.getNome());
        holder.textViewCategoria.setText(empresa.getCategoria());
        getImages(empresa.getUrlImagem(), holder.circleImageViewEmpresa);
        if (position == getItemCount()){

        }
    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }



    public void getImages(String imageUrl, CircleImageView circleImageView) {
        if (imageUrl.equals("")){
            Picasso.with(context).load(R.drawable.atendimento).error(R.drawable.atendimento).into(circleImageView);
        }
        else {
            Picasso.with(context).
                    load(imageUrl).
                    resize(80,80).
                    error(R.drawable.atendimento).
                    into(circleImageView);
        }
    }
}
