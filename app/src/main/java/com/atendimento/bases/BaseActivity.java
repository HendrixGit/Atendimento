package com.atendimento.bases;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;

import com.atendimento.R;
import com.atendimento.activity.InicioActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends AppCompatActivity {

    protected String[]  permissoesNecessarias = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    protected Toolbar toolbarBase;
    protected ViewPager viewPagerBase;
    protected MaterialSearchView searchViewBase;
    protected SmartTabLayout viewPagerTab;
    protected FragmentPagerItemAdapter adapterBase;

    protected ConnectivityManager cm;
    protected static String nomeApp = "Atendimento";
    protected InputMethodManager imm;
    protected SQLiteDatabase sqLiteDatabasePar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected void mudarTela(Context contexto, Class classe) {
        Intent intent = new Intent(contexto, classe);
        startActivity(intent);
    }

    protected void mudarTelaFinish(Context contexto, Class classe) {
        Intent intent = new Intent(contexto, classe);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }

    protected void mudarTelaParametroFlag(Context contexto, Class classe, String parametro, String parametro2, Integer parametro3, Boolean diaParametro) {
        Intent intent = new Intent(contexto, classe);
        intent.putExtra("hora", parametro);
        intent.putExtra("hora2", parametro2);
        intent.putExtra("duracao", parametro3);
        intent.putExtra("dia", diaParametro);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes){
        if (Build.VERSION.SDK_INT >= 23){
            List<String> listaPermissoes = new ArrayList<>();
            for(String permissao : permissoes){
                if(ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED == false){
                    listaPermissoes.add(permissao);
                }
            }
            if (!listaPermissoes.isEmpty()) {
                String[] novasPermissoes = new String[listaPermissoes.size()];
                listaPermissoes.toArray(novasPermissoes);
                ActivityCompat.requestPermissions(activity, novasPermissoes, 1);
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissoes, int[] grantResults){//verificar se as opcoes foi negada
        super.onRequestPermissionsResult(requestCode, permissoes, grantResults);
        for(int resultado : grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negagas: ");
        builder.setMessage("Para utilizar o app é preciso aceitar as permissões");
        builder.setPositiveButton("CONFIRMA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected boolean verificarConexaoInternet(){
        cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    protected void deslogaSairUsuario() {
        FirebaseAuth autenticacao;
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.signOut();
        LoginManager.getInstance().logOut();
        mudarTelaFinish(getApplicationContext(),  InicioActivity.class);
    }

    protected SQLiteDatabase databaseCategorias(){
        sqLiteDatabasePar = openOrCreateDatabase("databaseCategorias", MODE_PRIVATE, null);
        sqLiteDatabasePar.execSQL("DROP TABLE categorias");
        sqLiteDatabasePar.execSQL("CREATE TABLE IF NOT EXISTS categorias(codigo INT(3), descricao VARCHAR)");
        sqLiteDatabasePar.execSQL("DELETE FROM categorias");
        sqLiteDatabasePar.execSQL("INSERT INTO categorias(codigo, descricao)  VALUES(1, 'Clínicas Médicas')");
        sqLiteDatabasePar.execSQL("INSERT INTO categorias(codigo, descricao)  VALUES(2, 'Pet Shops')");
        sqLiteDatabasePar.execSQL("INSERT INTO categorias(codigo, descricao)  VALUES(3, 'Laboratórios')");
        sqLiteDatabasePar.execSQL("INSERT INTO categorias(codigo, descricao)  VALUES(4, 'Manicure/Pedicuere')");
        sqLiteDatabasePar.execSQL("INSERT INTO categorias(codigo, descricao)  VALUES(5, 'Salões de Beleza')");
        sqLiteDatabasePar.execSQL("INSERT INTO categorias(codigo, descricao)  VALUES(6, 'Escritórios Advocacia')");

        sqLiteDatabasePar.execSQL("DROP TABLE duracao");
        sqLiteDatabasePar.execSQL("CREATE TABLE IF NOT EXISTS duracao(codigo INT(3), descricao VARCHAR, duracaoHorario INT(3))");
        sqLiteDatabasePar.execSQL("DELETE FROM duracao");
        sqLiteDatabasePar.execSQL("INSERT INTO duracao(codigo, descricao, duracaoHorario)  VALUES(1, '15 Minutos', 15)");
        sqLiteDatabasePar.execSQL("INSERT INTO duracao(codigo, descricao, duracaoHorario)  VALUES(2, '30 Minutos', 30)");
        sqLiteDatabasePar.execSQL("INSERT INTO duracao(codigo, descricao, duracaoHorario)  VALUES(3, '60 Minutos', 60)");
        sqLiteDatabasePar.execSQL("INSERT INTO duracao(codigo, descricao, duracaoHorario)  VALUES(4, '80 Minutos', 80)");
        sqLiteDatabasePar.execSQL("INSERT INTO duracao(codigo, descricao, duracaoHorario)  VALUES(5, '120 Minutos', 120)");
        sqLiteDatabasePar.execSQL("INSERT INTO duracao(codigo, descricao, duracaoHorario)  VALUES(6, '180 Minutos', 180)");

        return sqLiteDatabasePar;
    }

    protected Cursor cursorCategorias(SQLiteDatabase parametroDatabase, String parametros){
        Cursor cursor;
        if (parametros.isEmpty()) {
            cursor = parametroDatabase.rawQuery("SELECT codigo, descricao FROM categorias", null);
        }
        else {
            String[] params = new String[]{parametros};
            cursor = parametroDatabase.rawQuery("SELECT codigo, descricao FROM categorias WHERE descricao = ?", params);
        }
        return cursor;
    }


    protected Cursor cursorDuracao(SQLiteDatabase parametroDatabase, String parametros){
        Cursor cursor;
        if (parametros.isEmpty()) {
            cursor = parametroDatabase.rawQuery("SELECT codigo, descricao FROM duracao", null);
        }
        else {
            String[] params = new String[]{parametros};
            try {
                Integer.parseInt(parametros);
                cursor = parametroDatabase.rawQuery("SELECT codigo, descricao, duracaoHorario FROM duracao WHERE duracaoHorario = ?", params);
            }
            catch (Exception e){
                cursor = parametroDatabase.rawQuery("SELECT codigo, descricao, duracaoHorario FROM duracao WHERE descricao = ?", params);
            }
        }
        return cursor;
    }

}
