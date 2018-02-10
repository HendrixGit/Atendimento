package com.atendimento.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Usuario;
import com.atendimento.util.Base64Custom;
import com.atendimento.util.Preferencias;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class InicioActivity extends BaseActivity {

    private LoginButton botaoLoginFacebook;
    private Button botaoEmail;
    private FirebaseAuth autenticacao;
    private CallbackManager mCallbackManager;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        verificarUsuarioLogado();

        botaoEmail    = findViewById(R.id.buttonLoginEmail);

        mCallbackManager = CallbackManager.Factory.create();
        botaoLoginFacebook = findViewById(R.id.loginButtonFacebook);
        botaoLoginFacebook.setReadPermissions("email", "public_profile");
        botaoLoginFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

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

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Facebook", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        autenticacao.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Facebook", "signInWithCredential:success");
                            FirebaseUser usuarioFirebase =  task.getResult().getUser();
                            String identificadorUsuario  = Base64Custom.codificarBase64(usuarioFirebase.getEmail());
                            usuario = new Usuario();
                            usuario.setId(identificadorUsuario);
                            usuario.setEmail(usuarioFirebase.getEmail());
                            usuario.setNome(usuarioFirebase.getEmail());
                            usuario.salvar();
                            Preferencias preferencias = new Preferencias(getApplicationContext());
                            preferencias.salvarDados(identificadorUsuario,usuarioFirebase.getEmail());
                            Toast.makeText(getApplicationContext(),"Sucesso no cadastro Bem-Vindo ",Toast.LENGTH_LONG).show();
                            mudarTelaFinish(getApplicationContext(),MainActivity.class);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            String erroExececao = "";
                            try {
                                throw task.getException();
                            }
                            catch(FirebaseAuthUserCollisionException e){
                                erroExececao = "E-mail ja cadastrado, como Login com e-mail";

                            }
                            catch (Exception e){
                                erroExececao = "Falha no Login";
                            }
                            Log.w("Facebook", "signInWithCredential:failure", task.getException());
                            LoginManager.getInstance().logOut();
                            Toast.makeText(InicioActivity.this, erroExececao, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
