package com.atendimento.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.fragment.EmpresasAppFragment;
import com.atendimento.model.Usuario;
import com.atendimento.util.Preferencias;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


public class MainActivity extends BaseActivity {

    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerPerfil;
    private Preferencias preferencias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferencias = new Preferencias(getApplicationContext());
        if (preferencias.getNome().equals("")){
            carregarNome(preferencias.getIdentificador());
        }

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle(nomeApp);
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        setSupportActionBar(toolbarBase);

        adapterBase = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Empresas", EmpresasAppFragment.class)
                .create()
        );

        viewPagerBase = findViewById(R.id.viewPagerMain);
        viewPagerBase.setAdapter(adapterBase);

        viewPagerTab = findViewById(R.id.viewPagerTabMain);
        viewPagerTab.setViewPager(viewPagerBase);

    }

    private void carregarNome(final String identificadorUsuario){
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child(identificadorUsuario);

         valueEventListenerPerfil = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(dataSnapshot.child("usuarios").child(identificadorUsuario).child("id").getValue().toString());
                    usuario.setNome(dataSnapshot.child("usuarios").child(identificadorUsuario).child("nome").getValue().toString());
                    usuario.setEmail(dataSnapshot.child("usuarios").child(identificadorUsuario).child("email").getValue().toString());
                    preferencias.salvarDados(identificadorUsuario, usuario.getNome(), usuario.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebase.addListenerForSingleValueEvent(valueEventListenerPerfil);
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
            case R.id.item_sair:deslogaSairUsuario(); return true;
            case R.id.item_configuracoes:mudarTela(getApplicationContext(),ConfiguracoesActivity.class); return true;
            case R.id.item_empresa:mudarTela(getApplicationContext(), EmpresasActivity.class);   return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
