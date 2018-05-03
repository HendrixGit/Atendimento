package com.atendimento.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Usuario;
import com.atendimento.util.Preferencias;
import com.atendimento.util.Util;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URL;


public class InicioActivity extends BaseActivity {

    private LoginButton botaoLoginFacebook;
    private Button botaoEmail;
    private FirebaseAuth autenticacao;
    private CallbackManager mCallbackManager;
    private Usuario usuario;
    private Bitmap  imagemPerfil = null;
    private StorageReference storageReference;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_inicio);

        util = new Util();
        verificarUsuarioLogado();

        botaoEmail    = findViewById(R.id.buttonLoginEmail);
        mCallbackManager = CallbackManager.Factory.create();
        botaoLoginFacebook = findViewById(R.id.loginButtonFacebook);
        botaoLoginFacebook.setReadPermissions("email", "public_profile");

        botaoLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

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
                if (verificarConexaoInternet() == false) {
                    Toast.makeText(getApplicationContext(),"Sem conexão com Internet ",Toast.LENGTH_LONG).show();
                }
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

    private void salvarFotoPerfil(Uri uri, String identifacorUsuario, String idFacebook){
        try {
            URL imageURL = new URL("https://graph.facebook.com/" + idFacebook + "/picture?type=large");
            imagemPerfil = util.diminuirImagem(BitmapFactory.decodeStream(imageURL.openConnection().getInputStream()),200,200);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagemPerfil.compress(Bitmap.CompressFormat.PNG, 50, stream);

            byte[] byteArray = stream.toByteArray();

            storageReference = ConfiguracaoFirebase.getStorage().child("usuarios").child(identifacorUsuario);
            UploadTask uploadTask = storageReference.putBytes(byteArray);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Falha ao enviar foto", Toast.LENGTH_LONG).show();
                    Log.i("erroFotoInicio", e.toString() + " " + e.getCause().toString());
                }
            });

        }
        catch (Exception e){
            Log.i("erroFotoInicio", e.getCause().toString() + " " + e.getMessage());
        }
    }

    private void handleFacebookAccessToken(final AccessToken token) {
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
                            String identificadorUsuario  =  usuarioFirebase.getUid();
                            usuario = new Usuario();
                            usuario.setId(identificadorUsuario);
                            usuario.setEmail(usuarioFirebase.getEmail());
                            usuario.setNome(usuarioFirebase.getDisplayName());
                            usuario.salvar();
                            salvarFotoPerfil(usuarioFirebase.getPhotoUrl(),identificadorUsuario, token.getUserId());
                            Preferencias preferencias = new Preferencias(getApplicationContext());
                            preferencias.salvarDados(identificadorUsuario,usuarioFirebase.getDisplayName(), usuarioFirebase.getEmail());
                            mudarTelaFinish(getApplicationContext(),MainActivity.class);
                            Toast.makeText(getApplicationContext(),"Sucesso no cadastro Bem-Vindo ",Toast.LENGTH_LONG).show();
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
