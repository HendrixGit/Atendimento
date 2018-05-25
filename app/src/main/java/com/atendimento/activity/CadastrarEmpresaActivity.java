package com.atendimento.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Empresa;
import com.atendimento.util.Preferencias;
import com.atendimento.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private ProgressBar progressBar;
    private StorageReference storageReference;
    private Bitmap      imagemEmpresa;
    private Bitmap      imagemEmpresaParametro;
    private Preferencias preferencias;
    private AlertDialog opcoes;
    private Dialog.OnClickListener onClickCameraListener;
    private Dialog.OnClickListener onClickGaleriaListener;
    private UploadTask uploadTask;
    private ImageView imageViewEditNomeEmpresa;
    private String idKey = "";
    private Empresa empresaParametro = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_empresa);
        Intent intent = getIntent();
        empresaParametro = (Empresa) intent.getSerializableExtra("objeto");
        if (empresaParametro != null){ idKey = empresaParametro.getId(); }
        util = new Util();
        preferencias = new Preferencias(getApplicationContext());
        identificadorUsuario = preferencias.getIdentificador();
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        nomeEmpresa = findViewById(R.id.editTextNomeEmpresa);
        nomeEmpresa.setEnabled(false);
        nomeEmpresa.getBackground().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        imageViewEditNomeEmpresa = findViewById(R.id.imageViewEditEmpresaNome);
        imageViewEditNomeEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomeEmpresa.setEnabled(true);
                imageViewEditNomeEmpresa.setVisibility(View.GONE);
            }
        });
        util = new Util();
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        circleImageView  = findViewById(R.id.circleImageEmpresa);
        listaCategoria = new ArrayList<String>();
        progressBar = findViewById(R.id.progressBarCadEmpresa);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.MULTIPLY);

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
                salvarEmpresa();
            }
        });

        cancel = findViewById(R.id.buttonEmpresaCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTelaFinish(getApplicationContext(),EmpresasActivity.class);
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarOpcoes();
            }
        });

        onClickCameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera,1);
            }
        };

        onClickGaleriaListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,0);
            }
        };

        carregarFotoEmpresa();
    }

    private void carregarFotoEmpresa(){
        if (!idKey.equals("")) {
            progressBar.setVisibility(View.VISIBLE);
            storageReference = ConfiguracaoFirebase.getStorage().child("empresas").child(identificadorUsuario).child(idKey);
            long dim = 1024 * 1024;
            storageReference.getBytes(dim).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    imagemEmpresa = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    circleImageView.setImageBitmap(imagemEmpresa);
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imagemEmpresaPadrao();
                    Toast.makeText(getApplicationContext(), "Erro ao Carregar Foto ", Toast.LENGTH_LONG).show();
                    Log.i("erroFotoCarregar", e.toString() + " " + e.getCause().toString());
                }
            });
        }
        else{
            imagemEmpresaPadrao();
        }
    }

    private void imagemEmpresaPadrao(){
        circleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_user));
        progressBar.setVisibility(View.GONE);
    }

    public void mostrarOpcoes(){
        AlertDialog.Builder builder  = util.CustomDialog("De onde deseja, tirar a foto da Empresa", CadastrarEmpresaActivity.this, "Câmera", onClickCameraListener, "Galeria", onClickGaleriaListener);
        opcoes = builder.create();
        opcoes.show();
    }

    private void salvarImagem() {
        if (imagemEmpresaParametro != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagemEmpresaParametro.compress(Bitmap.CompressFormat.PNG, 75, stream);
            byte[] byteArray = stream.toByteArray();

            storageReference = ConfiguracaoFirebase.getStorage().child("empresas").child(identificadorUsuario).child(idKey);
            uploadTask = storageReference.putBytes(byteArray);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "Falha ao enviar foto", Toast.LENGTH_LONG).show();
                    Log.i("erroFoto", exception.toString() + " " + exception.getCause().toString());
                }
            });
        }
    }

    private void salvarEmpresa(){
        if (!nomeEmpresa.getText().toString().equals("")) {
            if (idKey.equals("")){  idKey = firebase.child("empresas").child(identificadorUsuario).push().getKey();  }
            Empresa empresa = new Empresa();
            empresa.setId(idKey);
            empresa.setIdUsuario(identificadorUsuario);
            empresa.setNome(nomeEmpresa.getText().toString());
            empresa.setCategoria(spinnerCategoria.getSelectedItem().toString());
            firebase.child("empresas").child(identificadorUsuario).child(idKey).setValue(empresa);
            salvarImagem();
            mudarTelaFinish(getApplicationContext(), EmpresasActivity.class);
        }
        else{
            Toast.makeText(getApplicationContext(), "Preencha os dados", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null){
            progressBar.setVisibility(View.VISIBLE);
            Uri localImagemSelecionada = data.getData();
            try {
                imagemEmpresaParametro = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                imagemEmpresaParametro =  util.diminuirImagem(imagemEmpresaParametro, 200,200);

                circleImageView.setImageBitmap(imagemEmpresaParametro);
                progressBar.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
                circleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_user));
            }
        }
        else if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            try{
                Bundle extras = data.getExtras();
                imagemEmpresaParametro = (Bitmap) extras.get("data");
                circleImageView.setImageBitmap(imagemEmpresaParametro);
            }
            catch (Exception e){
                circleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_user));
            }
        }
    }
}
