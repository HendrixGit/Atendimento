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
import java.util.ArrayList;

public class EmpresasAdapter extends ArrayAdapter {

    private ArrayList<Empresa> empresas;
    private Context context;

    public EmpresasAdapter(Context context, ArrayList<Empresa> objects){
        super(context, 0, objects);
        this.context  = context;
        this.empresas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        if (empresas != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_empresas, parent, false);
            TextView textViewNome      = view.findViewById(R.id.textViewNomeEmpresa);
            TextView textViewCategoria = view.findViewById(R.id.textViewCategoria);

            Empresa empresa = empresas.get(position);
            textViewNome.setText(empresa.getNome());
            textViewCategoria.setText(empresa.getCategoria());

        }
        return view;
    }
}
