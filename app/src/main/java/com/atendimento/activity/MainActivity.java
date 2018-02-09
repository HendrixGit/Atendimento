package com.atendimento.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends BaseActivity {

    private Button botaoSair;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        botaoSair = findViewById(R.id.buttonSair);
        botaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autenticacao = ConfiguracaoFirebase.getAutenticacao();
                autenticacao.signOut();
                LoginManager.getInstance().logOut();
                mudarTelaFinish(getApplicationContext(),  InicioActivity.class);
            }
        });
    }

}
