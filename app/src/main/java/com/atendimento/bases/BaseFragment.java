package com.atendimento.bases;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atendimento.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;


public class BaseFragment extends Fragment {

    protected View viewBase;
    protected RecyclerView recyclerViewBase;
    protected FloatingActionButton floatingActionButtonBase;
    protected Boolean modoSelecaoBase = false;
    protected Integer selecionadosBase = 0;
    protected Query queryBase;
    protected DatabaseReference firebaseDatabase;
    protected ChildEventListener childEventListenerBase;
    protected StorageReference storageReferenceBase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBase         = inflater.inflate(R.layout.fragment_base, container, false);
        recyclerViewBase = viewBase.findViewById(R.id.recyclerViewBase);
        floatingActionButtonBase =  viewBase.findViewById(R.id.floatButtonBase);
        return viewBase;
    }

    protected void mudarTela(Context contexto, Class classe) {
        Intent intent = new Intent(contexto, classe);
        startActivity(intent);
    }

    protected void mudarTelaObject(Context context, Class classe, Serializable serializable, String nomeObjeto){
        Intent intent = new Intent(context, classe);
        intent.putExtra(nomeObjeto, serializable);
        startActivity(intent);
    }

    protected void selecionarEmpresas(Integer posicao){};

    protected void zerarSelecao(){
        selecionadosBase = 0;
        modoSelecaoBase  = false;
    }



}
