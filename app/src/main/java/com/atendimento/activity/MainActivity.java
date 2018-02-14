package com.atendimento.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends BaseActivity {

    private Button botaoSair;
    private FirebaseAuth autenticacao;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Atendimento");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        setSupportActionBar(toolbar);

        botaoSair = findViewById(R.id.buttonSair);
        botaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deslogarUsuario();
            }
        });
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
            case R.id.item_sair:
                deslogarUsuario(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
