package com.atendimento.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.atendimento.R;
import com.atendimento.adapter.TabAdapterEmpresa;
import com.atendimento.bases.BaseActivity;
import com.atendimento.fragment.EmpresasFragment;
import com.atendimento.util.SlidingTabLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.wang.avi.AVLoadingIndicatorView;

public class EmpresasActivity extends BaseActivity {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private AVLoadingIndicatorView  avi;
    private MaterialSearchView searchViewEmpresa;
    private TabAdapterEmpresa tabAdapter;
    private FragmentPagerItemAdapter adapter;

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
                        .add("Empresas", EmpresasFragment.class)
                        .create()
        );
        viewPager = findViewById(R.id.viewPagerEmpresas);
        viewPager.setAdapter( adapter );

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTabEmpresa);
        viewPagerTab.setViewPager( viewPager );

        searchViewEmpresa = findViewById(R.id.search_viewEmpresa);
        searchViewEmpresa.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                EmpresasFragment fragment = (EmpresasFragment) adapter.getPage(0);
                if (newText != null && !newText.isEmpty()) {
                    fragment.pesquisarEmpresa(newText);
                }
                return true;
            }
        });
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

