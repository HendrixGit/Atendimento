package com.atendimento.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.atendimento.R;
import com.atendimento.activity.CadastrarEmpresaActivity;
import com.atendimento.adapter.EmpresasAdapter;
import com.atendimento.bases.BaseFragment;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Empresa;
import com.atendimento.util.Preferencias;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmpresasFragment extends BaseFragment {

    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<Empresa> empresas = new ArrayList<>();
    private DatabaseReference firebaseDatabase;
    private ValueEventListener valueEventListenerEmpresas;
    private ChildEventListener childEventListenerEmpresas;
    private FloatingActionButton cadastrarEmpresaButton;
    private Query query;

    public EmpresasFragment(){}

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

    public void pesquisarEmpresa(String textoPesquisa){

    }

    private void recuperarEmpresas(){
        childEventListenerEmpresas = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Empresa empresa = dataSnapshot.getValue(Empresa.class);
                empresas.add(empresa);
                arrayAdapter.notifyDataSetChanged();
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
        };
        query.addChildEventListener(childEventListenerEmpresas);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empresas, container, false);
        cadastrarEmpresaButton =  view.findViewById(R.id.floatButtonCadastrarEmpresa);
        cadastrarEmpresaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTela(getActivity(), CadastrarEmpresaActivity.class);
            }
        });
        listView = view.findViewById(R.id.listViewEmpresas);
        arrayAdapter = new EmpresasAdapter(getActivity(), empresas);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mudarTelaObject(getActivity(), CadastrarEmpresaActivity.class, empresas.get(i), "empresa");
            }
        });

        Preferencias preferencias = new Preferencias(getActivity());
        String idUsuarioLogado = preferencias.getIdentificador();
        firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        query = firebaseDatabase.child("empresas").child(idUsuarioLogado).orderByChild("nome");;
        return view;
    }

}
