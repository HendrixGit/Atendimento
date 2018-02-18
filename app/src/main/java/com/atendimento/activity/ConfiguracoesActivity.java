package com.atendimento.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.model.Usuario;
import com.atendimento.util.Preferencias;
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
        toolbar.setTitle(nomeApp);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listView = findViewById(R.id.listViewConfiguracoes);

        Preferencias preferencias = new Preferencias(getApplicationContext());
        identificadorUsuario = preferencias.getIdentificador();

    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: mudarTelaFinish(getApplicationContext(), MainActivity.class);return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
