package com.atendimento.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.adapter.PerfilAdapter;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Usuario;
import com.atendimento.util.Preferencias;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConfiguracoesActivity extends BaseActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private String identificadorUsuario;
    private DatabaseReference firebase;
    private ListView listView;
    private ArrayList<Usuario> usuarios;
    private ArrayAdapter<Usuario> adapter;
    private ValueEventListener valueEventListenerPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listView = findViewById(R.id.listViewConfiguracoes);

        Preferencias preferencias = new Preferencias(getApplicationContext());
        identificadorUsuario = preferencias.getIdentificador();

        usuarios = new ArrayList<>();
        adapter  = new PerfilAdapter(ConfiguracoesActivity.this,usuarios);
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child(identificadorUsuario);
        listView.setAdapter(adapter);

        valueEventListenerPerfil = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarios.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Usuario usuario = new Usuario();//dados.getValue(Usuario.class);
                    usuario.setNome(dataSnapshot.child("usuarios").child(identificadorUsuario).child("nome").getValue().toString());
                    usuario.setEmail(dataSnapshot.child("usuarios").child(identificadorUsuario).child("email").getValue().toString());
                    usuarios.add(usuario);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebase.addValueEventListener(valueEventListenerPerfil);
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeListener();
    }

    private void removeListener() {
        firebase.removeEventListener(valueEventListenerPerfil);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: salvarDados();return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void salvarDados(){
        EditText nome = findViewById(R.id.editNomePerfil);
        if (!nome.getText().equals("")) {
            firebase.child("usuarios")
                    .child(identificadorUsuario)
                    .child("nome").setValue(nome.getText().toString());
            mudarTelaFinish(getApplicationContext(), MainActivity.class);
        }
        else{
            Toast.makeText(getApplicationContext(),"Favor Preencha o nome",Toast.LENGTH_LONG).show();
        }
    }

}
