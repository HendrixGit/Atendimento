package com.atendimento.activity;

import android.app.DialogFragment;
import android.content.DialogInterface;
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
import com.atendimento.fragment.SenhaDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class EditEmailActivity extends BaseActivity implements DialogInterface.OnDismissListener {

    private android.support.v7.widget.Toolbar toolbar;
    private Preferencias preferencias;
    private String identificadorUsuario;
    private EditText nome;
    private EditText email;
    private DatabaseReference firebase;
    private FirebaseUser usuarioFirebase;
    private Button cancelar;
    private Button ok;
    private DialogFragment dialogFragment;
    private FirebaseAuth autenticacao;
    private Boolean resultadoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preferencias);

        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Favor Digite o novo e-mail");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        preferencias = new Preferencias(getApplicationContext());
        resultadoDialog = ConfiguracaoFirebase.getProcessadoSucesso();
        firebase        = ConfiguracaoFirebase.getFirebaseDatabase();
        usuarioFirebase = ConfiguracaoFirebase.getAutenticacao().getCurrentUser();
        identificadorUsuario = usuarioFirebase.getUid();
        nome = findViewById(R.id.editTextNome);
        nome.setText(preferencias.getNome());
        nome.setVisibility(View.GONE);
        email = findViewById(R.id.editTextEmail);
        cancelar = findViewById(R.id.buttonEditCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTelaFinish(getApplicationContext(),ConfiguracoesActivity.class);
            }
        });

        ok = findViewById(R.id.buttonEditOK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().equals("")){
                    usuarioFirebase.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                firebase.child("usuarios").child(identificadorUsuario).child("email").setValue(email.getText().toString());
                                preferencias.salvarDados(identificadorUsuario,nome.getText().toString(),email.getText().toString());
                                mudarTelaFinish(getApplicationContext(),ConfiguracoesActivity.class);
                            }
                            else{
                                String erroExcecao = "";
                                try {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthRecentLoginRequiredException recentLogin){
                                    erroExcecao = "Erro Login Recente";
                                    dialogFragment = new SenhaDialog();
                                    dialogFragment.show(getFragmentManager(),"senha");
                                }
                                catch (FirebaseAuthInvalidCredentialsException e){
                                    erroExcecao = "E-mail digitado inválido";
                                }
                                catch (FirebaseAuthUserCollisionException e){
                                    erroExcecao = "E-mail já cadastrado";
                                }
                                catch (Exception e){
                                    erroExcecao = "Erro ao atualizar e-mail";
                                    e.printStackTrace();
                                }

                                Toast.makeText(getApplicationContext(),erroExcecao,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"Favor Insira o email",Toast.LENGTH_LONG).show();
                }
            }
        });
        preferencias = new Preferencias(getApplicationContext());
        email.setText(preferencias.getEmail());
        identificadorUsuario = preferencias.getIdentificador();
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (ConfiguracaoFirebase.getProcessadoSucesso() == true) {
            ok.performClick();
        }
    }
}
