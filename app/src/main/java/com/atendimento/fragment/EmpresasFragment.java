package com.atendimento.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.atendimento.R;
import com.atendimento.adapter.EmpresasAdapter;
import com.atendimento.bases.BaseFragment;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Empresa;
import com.atendimento.util.Preferencias;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EmpresasFragment extends BaseFragment {

    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<Empresa> empresas;
    private DatabaseReference firebaseDatabase;
    private ValueEventListener valueEventListenerEmpresas;
    private StorageReference storageReference;
    private ArrayList<Uri> imagensEmpresas;

    public EmpresasFragment(){}

    @Override
    public void onStart() {
        super.onStart();
        firebaseDatabase.addValueEventListener(valueEventListenerEmpresas);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseDatabase.removeEventListener(valueEventListenerEmpresas);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empresas, container, false);
        empresas = new ArrayList<>();
        imagensEmpresas = new ArrayList<>();
        listView = view.findViewById(R.id.listViewEmpresas);
        arrayAdapter = new EmpresasAdapter(getActivity(), empresas);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        final Preferencias preferencias = new Preferencias(getActivity());
        String idUsuarioLogado = preferencias.getIdentificador();
        firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase().child("empresas").child(idUsuarioLogado);

        valueEventListenerEmpresas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                empresas.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Empresa empresa = dados.getValue(Empresa.class);
                    empresas.add(empresa);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return view;
    }

    public void carregarFoto(Empresa empresa){
        long dim = 48 * 48;
        storageReference = ConfiguracaoFirebase.getStorage().child("empresas").child(empresa.getIdUsuario()).child(empresa.getId());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imagensEmpresas.add(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}