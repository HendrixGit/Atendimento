package com.atendimento.activity;

import android.app.AppOpsManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.atendimento.R;
import com.atendimento.adapter.TabAdapterEmpresa;
import com.atendimento.bases.BaseActivity;
import com.atendimento.util.SlidingTabLayout;
import com.wang.avi.AVLoadingIndicatorView;

public class EmpresasActivity extends BaseActivity {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private AVLoadingIndicatorView  avi;
    private FloatingActionButton cadastrarEmpresaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);

        cadastrarEmpresaButton = findViewById(R.id.floatButtonCadastrarEmpresa);
        cadastrarEmpresaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTela(getApplicationContext(), CadastrarEmpresaActivity.class);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Empresas");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        avi = findViewById(R.id.avi_Empresas);

        slidingTabLayout = findViewById(R.id.tabEmpresas);
        slidingTabLayout.setDistributeEvenly(true);//preenche toda a largura
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorPrimaryDark));

        TabAdapterEmpresa tabAdapter = new TabAdapterEmpresa(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPagerEmpresas);
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        mudarTelaFinish(getApplicationContext(), InicioActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            default: return super.onOptionsItemSelected(item);
        }
    }
}

