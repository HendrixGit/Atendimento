package com.atendimento.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.atendimento.activity.MarcarHorariosActivity;
import com.atendimento.adapter.AdapterEmpresasApp;
import com.atendimento.bases.BaseFragment;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Empresa;
import com.atendimento.util.RecyclerItemClickListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class EmpresasAppFragment extends BaseFragment {

    private AdapterEmpresasApp adapter;
    private List<Empresa> empresaList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        adapter = new AdapterEmpresasApp(empresaList, getActivity());
        recyclerViewBase.setAdapter(adapter);
        recyclerViewBase.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewBase,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                mudarTelaObject(getActivity(), MarcarHorariosActivity.class, empresaList.get(position), "empresa");
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        floatingActionButtonBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        queryBase = firebaseDatabase.child("empresasApp").orderByChild("nome");
        return viewBase;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarEmpresas();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void pesquisarEmpresa(String textoPesquisa) {

    }

    @Override
    public void selecionarEmpresas(Integer posicao) {

    }

    @Override
    public void removerListener() {

    }

    @Override
    public void atualizarLista() {

    }

    @Override
    public void recuperarEmpresas() {
        empresaList.clear();
        childEventListenerBase = queryBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Empresa empresa = dataSnapshot.getValue(Empresa.class);
                empresaList.add(empresa);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
