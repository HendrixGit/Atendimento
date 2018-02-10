package com.atendimento.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends BaseActivity {

    private Button botaoLogar;
    private TextView textoCadastro;
    private TextView textoEsqueci;
    private EditText email;
    private EditText senha;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editEmail);
        senha = findViewById(R.id.editSenha);
        botaoLogar = findViewById(R.id.buttonLoginEntrar);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!email.getText().toString().equals("")) && (!senha.getText().toString().equals(""))){
                    logarUsuario();
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

        textoEsqueci = findViewById(R.id.textoEsqueci);
        textoEsqueci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTela(getApplicationContext(),RedifinicaoSenhaActivity.class);
            }
        });
    }

    private void logarUsuario(){
        usuario = new Usuario();
        usuario.setEmail(email.getText().toString());
        usuario.setSenha(senha.getText().toString());
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Bem vindo ", Toast.LENGTH_LONG).show();
                    mudarTelaFinish(getApplicationContext(), MainActivity.class);
                }
                else{
                    String erroExececao = "";
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException e){
                        erroExececao = "E-mail incorreto, ou não cadastrado";
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        erroExececao = "Senha incorreta";
                    }
                    catch(Exception e){
                        erroExececao = "Erro ao logar";
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),erroExececao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
