package com.atendimento.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.atendimento.R;
import com.atendimento.model.Empresa;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmpresasAdapter extends ArrayAdapter {

    private ArrayList<Empresa> empresas;
    private Context context;
    private TextView textViewNome;
    private TextView textViewCategoria;
    private CircleImageView circleImageViewEmpresa;
    private View view = null;

    public EmpresasAdapter(Context context, ArrayList<Empresa> objects){
        super(context, 0, objects);
        this.context  = context;
        this.empresas = objects;
    }

    public void getImages(String imageUrl, CircleImageView circleImageView) {
        if (imageUrl.equals("")){
            Picasso.with(getContext()).load(R.drawable.atendimento).error(R.drawable.atendimento).into(circleImageView);
        }
        else {
            Picasso.with(getContext()).load(imageUrl).error(R.drawable.atendimento).into(circleImageView);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.lista_empresas, parent, false);
        textViewNome = view.findViewById(R.id.textViewNomeEmpresa);
        textViewCategoria = view.findViewById(R.id.textViewCategoria);
        circleImageViewEmpresa = view.findViewById(R.id.circleImageEmpresasAdapter);

        if (empresas != null) {
            Empresa empresa = empresas.get(position);
            textViewNome.setText(empresa.getNome());
            textViewCategoria.setText(empresa.getCategoria());
            getImages(empresa.getUrlImagem(), circleImageViewEmpresa);

        }
        return view;
    }
}
