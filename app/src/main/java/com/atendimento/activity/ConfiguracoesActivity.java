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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConfiguracoesActivity extends BaseActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private String identificadorUsuario;
    private DatabaseReference firebase;
    private FirebaseAuth autenticacao;
    private ListView listView;
    private ArrayList<Usuario> usuarios;
    private ArrayAdapter<Usuario> adapter;
    private ValueEventListener valueEventListenerPerfil;
    private EditText nome;
    private EditText email;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        nome  = findViewById(R.id.editNomeConf);
        email = findViewById(R.id.editEmailConf);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Preferencias preferencias = new Preferencias(getApplicationContext());
        identificadorUsuario = preferencias.getIdentificador();

        usuarios = new ArrayList<>();
        adapter  = new PerfilAdapter(ConfiguracoesActivity.this,usuarios);
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child(identificadorUsuario);

        valueEventListenerPerfil = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    usuario = new Usuario();
                    usuario.setNome(dataSnapshot.child("usuarios").child(identificadorUsuario).child("nome").getValue().toString());
                    usuario.setEmail(dataSnapshot.child("usuarios").child(identificadorUsuario).child("email").getValue().toString());
                }
                nome.setText(usuario.getNome().toString());
                email.setText(usuario.getEmail().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebase.addValueEventListener(valueEventListenerPerfil);
        if (verificarProviderLogin() == true){
            nome.setEnabled(false);
            email.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Login Feito pelo facebook", Toast.LENGTH_LONG).show();
        }
    }

    private boolean verificarProviderLogin() {
        boolean result = false;
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        for (UserInfo userInfo : autenticacao.getCurrentUser().getProviderData()) {
            if (userInfo.getProviderId().equals("facebook.com")) {
                result = true;
            }
        }
        return result;
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
        if (!nome.getText().toString().equals("")) {
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
