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

public class AdapterGenerico extends RecyclerView.Adapter<AdapterGenerico.MyViewHoder> {

    private List<Object> objects;
    private Context context;

    public AdapterGenerico(List<Object> objects, Context context){
        this.context  = context;
        this.objects  = objects;
    }

    public class MyViewHoder extends RecyclerView.ViewHolder {

        private TextView textViewTitulo;
        private TextView textViewSubTitulo;
        private CircleImageView circleImageViewImagemListagem;
        private CircleImageView circleImageViewSelecaoListagem;

        public MyViewHoder(View itemView) {
            super(itemView);
            textViewTitulo                 = itemView.findViewById(R.id.textViewNomeEmpresa);
            textViewSubTitulo              = itemView.findViewById(R.id.textViewCategoria);
            circleImageViewImagemListagem  = itemView.findViewById(R.id.circleImageEmpresasAdapter);
            circleImageViewSelecaoListagem = itemView.findViewById(R.id.circleImageSelecao);
        }
    }

    @Override
    public MyViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listagem,parent,false);
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
        if (objects.contains(Empresa.class)) {
            Empresa empresa = (Empresa) objects.get(position);
            holder.textViewTitulo.setText(empresa.getNome());
            holder.textViewSubTitulo.setText(empresa.getCategoria());
            getImages(empresa.getUrlImagem(), holder.circleImageViewImagemListagem);
            if (empresa.getSelecionado()) {
                Picasso.with(context).load(R.drawable.checkmarkblue).resize(64, 64).into(holder.circleImageViewSelecaoListagem);
            }
            else {
                holder.circleImageViewSelecaoListagem.setImageDrawable(null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
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
