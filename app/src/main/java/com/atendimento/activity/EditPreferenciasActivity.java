package com.atendimento.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.util.Preferencias;
import com.google.firebase.database.DatabaseReference;

public class EditPreferenciasActivity extends BaseActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private Preferencias preferencias;
    private EditText nome;
    private Button ok;
    private Button cancelar;
    private DatabaseReference firebase;
    private String identificadorUsuario;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preferencias);

        email = findViewById(R.id.editTextEmail);
        email.setVisibility(View.GONE);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Favor Digite o seu nome");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));

        preferencias = new Preferencias(getApplicationContext());
        identificadorUsuario = preferencias.getIdentificador();
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        nome = findViewById(R.id.editTextNome);
        nome.setText(preferencias.getNome().toString());

        ok = findViewById(R.id.buttonEditOK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarNome();
            }
        });

        cancelar = findViewById(R.id.buttonEditCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTelaFinish(getApplicationContext(),ConfiguracoesActivity.class);
            }
        });
    }

    private void salvarNome() {
        if (!nome.getText().toString().equals("")) {
            firebase.child("usuarios")
                    .child(identificadorUsuario)
                    .child("nome").setValue(nome.getText().toString());
            preferencias.salvarDados(identificadorUsuario, nome.getText().toString(), preferencias.getEmail());
            mudarTelaFinish(getApplicationContext(), ConfiguracoesActivity.class);
        }
        else{
            Toast.makeText(getApplicationContext(),"Favor Preencha o nome",Toast.LENGTH_LONG).show();
        }
    }

}
