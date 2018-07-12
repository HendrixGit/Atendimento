package com.atendimento.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.atendimento.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class AdapterGenerico extends RecyclerView.Adapter<AdapterGenerico.MyViewHoder> {

    protected Context contexto;

    public AdapterGenerico(Context context){
        this.contexto  = context;
    }

    public class MyViewHoder extends RecyclerView.ViewHolder {

        public TextView textViewTitulo;
        public TextView textViewSubTitulo;
        public CircleImageView circleImageViewImagemListagem;
        public CircleImageView circleImageViewSelecaoListagem;

        public MyViewHoder(View itemView) {
            super(itemView);
            textViewTitulo                 = itemView.findViewById(R.id.textViewTextoTitulo);
            textViewSubTitulo              = itemView.findViewById(R.id.textViewSubTexto);
            circleImageViewImagemListagem  = itemView.findViewById(R.id.circleImageListagem);
            circleImageViewSelecaoListagem = itemView.findViewById(R.id.circleImageSelecaoListagem);
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
    public abstract void onBindViewHolder(MyViewHoder holder, int position);

    @Override
    public abstract int getItemCount();

    public abstract List getList();

    public void getImages(String imageUrl, CircleImageView circleImageView) {
        if (imageUrl.equals("")){
            Picasso.with(contexto).load(R.drawable.atendimento).error(R.drawable.atendimento).into(circleImageView);
        }
        else {
            Picasso.with(contexto).
                    load(imageUrl).
                    resize(80,80).
                    error(R.drawable.atendimento).
                    into(circleImageView);
        }
    }
}
