package com.atendimento.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
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
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    private String idKey     = "";
    private String urlImagem = "";
    private Empresa empresaParametro = null;
    private Task taskSalvarEmpresa;
    private RunnableFuture runnableFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_empresa);
        nomeEmpresa      = findViewById(R.id.editTextNomeEmpresa);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        circleImageView  = findViewById(R.id.circleImageEmpresa);
        progressBar      = findViewById(R.id.progressBarCadEmpresa);
        toolbar          = findViewById(R.id.toolbar);
        preferencias = new Preferencias(getApplicationContext());
        identificadorUsuario = preferencias.getIdentificador();
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
        Intent intent = getIntent();
        empresaParametro = (Empresa) intent.getSerializableExtra("empresa");
        if (empresaParametro != null){ carregarDados(); }
        else { idKey = firebase.child("empresas").child(identificadorUsuario).push().getKey(); }
        util = new Util();
        nomeEmpresa.setEnabled(false);
        imm.hideSoftInputFromWindow(nomeEmpresa.getWindowToken(),0);
        nomeEmpresa.getBackground().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        imageViewEditNomeEmpresa = findViewById(R.id.imageViewEditEmpresaNome);
        imageViewEditNomeEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewEditNomeEmpresa.setVisibility(View.GONE);
                nomeEmpresa.setEnabled(true);
                nomeEmpresa.requestFocus();
                imm.showSoftInput(nomeEmpresa, 0);
            }
        });
        util = new Util();
        listaCategoria = new ArrayList<String>();
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
                mudarTelaFinish(getApplicationContext(), EmpresasActivity.class);
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

    private Task salvarImagem() {
        if (imagemEmpresaParametro != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagemEmpresaParametro.compress(Bitmap.CompressFormat.PNG, 75, stream);
            byte[] byteArray = stream.toByteArray();

            storageReference = ConfiguracaoFirebase.getStorage().child("empresas").child(identificadorUsuario).child(idKey);
            uploadTask = storageReference.putBytes(byteArray);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    urlImagem = taskSnapshot.getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "Falha ao enviar foto", Toast.LENGTH_LONG).show();
                    Log.i("erroFoto", exception.toString() + " " + exception.getCause().toString());
                }
            });
        }
        return uploadTask;
    }

    private void carregarDados(){
        idKey     = empresaParametro.getId();
        urlImagem = empresaParametro.getUrlImagem();
        nomeEmpresa.setText(empresaParametro.getNome());
    }


    private void salvarEmpresa(){
        if (!nomeEmpresa.getText().toString().equals("") && imagemEmpresaParametro != null) {
            runnableFuture = new RunnableFuture() {
                @Override
                public void run() {
                    if (imagemEmpresaParametro != null) {
                        salvarImagem();
                        Tasks.whenAll(uploadTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                isDone();
                            }
                        });
                    }
                    else{ isDone(); }
                }

                @Override
                public boolean cancel(boolean b) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    Empresa empresa = new Empresa();
                    empresa.setId(idKey);
                    empresa.setIdUsuario(identificadorUsuario);
                    empresa.setNome(nomeEmpresa.getText().toString());
                    empresa.setCategoria(spinnerCategoria.getSelectedItem().toString());
                    empresa.setUrlImagem(urlImagem);
                    taskSalvarEmpresa = firebase.child("empresas").child(identificadorUsuario).child(idKey).setValue(empresa);
                    Tasks.whenAll(taskSalvarEmpresa).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mudarTelaFinish(getApplicationContext(), EmpresasActivity.class);
                        }
                    });
                    return true;
                }

                @Override
                public Object get() throws InterruptedException, ExecutionException {
                    return null;
                }

                @Override
                public Object get(long l, @NonNull TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                    return null;
                }
            };
            runnableFuture.run();
        }
        else{
            Toast.makeText(getApplicationContext(), "Preencha os dados, e coloque a foto", Toast.LENGTH_LONG).show();
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
