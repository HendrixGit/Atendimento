package com.atendimento.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;

public class LoginActivity extends BaseActivity {

    private ImageView botaoLogar;
    private TextView textoCadastro;
    private EditText email;
    private EditText senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editEmail);
        senha = findViewById(R.id.editSenha);
        botaoLogar = findViewById(R.id.imageLogar);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!email.getText().toString().equals("")) && (!senha.getText().toString().equals(""))){

                }
                else{
                    Toast.makeText(getApplicationContext(),"Favor preencha os campos",Toast.LENGTH_LONG).show();
                }
            }
        });

        textoCadastro = findViewById(R.id.textCadastro);
        textoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTela(getApplicationContext(),CadastroActivity.class);
            }
        });
    }


}
