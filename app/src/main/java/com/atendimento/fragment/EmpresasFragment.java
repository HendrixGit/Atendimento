package com.atendimento.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.atendimento.R;
import com.atendimento.activity.CadastrarEmpresaActivity;
import com.atendimento.adapter.EmpresasAdapter;
import com.atendimento.bases.BaseFragment;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Empresa;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import java.util.ArrayList;
import java.util.List;

public class EmpresasFragment extends BaseFragment {

    private RecyclerView recyclerViewEmpresas;
    private EmpresasAdapter adapterEmpresa;
    private List<Empresa> empresas = new ArrayList<>();
    private DatabaseReference firebaseDatabase;
    private ChildEventListener childEventListenerEmpresas;
    private FloatingActionButton cadastrarEmpresaButton;
    private Query query;

    public EmpresasFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empresas, container, false);
        recyclerViewEmpresas = view.findViewById(R.id.recyclerViewEmpresas);
        adapterEmpresa = new EmpresasAdapter(empresas,getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewEmpresas.setLayoutManager(layoutManager);
        recyclerViewEmpresas.setHasFixedSize(true);
        recyclerViewEmpresas.setAdapter(adapterEmpresa);

        cadastrarEmpresaButton =  view.findViewById(R.id.floatButtonCadastrarEmpresa);
        cadastrarEmpresaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTela(getActivity(), CadastrarEmpresaActivity.class);
            }
        });


        String idUsuarioLogado = ConfiguracaoFirebase.getAutenticacao().getCurrentUser().getUid();
        firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        query = firebaseDatabase.child("empresas").child(idUsuarioLogado).orderByChild("nome");;

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarEmpresas();
    }

    @Override
    public void onStop() {
        super.onStop();
        query.removeEventListener(childEventListenerEmpresas);
    }

    public void carregarEmpresas(List<Empresa> listaParametro){
        adapterEmpresa = new EmpresasAdapter(listaParametro, getActivity());
        recyclerViewEmpresas.setAdapter(adapterEmpresa);
        adapterEmpresa.notifyDataSetChanged();
    }

    public void recarregarEmpresas(){
        carregarEmpresas(empresas);
    }

    public void recuperarEmpresas(){
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

    public void pesquisarEmpresa(String textoPesquisa){
        List<Empresa> listaEmpresasBusca = new ArrayList<>();
        for(Empresa empresa : empresas){
            if(empresa.getNome().contains(textoPesquisa)){
                listaEmpresasBusca.add(empresa);
            }
        }
        carregarEmpresas(listaEmpresasBusca);
    }

}
