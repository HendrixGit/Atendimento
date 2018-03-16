package com.atendimento.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Usuario;
import com.atendimento.util.Preferencias;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends BaseActivity {

    private FirebaseAuth autenticacao;
    private Toolbar toolbar;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerPerfil;
    private Preferencias preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(nomeApp);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        setSupportActionBar(toolbar);

        preferencias = new Preferencias(getApplicationContext());
        if (preferencias.getNome().equals("")){
            carregarNome(preferencias.getIdentificador());
        }
    }

    private void carregarNome(final String identificadorUsuario){
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child(identificadorUsuario);

        valueEventListenerPerfil = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = new Usuario();
                usuario.setNome(dataSnapshot.child("usuarios").child(identificadorUsuario).child("nome").getValue().toString());
                preferencias.salvarDados(identificadorUsuario, usuario.getNome());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebase.addValueEventListener(valueEventListenerPerfil);
    }

    private void deslogarUsuario() {
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.signOut();
        LoginManager.getInstance().logOut();
        mudarTelaFinish(getApplicationContext(),  InicioActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sair:deslogarUsuario(); return true;
            case R.id.item_configuracoes:mudarTela(getApplicationContext(),ConfiguracoesActivity.class); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
