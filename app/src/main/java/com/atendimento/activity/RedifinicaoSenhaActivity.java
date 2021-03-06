package com.atendimento.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.util.Preferencias;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;

public class RedifinicaoSenhaActivity extends BaseActivity {

    private Button botaoRedefinir;
    private EditText email;
    private FirebaseAuth autenticacao;
    private Preferencias preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redifinicao_senha);

        preferencias = new Preferencias(getApplicationContext());
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        email = findViewById(R.id.editEmailRedefinicao);
        botaoRedefinir = findViewById(R.id.buttonRedefinir);
        botaoRedefinir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().equals("")){
                    autenticacao.sendPasswordResetEmail(email.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        mudarTelaFinish(getApplication(),LoginActivity.class);
                                        Toast.makeText(getApplicationContext(),
                                                "Um e-mail para a redifinição foi enviado para você, prossiga com  redefinição de senha por lá",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        String erroExececao = "";
                                        try{
                                            throw task.getException();
                                        }
                                        catch (FirebaseAuthRecentLoginRequiredException recentLogin){
                                            erroExececao = "Erro Login Recente";
                                        }

                                        catch (FirebaseAuthInvalidCredentialsException e){
                                            erroExececao = "E-mail digitado inválido";
                                        }
                                        catch(Exception e){
                                            erroExececao = "Falha ao enviar e-mail";
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(getApplicationContext(),erroExececao, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(),"Favor digite o e-mail",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
