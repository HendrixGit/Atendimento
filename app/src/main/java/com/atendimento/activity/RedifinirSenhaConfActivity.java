package com.atendimento.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.fragment.SenhaDialog;
import com.atendimento.util.MyDialogFragmentListener;
import com.atendimento.util.Preferencias;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class RedifinirSenhaConfActivity extends BaseActivity implements MyDialogFragmentListener {

    private android.support.v7.widget.Toolbar toolbar;
    private Preferencias preferencias;
    private Button ok;
    private Button cancelar;
    private String identificadorUsuario;
    private EditText senha;
    private ProgressBar progressBar;
    private CheckBox checkBoxSenha;
    private FirebaseUser firebaseUser;
    private DialogFragment dialogFragment;
    private AuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redifinir_senha_conf);

        senha = findViewById(R.id.editTextSenhaConf);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Favor Digite a nova senha");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));

        preferencias = new Preferencias(getApplicationContext());
        identificadorUsuario = preferencias.getIdentificador();
        firebaseUser = ConfiguracaoFirebase.getAutenticacao().getCurrentUser();

        ok = findViewById(R.id.buttonOKSenhaConf);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!senha.getText().toString().equals("")){
                    firebaseUser.updatePassword(senha.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Senha alterada, com sucesso",Toast.LENGTH_SHORT).show();
                                        mudarTelaFinish(getApplicationContext(),ConfiguracoesActivity.class);
                                    }
                                    else{
                                        String erroExcecao = "";
                                        try {
                                            throw task.getException();
                                        }
                                        catch (FirebaseAuthWeakPasswordException e){
                                            erroExcecao = "Favor digite uma senha mais forte, contendo caracters e números";
                                        }
                                        catch (FirebaseAuthRecentLoginRequiredException recentLogin) {
                                            erroExcecao = "Erro Login Recente";
                                            dialogFragment = new SenhaDialog();
                                            dialogFragment.show(getFragmentManager(), "senha");
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                            erroExcecao = "Erro ao excluiir conta";
                                        }
                                        Toast.makeText(getApplicationContext(),erroExcecao,Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(getApplicationContext(),"Favor insira a senha",Toast.LENGTH_LONG).show();
                }
            }
        });

        cancelar = findViewById(R.id.buttonCancelarSenhaConf);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTelaFinish(getApplicationContext(),ConfiguracoesActivity.class);
            }
        });

        checkBoxSenha = findViewById(R.id.checkBoxSenhaConf);
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
    }

    @Override
    public void onReturnValue(String resultadoParametro) {
        if (!resultadoParametro.equals("")) {
            credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), resultadoParametro);
            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        ok.performClick();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Senha inválida", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
