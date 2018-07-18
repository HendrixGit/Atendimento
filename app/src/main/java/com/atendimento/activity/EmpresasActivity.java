package com.atendimento.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.fragment.EmpresasFragment;
import com.atendimento.util.Util;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


public class EmpresasActivity extends BaseActivity {

    private MenuItem deletarEmpresa;
    private MenuItem pesquisarEmpresa;
    private String titulo = "Empresas";
    private Util util;
    private DialogInterface.OnClickListener yesEcluirEmpresa;
    private AlertDialog dialogExcluir;
    private EmpresasFragment fragment;
    public  Boolean pesquisando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);
        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        toolbarBase.setTitle(titulo);
        setSupportActionBar(toolbarBase);
        getSupportActionBar().setElevation(0);
        util = new Util();

        yesEcluirEmpresa = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EmpresasFragment fragment =  (EmpresasFragment) adapterBase.getPage(0);
                fragment.deletarEmpresa();
                toolbarPadrao();
            }
        };

        adapterBase = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Empresas Cadastradas", EmpresasFragment.class)
                        .create()
        );
        viewPagerBase = findViewById(R.id.viewPagerEmpresas);
        viewPagerBase.setAdapter( adapterBase );

        viewPagerTab = findViewById(R.id.viewPagerTabEmpresa);
        viewPagerTab.setViewPager( viewPagerBase );

        searchViewBase = findViewById(R.id.search_view);
        searchViewBase.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                criaFragmenteEmpresas();
                pesquisando = true;
                viewPagerTab.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {
                apareceTabEmpresas();
            }
        });
        searchViewBase.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
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
        criaFragmenteEmpresas();
    }

    private void apareceTabEmpresas() {
        pesquisando = false;
        viewPagerTab.setVisibility(View.VISIBLE);
        criaFragmenteEmpresas();
        fragment.recarregarEmpresas();
    }

    private void pesquisarEmpresa(String newText) {
        criaFragmenteEmpresas();
        if (newText != null && !newText.isEmpty()) {
            fragment.pesquisarEmpresa(newText.toLowerCase());
        }
        else{
            fragment.recarregarEmpresas();
        }
    }

    private void criaFragmenteEmpresas() {
        if (fragment == null) {
            fragment = (EmpresasFragment) adapterBase.getPage(0);
        }
    }

    public void setTituloToolbar(String texto){
        toolbarBase.setTitle(texto);
        toolbarBase.setBackgroundColor(getResources().getColor(R.color.colorSelecionado));
        viewPagerTab.setBackgroundColor(getResources().getColor(R.color.colorSelecionado));
        pesquisarEmpresa.setVisible(false);
        deletarEmpresa.setVisible(true);
        deletarEmpresa.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void toolbarPadrao(){
        toolbarBase.setTitle(titulo);
        toolbarBase.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        viewPagerTab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        pesquisarEmpresa.setVisible(true);
        pesquisarEmpresa.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        deletarEmpresa.setVisible(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        criaFragmenteEmpresas();
        fragment.zerarSelecao();
    }


    @Override
    protected void onStop() {
        super.onStop();
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
        searchViewBase.setMenuItem(menuItemPesquisa);
        pesquisarEmpresa = menuItemPesquisa;
        deletarEmpresa   = menuItemDeletar;
        deletarEmpresa.setVisible(false);
        deletarEmpresa.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AlertDialog.Builder builder = util.YesNoDialog("Deseja realmente excluir?", EmpresasActivity.this, yesEcluirEmpresa);
                dialogExcluir = builder.create();
                dialogExcluir.show();
                return false;
            }
        });
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

