package com.atendimento.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.atendimento.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class PerfilAdapter extends ArrayAdapter<Usuario> {

    private ArrayList<Usuario> usuarios;
    private Context context;

    public PerfilAdapter(@NonNull Context context, @NonNull List<Usuario> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (usuarios != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        }
        return convertView;
    }
}
