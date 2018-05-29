package com.atendimento.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
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
        //super.onBackPressed();
        mudarTelaFinish(getApplicationContext(), InicioActivity.class);
    }
}
