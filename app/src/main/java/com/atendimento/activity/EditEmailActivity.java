package com.atendimento.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.util.Preferencias;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class EditEmailActivity extends BaseActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private Preferencias preferencias;
    private String identificadorUsuario;
    private EditText nome;
    private EditText email;
    private DatabaseReference firebase;
    private FirebaseUser usuarioFirebase;
    private Button cancelar;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preferencias);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Favor Digite o novo e-mail");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        preferencias = new Preferencias(getApplicationContext());
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

                                    LinearLayout layout = new LinearLayout(getApplicationContext());
                                    layout.setOrientation(LinearLayout.VERTICAL);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditEmailActivity.this, R.style.dialog);
                                    builder.setTitle("Favor Insira a Senha");
                                    final EditText senha = new EditText(getApplicationContext());
                                    senha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                    senha.setTextColor(Color.WHITE);
                                    final CheckBox checkBoxSenha = new CheckBox(getApplicationContext());
                                    checkBoxSenha.setText("Mostrar Senha");
                                    checkBoxSenha.setTextColor(Color.WHITE);
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
                                    layout.addView(senha);
                                    layout.addView(checkBoxSenha);
                                    builder.setView(layout);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (senha.getText().toString().equals("")) {
                                                Toast.makeText(getApplicationContext(),"Senha Inválida", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                AuthCredential credential = EmailAuthProvider.
                                                        getCredential(usuarioFirebase.getEmail(), senha.getText().toString());
                                                usuarioFirebase.reauthenticate(credential)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    ok.performClick();
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Senha Inválida", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();

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
}
