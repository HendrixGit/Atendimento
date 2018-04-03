package com.atendimento.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.util.Preferencias;
import com.atendimento.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivity extends BaseActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private String identificadorUsuario;
    private DatabaseReference firebase;
    private FirebaseAuth autenticacao;
    private StorageReference storageReference;
    private EditText nome;
    private EditText email;
    private Bitmap      imagemPerfil;
    private Bitmap      imagemPerfilParametro;
    private CircleImageView circleImageView;
    private ImageView imageViewEditNome;
    private ImageView imageViewEditEmail;
    private AlertDialog opcoes;
    private Util util;
    private UploadTask uploadTask;
    private ProgressBar progressBar;
    private Preferencias preferencias;
    private Button botaoCancelar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        circleImageView = findViewById(R.id.circleImagePerfil);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        botaoCancelar = findViewById(R.id.buttonCancelarConta);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.MULTIPLY);
        util = new Util();
        nome  = findViewById(R.id.editNomeConf);
        email = findViewById(R.id.editEmailConf);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        imageViewEditNome  = findViewById(R.id.imageViewEditNome);
        imageViewEditNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTela(getApplicationContext(),EditPreferenciasActivity.class);
            }
        });
        imageViewEditEmail = findViewById(R.id.imageViewEditEmail);
        imageViewEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTela(getApplicationContext(),EditEmailActivity.class);
            }
        });

        preferencias = new Preferencias(getApplicationContext());
        identificadorUsuario = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebaseDatabase();

        carregarFoto();
        nome.setText(preferencias.getNome());
        email.setText(preferencias.getEmail());
        nome.setEnabled(false);
        email.setEnabled(false);

        if (verificarProviderLogin() == true){
            circleImageView.setEnabled(false);
            imageViewEditEmail.setVisibility(View.GONE);
            imageViewEditNome.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Login Feito pelo facebook", Toast.LENGTH_LONG).show();
        }
        else{
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracoesActivity.this);
                    builder.setTitle("Trocar e-mail");
                    builder.setMessage("Favor insira o novo e-mail abaixo");
                    final EditText editText = new EditText(ConfiguracoesActivity.this);
                    builder.setView(editText);
                    builder.setPositiveButton("Redefinir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (editText.getText().toString().equals("")){
                                Toast.makeText(getApplicationContext(),"Favor Insira o e-mail",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    opcoes = builder.create();
                    opcoes.show();
                }
            });
        }

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarOpcoes();
            }
        });

        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarProviderLogin() == true){

                }
                else{

                }
            }
        });

    }

    public void mostrarOpcoes(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog);
        builder.setTitle("De onde deseja, tirar a foto de Perfil");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera,1);
                }catch (Exception e){
                    Log.i("erroCamera",e.getCause().toString());
                }

            }
        });
        builder.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,0);
            }
        });
        opcoes = builder.create();
        opcoes.show();
    }

    private void carregarFoto(){
        progressBar.setVisibility(View.VISIBLE);
        storageReference = ConfiguracaoFirebase.getStorage().child(identificadorUsuario);
            long dim = 1024 * 1024;
            storageReference.getBytes(dim).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    imagemPerfil = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    circleImageView.setImageBitmap(imagemPerfil);
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    circleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_user));
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Erro ao Carregar Foto ",Toast.LENGTH_LONG).show();
                    Log.i("erroFotoCarregar", e.toString() + " " + e.getCause().toString());
                }
            });
    }

    private boolean verificarProviderLogin() {
        boolean result = false;
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        for (UserInfo userInfo : autenticacao.getCurrentUser().getProviderData()) {
            if (userInfo.getProviderId().equals("facebook.com")) {
                result = true;
            }
        }
        return result;
    }

    private void salvarImagem() {
        if (imagemPerfilParametro != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagemPerfilParametro.compress(Bitmap.CompressFormat.PNG, 75, stream);
            byte[] byteArray = stream.toByteArray();

            storageReference = ConfiguracaoFirebase.getStorage().child(identificadorUsuario);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null){
            progressBar.setVisibility(View.VISIBLE);
            Uri localImagemSelecionada = data.getData();

            try {
                imagemPerfilParametro = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                imagemPerfilParametro =  util.diminuirImagem(imagemPerfilParametro, 200,200);

                circleImageView.setImageBitmap(imagemPerfilParametro);
                progressBar.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
                circleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_user));
            }
        }
        else if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            try{
                Bundle extras = data.getExtras();
                imagemPerfilParametro = (Bitmap) extras.get("data");
                circleImageView.setImageBitmap(imagemPerfilParametro);
            }
            catch (Exception e){
                circleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_user));
            }
        }
        salvarImagem();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: mudarTelaFinish(getApplicationContext(), MainActivity.class); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

}
