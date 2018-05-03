package com.atendimento.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastrarEmpresaActivity extends BaseActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private String identificadorUsuario;
    private DatabaseReference firebase;
    private Button ok;
    private Button cancel;
    private CircleImageView circleImageView;
    private Spinner spinnerCategoria;
    private List<String> listaCategoria;
    private ArrayAdapter<String> dataCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_empresa);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        listaCategoria = new ArrayList<String>();
        listaCategoria.add("MK");
        dataCategoria = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listaCategoria);
        spinnerCategoria.setAdapter(dataCategoria);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastro de Empresa");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        circleImageView = findViewById(R.id.circleImageEmpresa);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ok     = findViewById(R.id.buttonEmpresaOK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancel = findViewById(R.id.buttonEmpresaCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTelaFinish(getApplicationContext(),MainActivity.class);
            }
        });
    }
}
