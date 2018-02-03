package com.atendimento.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.atendimento.R;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private ImageView botaoCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome  = findViewById(R.id.editCadNome);
        email = findViewById(R.id.editCadEmail);
        senha = findViewById(R.id.editCadSenha);
        botaoCadastrar = findViewById(R.id.imageViewCadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((!nome.getText().toString().equals("")) && (!email.getText().toString().equals("")) && (!senha.getText().toString().equals(""))){
                    cadastrarUsuario();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Favor preencha todos os campos",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void cadastrarUsuario(){

    }
}
