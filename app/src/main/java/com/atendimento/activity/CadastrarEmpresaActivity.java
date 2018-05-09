package com.atendimento.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.util.Util;
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
    private EditText nomeEmpresa;
    private Util util;
    private SQLiteDatabase sqLiteDatabasePar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_empresa);
        nomeEmpresa      = findViewById(R.id.editTextNomeEmpresa);
        util = new Util();
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        circleImageView = findViewById(R.id.circleImageEmpresa);
        listaCategoria = new ArrayList<String>();

        sqLiteDatabasePar = openOrCreateDatabase("databaseCategorias", MODE_PRIVATE, null);
        sqLiteDatabasePar.execSQL("DELETE FROM categorias");
        sqLiteDatabasePar.execSQL("CREATE TABLE IF NOT EXISTS categorias(codigo INT(3), descricao VARCHAR)");
        sqLiteDatabasePar.execSQL("INSERT INTO categorias(codigo, descricao)  VALUES(1, 'Clínicas Médicas')");
        sqLiteDatabasePar.execSQL("INSERT INTO categorias(codigo, descricao)  VALUES(2, 'Pet Shops')");
        sqLiteDatabasePar.execSQL("INSERT INTO categorias(codigo, descricao)  VALUES(3, 'Laboratórios')");
        sqLiteDatabasePar.execSQL("INSERT INTO categorias(codigo, descricao)  VALUES(4, 'Manicure/Pedicuere')");
        sqLiteDatabasePar.execSQL("INSERT INTO categorias(codigo, descricao)  VALUES(5, 'Salões de Beleza')");
        sqLiteDatabasePar.execSQL("INSERT INTO categorias(codigo, descricao)  VALUES(6, 'Escritórios Advocacia')");

        Cursor cursor = sqLiteDatabasePar.rawQuery("SELECT codigo, descricao FROM categorias",null);
        int indiceColunaDescricao = cursor.getColumnIndex("descricao");
        cursor.moveToFirst();

        while (!cursor.isLast()) {
            listaCategoria.add(cursor.getString(indiceColunaDescricao));
            cursor.moveToNext();
        }

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

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
