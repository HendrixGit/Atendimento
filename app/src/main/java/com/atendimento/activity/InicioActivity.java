package com.atendimento.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class InicioActivity extends BaseActivity {

    private ImageView botaoFacebook;
    private ImageView botaoEmail;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        verificarUsuarioLogado();

        botaoFacebook = findViewById(R.id.imageFacebook);
        botaoEmail    = findViewById(R.id.imageEmail);

        botaoFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        botaoEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTela(getApplicationContext(), LoginActivity.class);
            }
        });

        validaPermissoes(1,this, permissoesNecessarias);

    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        if (autenticacao.getCurrentUser() != null){
            mudarTelaFinish(getApplicationContext(), MainActivity.class);
        }
    }


}
