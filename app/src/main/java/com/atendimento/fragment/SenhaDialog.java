package com.atendimento.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.atendimento.R;
import com.atendimento.util.MyDialogFragmentListener;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SenhaDialog extends DialogFragment {

    private EditText senha;
    private String senhaParemetro = "";
    private CheckBox checkBoxSenha;
    private View viewSenha;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private MyDialogFragmentListener activityChamada;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
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
                            senhaParemetro = senha.getText().toString();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        senhaParemetro = "";
                    }
                });
        return builder.create();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        activityChamada = (MyDialogFragmentListener) getActivity();
        activityChamada.onReturnValue(senhaParemetro);
        super.onDismiss(dialog);
    }

}
