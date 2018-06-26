package com.atendimento.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.fragment.EmpresasFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class EmpresasActivity extends BaseActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private MaterialSearchView searchViewEmpresa;
    private MenuItem deletarEmpresa;
    private MenuItem pesquisarEmpresa;
    private FragmentPagerItemAdapter adapter;
    private SmartTabLayout viewPagerTab;
    private String titulo = "Empresas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(titulo);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Empresas Cadastradas", EmpresasFragment.class)
                        .create()
        );
        viewPager = findViewById(R.id.viewPagerEmpresas);
        viewPager.setAdapter( adapter );

        viewPagerTab = findViewById(R.id.viewPagerTabEmpresa);
        viewPagerTab.setViewPager( viewPager );

        searchViewEmpresa = findViewById(R.id.search_viewEmpresa);
        searchViewEmpresa.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                viewPagerTab.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {
                apareceTabEmpresas();
            }
        });
        searchViewEmpresa.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarEmpresa(newText);
                return true;
            }
        });

        deletarEmpresa   = findViewById(R.id.item_deletarEmpresa);
        pesquisarEmpresa = findViewById(R.id.item_pesquisaEmpresa);
    }

    private void apareceTabEmpresas() {
        viewPagerTab.setVisibility(View.VISIBLE);
        EmpresasFragment fragment = (EmpresasFragment) adapter.getPage(0);
        fragment.recarregarEmpresas();
    }

    private void pesquisarEmpresa(String newText) {
        EmpresasFragment fragment = (EmpresasFragment) adapter.getPage(0);
        if (newText != null && !newText.isEmpty()) {
            fragment.pesquisarEmpresa(newText.toLowerCase());
        }
        else{
            fragment.recarregarEmpresas();
        }
    }

    public void setTituloToolbar(String texto){
        toolbar.setTitle(texto);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorSelecionado));
        viewPagerTab.setBackgroundColor(getResources().getColor(R.color.colorSelecionado));
        pesquisarEmpresa.setVisible(false);
        deletarEmpresa.setVisible(true);
        deletarEmpresa.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void toolbarPadrao(){
        toolbar.setTitle(titulo);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        viewPagerTab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        pesquisarEmpresa.setVisible(true);
        pesquisarEmpresa.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        deletarEmpresa.setVisible(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        EmpresasFragment fragment = (EmpresasFragment) adapter.getPage(0);
        fragment.zerarSelecao();
    }



    @Override
    public void onBackPressed() {
        mudarTelaFinish(getApplicationContext(), InicioActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);
        MenuItem menuItemPesquisa  = menu.findItem(R.id.item_pesquisaEmpresa);
        MenuItem menuItemDeletar   = menu.findItem(R.id.item_deletarEmpresa);
        searchViewEmpresa.setMenuItem(menuItemPesquisa);
        pesquisarEmpresa = menuItemPesquisa;
        deletarEmpresa   = menuItemDeletar;
        deletarEmpresa.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_sair            : deslogaSairUsuario(); return true;
            case R.id.item_tela_Principal  : mudarTelaFinish(getApplicationContext(),  MainActivity.class);
            case R.id.item_deletarEmpresa  : return true;
            case android.R.id.home:  toolbarPadrao();
            default: return super.onOptionsItemSelected(item);
        }
    }

}

