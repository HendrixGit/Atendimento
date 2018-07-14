package com.atendimento.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.atendimento.activity.CadastrarEmpresaActivity;
import com.atendimento.activity.EmpresasActivity;
import com.atendimento.adapter.AdapterEmpresasApp;
import com.atendimento.bases.BaseFragment;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Empresa;
import com.atendimento.util.RecyclerItemClickListener;
import com.atendimento.util.SimpleDividerItemDecoration;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


public class EmpresasFragment extends BaseFragment {

    private AdapterEmpresasApp adapterEmpresa;
    private List<Empresa> empresas             = new ArrayList<>();
    private List<Empresa> empresasSelecionadas = new ArrayList<>();
    private EmpresasActivity empresasActivity;

    public EmpresasFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        empresasActivity = (EmpresasActivity) getActivity();
        adapterEmpresa = new AdapterEmpresasApp(empresas, getContext());
        RecyclerView.LayoutManager layoutManager   = new LinearLayoutManager(getActivity());
        recyclerViewBase.setLayoutManager(layoutManager);
        recyclerViewBase.setHasFixedSize(true);
        recyclerViewBase.setAdapter(adapterEmpresa);
        recyclerViewBase.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewBase,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                atualizarLista();
                                if (!modoSelecaoBase) {
                                    mudarTelaObject(getActivity(), CadastrarEmpresaActivity.class, empresas.get(position), "empresa");
                                }
                                else{
                                    selecionarEmpresas(position);
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                if (!empresasActivity.pesquisando) {
                                    selecionarEmpresas(position);
                                }
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                ));

        recyclerViewBase.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        floatingActionButtonBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTela(getActivity(), CadastrarEmpresaActivity.class);
            }
        });


        String idUsuarioLogado = ConfiguracaoFirebase.getAutenticacao().getCurrentUser().getUid();
        firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        queryBase = firebaseDatabase.child("empresas").child(idUsuarioLogado).orderByChild("nome");
        return viewBase;
    }

    private void atualizarLista() {
        List<Empresa> listaAtualizada = adapterEmpresa.getList();
        empresas = listaAtualizada;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (empresas.size() <= 0) {
            recuperarEmpresas();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        removerListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        removerListener();
    }


    private void removerListener() {
        queryBase.removeEventListener(childEventListenerBase);
    }

    public void recarregarEmpresas(){
        carregarEmpresas(empresas);
    }

    public void carregarEmpresas(List<Empresa> listaParametro){
        adapterEmpresa = new AdapterEmpresasApp(listaParametro, getActivity());
        recyclerViewBase.setAdapter(adapterEmpresa);
        adapterEmpresa.notifyDataSetChanged();
    }

    public void recuperarEmpresas(){
        empresas.clear();
        childEventListenerBase = queryBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Empresa empresa = dataSnapshot.getValue(Empresa.class);
                empresas.add(empresa);
                adapterEmpresa.notifyDataSetChanged();
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

    public void selecionarEmpresas(Integer posicao){
        modoSelecaoBase = true;
        if (!empresasSelecionadas.contains(empresas.get(posicao)) && empresasSelecionadas.size() != empresas.size()) {
            empresas.get(posicao).setSelecionado(true);
            selecionadosBase++;
            empresasSelecionadas.add(empresas.get(posicao));
        }
        else{
            empresas.get(posicao).setSelecionado(false);
            selecionadosBase--;
            empresasSelecionadas.remove(empresas.get(posicao));
        }
        adapterEmpresa.notifyDataSetChanged();
        empresasActivity.setTituloToolbar(selecionadosBase.toString());
        if (selecionadosBase == 0){ empresasActivity.toolbarPadrao(); }
    }

    public void zerarSelecao(){
        super.zerarSelecao();
        empresasSelecionadas.clear();
        int i = 0;
        for(Empresa empresa : empresas) {   empresas.get(i).setSelecionado(false);  i++; }
        adapterEmpresa.notifyDataSetChanged();
    }

    public void deletarEmpresa(){
        for (Empresa empresa : empresasSelecionadas){
            DatabaseReference firebaseDatabaseDeletar = ConfiguracaoFirebase.getFirebaseDatabase();
            firebaseDatabaseDeletar.child("empresas").child(empresa.getIdUsuario()).child(empresa.getId()).removeValue();
            firebaseDatabaseDeletar.child("empresasApp").child(empresa.getId()).removeValue();
            storageReferenceBase = ConfiguracaoFirebase.getStorage().child("empresas").child(empresa.getIdUsuario()).child(empresa.getId());
            storageReferenceBase.delete();
        }
        recuperarEmpresas();
    }

    public void pesquisarEmpresa(String textoPesquisa){
        List<Empresa> listaEmpresasBusca = new ArrayList<>();
        for(Empresa empresa : empresas){
            String nome      = Normalizer.normalize(empresa.getNome(),      Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            String categoria = Normalizer.normalize(empresa.getCategoria(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            if(nome.toLowerCase().contains(textoPesquisa) || categoria.toLowerCase().contains(textoPesquisa) ){
                listaEmpresasBusca.add(empresa);
            }
        }
        carregarEmpresas(listaEmpresasBusca);
    }
}
