package com.atendimento.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.atendimento.R;
import com.atendimento.activity.CadastrarEmpresaActivity;
import com.atendimento.activity.EmpresasActivity;
import com.atendimento.adapter.EmpresasAdapter;
import com.atendimento.bases.BaseFragment;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Empresa;
import com.atendimento.util.RecyclerItemClickListener;
import com.atendimento.util.SimpleDividerItemDecoration;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class EmpresasFragment extends BaseFragment {

    private RecyclerView recyclerViewEmpresas;
    private EmpresasAdapter adapterEmpresa;
    private List<Empresa> empresas = new ArrayList<>();
    private List<Empresa> empresasSelecionadas = new ArrayList<>();
    private Integer selecionados = 0;
    private DatabaseReference firebaseDatabase;
    private ChildEventListener childEventListenerEmpresas;
    private StorageReference storageReferenceEmpresas;
    private FloatingActionButton cadastrarEmpresaButton;
    private Query query;
    private Boolean modoSelecao = false;
    private EmpresasActivity empresasActivity;

    public EmpresasFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empresas, container, false);
        empresasActivity = (EmpresasActivity) getActivity();
        recyclerViewEmpresas = view.findViewById(R.id.recyclerViewEmpresas);
        adapterEmpresa = new EmpresasAdapter(empresas, getContext());
        RecyclerView.LayoutManager layoutManager   = new LinearLayoutManager(getActivity());
        recyclerViewEmpresas.setLayoutManager(layoutManager);
        recyclerViewEmpresas.setHasFixedSize(true);
        recyclerViewEmpresas.setAdapter(adapterEmpresa);
        recyclerViewEmpresas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewEmpresas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (!modoSelecao) {
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

        recyclerViewEmpresas.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        cadastrarEmpresaButton =  view.findViewById(R.id.floatButtonCadastrarEmpresa);
        cadastrarEmpresaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTela(getActivity(), CadastrarEmpresaActivity.class);
            }
        });


        String idUsuarioLogado = ConfiguracaoFirebase.getAutenticacao().getCurrentUser().getUid();
        firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        query = firebaseDatabase.child("empresas").child(idUsuarioLogado).orderByChild("nome");
        return view;
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
        query.removeEventListener(childEventListenerEmpresas);
    }

    public void recarregarEmpresas(){
        carregarEmpresas(empresas);
    }

    public void carregarEmpresas(List<Empresa> listaParametro){
        adapterEmpresa = new EmpresasAdapter(listaParametro, getActivity());
        recyclerViewEmpresas.setAdapter(adapterEmpresa);
        adapterEmpresa.notifyDataSetChanged();
    }

    public void recuperarEmpresas(){
        empresas.clear();
        childEventListenerEmpresas = query.addChildEventListener(new ChildEventListener() {
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
        modoSelecao = true;
        if (!empresasSelecionadas.contains(empresas.get(posicao)) && empresasSelecionadas.size() != empresas.size()) {
            empresas.get(posicao).setSelecionado(true);
            selecionados++;
            empresasSelecionadas.add(empresas.get(posicao));
        }
        else{
            empresas.get(posicao).setSelecionado(false);
            selecionados--;
            empresasSelecionadas.remove(empresas.get(posicao));
        }
        adapterEmpresa.notifyDataSetChanged();
        empresasActivity.setTituloToolbar(selecionados.toString());
        if (selecionados == 0){ empresasActivity.toolbarPadrao(); }
    }

    public void zerarSelecao(){
        selecionados = 0;
        modoSelecao = false;
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
            storageReferenceEmpresas = ConfiguracaoFirebase.getStorage().child("empresas").child(empresa.getIdUsuario()).child(empresa.getId());
            storageReferenceEmpresas.delete();
        }
        recuperarEmpresas();
    }

    public void pesquisarEmpresa(String textoPesquisa){
        List<Empresa> listaEmpresasBusca = new ArrayList<>();
        for(Empresa empresa : empresas){
            if(empresa.getNome().toLowerCase().contains(textoPesquisa)){
                listaEmpresasBusca.add(empresa);
            }
        }
        carregarEmpresas(listaEmpresasBusca);
    }
}
