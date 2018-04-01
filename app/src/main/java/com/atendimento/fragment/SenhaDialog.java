package com.atendimento.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.config.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SenhaDialog extends DialogFragment {

    private EditText senha;
    private CheckBox checkBoxSenha;
    private View viewSenha;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private FirebaseUser usuarioFirebase;
    private AuthCredential credential;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        //ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar);
        ConfiguracaoFirebase.setProcessadoSucesso(false);
        usuarioFirebase = ConfiguracaoFirebase.getAutenticacao().getCurrentUser();
        builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        builder.setCancelable(false);
        inflater      = getActivity().getLayoutInflater();
        viewSenha     = inflater.inflate(R.layout.senha_dialog, null, false);
        senha         = viewSenha.findViewById(R.id.editSenhaDialog);
        senha.setTextColor(Color.WHITE);
        checkBoxSenha = viewSenha.findViewById(R.id.checkBoxSenha);
        checkBoxSenha.setTextColor(Color.WHITE);
        checkBoxSenha.setOnClickListener(new View.OnClickListener(){
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
        builder.setView(viewSenha)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (senha.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),R.string.favorSenha,Toast.LENGTH_LONG).show();
                        }
                        else{
                            credential = EmailAuthProvider.getCredential(usuarioFirebase.getEmail(), senha.getText().toString());
                            usuarioFirebase.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                ConfiguracaoFirebase.setProcessadoSucesso(true);
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Senha Inválida", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
