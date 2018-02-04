package com.atendimento.activity;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Usuario;
import com.atendimento.util.Base64Custom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends BaseActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private ImageView botaoCadastrar;
    private FirebaseAuth autenticacao;

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
        final Usuario usuario = new Usuario();
        usuario.setNome(nome.getText().toString());
        usuario.setEmail(email.getText().toString());
        usuario.setSenha(senha.getText().toString());

        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(usuarioFirebase.getUid());
                    usuario.salvar();
                    Toast.makeText(getApplicationContext(),"Sucesso no cadastro Bem-Vindo " + usuario.getNome(),Toast.LENGTH_LONG).show();
                    mudarTelaFinish(getApplication(),MainActivity.class);
                }
                else{
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Favor digite uma senha mais forte, contendo caracters e números";
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "E-mail digitado inválido";
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "E-mail já cadastrado";
                    }
                    catch (Exception e){
                        erroExcecao = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),erroExcecao,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
