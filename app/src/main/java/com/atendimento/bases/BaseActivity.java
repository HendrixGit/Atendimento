package com.atendimento.bases;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.atendimento.R;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends AppCompatActivity {

    protected String[]  permissoesNecessarias = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    protected ConnectivityManager cm;
    protected static String nomeApp = "Atendimento";

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
        //finish();
        ActivityCompat.finishAffinity(this);
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

}
