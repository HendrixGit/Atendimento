package com.atendimento.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.atendimento.R;
import com.atendimento.model.Usuario;

import java.util.ArrayList;

public class PerfilAdapter extends ArrayAdapter {

    private ArrayList<Usuario> usuarios;
    private Context context;

    public PerfilAdapter(Context context, ArrayList<Usuario> objects) {
        super(context, 0, objects);
        this.usuarios = objects;
        this.context  = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (usuarios != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_perfil, parent, false);

            Usuario usuario = usuarios.get(position);

            EditText nome  = view.findViewById(R.id.editNomeConf);
            TextView email = view.findViewById(R.id.editEmailPerfil);

            nome.setText(usuario.getNome());
            email.setText(usuario.getEmail());

        }
        return view;
    }
}