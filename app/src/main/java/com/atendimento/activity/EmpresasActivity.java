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
    private FragmentPagerItemAdapter adapter;
    private SmartTabLayout viewPagerTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Empresas");
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
    }

    private void apareceTabEmpresas() {
        viewPagerTab.setVisibility(View.VISIBLE);
        EmpresasFragment fragment = (EmpresasFragment) adapter.getPage(0);
        fragment.recarregarEmpresas();
    }

    private void pesquisarEmpresa(String newText) {
        EmpresasFragment fragment = (EmpresasFragment) adapter.getPage(0);
        if (newText != null && !newText.isEmpty()) {
            fragment.pesquisarEmpresa(newText);
        }
        else{ fragment.recarregarEmpresas(); }
    }

    @Override
    public void onBackPressed() {
        mudarTelaFinish(getApplicationContext(), InicioActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);
        MenuItem menuItem = menu.findItem(R.id.item_pesquisaEmpresa);
        searchViewEmpresa.setMenuItem(menuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_sair            : deslogaSairUsuario(); return true;
            case R.id.item_tela_Principal  : mudarTelaFinish(getApplicationContext(),  MainActivity.class);
            default: return super.onOptionsItemSelected(item);
        }
    }

}

