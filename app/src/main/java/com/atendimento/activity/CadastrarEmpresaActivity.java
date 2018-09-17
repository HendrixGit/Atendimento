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
import com.atendimento.model.Horario;
import com.atendimento.util.Preferencias;
import com.atendimento.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
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
    private Bitmap imagemEmpresaParametro;
    private Preferencias preferencias;
    private AlertDialog opcoes;
    private Dialog.OnClickListener onClickCameraListener;
    private Dialog.OnClickListener onClickGaleriaListener;
    private UploadTask uploadTask;
    private ImageView imageViewEditNomeEmpresa;
    private String idKey = "";
    private String urlImagem = "";
    private Empresa empresaParametro = null;
    private Task taskSalvarEmpresa;
    private Task taskSalvarEmpresa2;
    private RunnableFuture runnableFuture;
    private ArrayList<Horario> empresaHorarios;
    private ImageView checkHorarios;
    private Button buttonHorarios;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_empresa);

        buttonHorarios = findViewById(R.id.buttonHorarios);
        buttonHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HorariosEmpresaActivity.class);
                Empresa empresa = getDadosEmpresa();
                intent.putExtra("empresa", empresa);
                if (empresaHorarios != null) {
                    intent.putParcelableArrayListExtra("horarios", empresaHorarios);
                }
                startActivity(intent);
            }
        });

        nomeEmpresa = findViewById(R.id.editTextNomeEmpresa);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        circleImageView = findViewById(R.id.circleImageEmpresa);
        progressBar = findViewById(R.id.progressBarCadEmpresa);
        toolbar = findViewById(R.id.toolbar);
        preferencias = new Preferencias(getApplicationContext());
        identificadorUsuario = preferencias.getIdentificador();
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        checkHorarios = findViewById(R.id.imageViewCheckHorarios);
        intent = getIntent();
        empresaParametro = (Empresa) intent.getSerializableExtra("empresa");
        if (empresaParametro != null) {
            carregarDados();
        }
        else {
            idKey = firebase.push().getKey();
            carregarFoto();
        }

        util = new Util();
        nomeEmpresa.setEnabled(false);
        imm.hideSoftInputFromWindow(nomeEmpresa.getWindowToken(), 0);
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
        listaCategoria = new ArrayList<String>();
        progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.MULTIPLY);

        sqLiteDatabasePar = databaseCategorias();

        Cursor cursor = cursorCategorias(sqLiteDatabasePar, "");
        int indiceColunaDescricao = cursor.getColumnIndex("descricao");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            listaCategoria.add(cursor.getString(indiceColunaDescricao));
            cursor.moveToNext();
        }

        dataCategoria = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listaCategoria);
        spinnerCategoria.setAdapter(dataCategoria);
        if (empresaParametro != null) {
            Cursor cursor2 = cursorCategorias(sqLiteDatabasePar, empresaParametro.getCategoria());
            int indiceColunaCodigo = cursor.getColumnIndex("codigo");
            cursor2.moveToFirst();
            int opcao = Integer.parseInt(cursor2.getString(indiceColunaCodigo));
            spinnerCategoria.setSelection(opcao - 1);
        }

        toolbar.setTitle("Cadastro de Empresa");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        circleImageView = findViewById(R.id.circleImageEmpresa);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ok = findViewById(R.id.buttonEmpresaOK);
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
                startActivityForResult(camera, 1);
            }
        };

        onClickGaleriaListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        };
    }

    private void imagemlistaHorariosPreenchida() {
        checkHorarios.setImageDrawable(getResources().getDrawable(R.drawable.checkmarkblue));
        checkHorarios.setVisibility(View.VISIBLE);
    }

    private void carregarFoto() {
        progressBar.setVisibility(View.VISIBLE);
        if (imagemEmpresaParametro != null) {
            circleImageView.setImageBitmap(imagemEmpresaParametro);
            progressBar.setVisibility(View.GONE);
        } else {
            storageReference = ConfiguracaoFirebase.getStorage().child("empresas").child(identificadorUsuario).child(idKey);
            long dim = 1024 * 1024;
            storageReference.getBytes(dim).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    imagemEmpresaParametro = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    circleImageView.setImageBitmap(imagemEmpresaParametro);
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_action_user).into(circleImageView);
                    Log.i("erroFotoCarregar", e.toString() + " " + e.getCause().toString());
                }
            });
        }
    }

    public void mostrarOpcoes() {
        AlertDialog.Builder builder = util.CustomDialog("De onde deseja, tirar a foto da Empresa", CadastrarEmpresaActivity.this, "Câmera", onClickCameraListener, "Galeria", onClickGaleriaListener);
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

    private void carregarDados() {
        idKey = empresaParametro.getId();
        urlImagem = empresaParametro.getUrlImagem();
        nomeEmpresa.setText(empresaParametro.getNome());
        if (empresaParametro.getImageArray() != null) {
            imagemEmpresaParametro = BitmapFactory.decodeByteArray(empresaParametro.getImageArray(), 0, empresaParametro.getImageArray().length);
        }
        carregarFoto();

        if (intent.getParcelableArrayListExtra("horarios") != null) {
            empresaHorarios = intent.getParcelableArrayListExtra("horarios");
            imagemlistaHorariosPreenchida();
        }
        else{
            empresaHorarios = new ArrayList<>();
            firebase.child("horarios").child(idKey).orderByChild("diaSemana").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Horario horario = dataSnapshot.getValue(Horario.class);
                    empresaHorarios.add(horario);
                    imagemlistaHorariosPreenchida();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private Empresa getDadosEmpresa(){
        Empresa empresaAtual = new Empresa();
        empresaAtual.setNome(nomeEmpresa.getText().toString());
        empresaAtual.setCategoria(spinnerCategoria.getSelectedItem().toString());
        empresaAtual.setId(idKey);
        empresaAtual.setIdUsuario(identificadorUsuario);
        empresaAtual.setUrlImagem(urlImagem);

        if (imagemEmpresaParametro != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagemEmpresaParametro.compress(Bitmap.CompressFormat.PNG, 100, stream);
            empresaAtual.setImageArray(stream.toByteArray());
        }
        return empresaAtual;
    }

    private void salvarEmpresa(){
        if ((!nomeEmpresa.getText().toString().equals("")) && (imagemEmpresaParametro != null)  && (empresaHorarios != null)) {
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

                    salvarHorarios();

                    taskSalvarEmpresa  = firebase.child("empresas").child(identificadorUsuario).child(idKey).setValue(empresa);
                    taskSalvarEmpresa2 = firebase.child("empresasApp").child(idKey).setValue(empresa);
                    Tasks.whenAll(taskSalvarEmpresa, taskSalvarEmpresa2).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            Toast.makeText(getApplicationContext(), "Preencha os dados, coloque a foto e defina os horários", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        empresaParametro = null;
    }

    private void salvarHorarios(){
        int i = 0;
        for (Horario horario : empresaHorarios){
            empresaHorarios.get(i).setIdEmpresa(idKey);
            empresaHorarios.get(i).setIdUsuariosEmpresa(identificadorUsuario);
            firebase.child("horarios").child(idKey).child(horario.getDescricaoDia()).setValue(empresaHorarios.get(i));
            i++;
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
