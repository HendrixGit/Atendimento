package com.atendimento.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Usuario;
import com.atendimento.util.Preferencias;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends BaseActivity {

    private Button botaoLogar;
    private TextView textoCadastro;
    private TextView textoEsqueci;
    private EditText email;
    private EditText senha;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    private Usuario usuario;
    private CheckBox checkBoxSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editEmail);
        senha = findViewById(R.id.editSenha);
        checkBoxSenha = findViewById(R.id.checkBoxSenhaLogin);
        checkBoxSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxSenha.isChecked()){
                    senha.setTransformationMethod(null);
                }
                else{
                    senha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                senha.setSelection(senha.getText().length());
            }
        });

        botaoLogar = findViewById(R.id.buttonLoginEntrar);
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();

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
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    String identificadorUsuario  = usuarioFirebase.getUid();
                    Preferencias preferencias = new Preferencias(getApplicationContext());
                    preferencias.salvarDados(identificadorUsuario, "", usuarioFirebase.getEmail());
                    mudarTelaFinish(getApplicationContext(), MainActivity.class);
                    Toast.makeText(getApplicationContext(),"Bem vindo ", Toast.LENGTH_LONG).show();
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
