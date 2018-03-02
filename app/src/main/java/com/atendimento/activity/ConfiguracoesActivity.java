package com.atendimento.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.adapter.PerfilAdapter;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Usuario;
import com.atendimento.util.Preferencias;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConfiguracoesActivity extends BaseActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private String identificadorUsuario;
    private DatabaseReference firebase;
    private FirebaseAuth autenticacao;
    private StorageReference storageReference;
    private ListView listView;
    private ArrayList<Usuario> usuarios;
    private ArrayAdapter<Usuario> adapter;
    private ValueEventListener valueEventListenerPerfil;
    private EditText nome;
    private EditText email;
    private Usuario usuario;
    private ImageButton foto;
    private ImageView   imageViewPerfil;
    private Bitmap      imagemPerfil;
    private Bitmap      imagemPerfilParametro;
    private AlertDialog opcoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        foto = findViewById(R.id.imageButton);
        imageViewPerfil = findViewById(R.id.imagePerfil);

        nome  = findViewById(R.id.editNomeConf);
        email = findViewById(R.id.editEmailConf);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Preferencias preferencias = new Preferencias(getApplicationContext());
        identificadorUsuario = preferencias.getIdentificador();

        usuarios = new ArrayList<>();
        adapter  = new PerfilAdapter(ConfiguracoesActivity.this,usuarios);
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child(identificadorUsuario);

        valueEventListenerPerfil = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = new Usuario();
                usuario.setNome(dataSnapshot.child("usuarios").child(identificadorUsuario).child("nome").getValue().toString());
                usuario.setEmail(dataSnapshot.child("usuarios").child(identificadorUsuario).child("email").getValue().toString());
                nome.setText(usuario.getNome().toString());
                email.setText(usuario.getEmail().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        carregarFoto();
        firebase.addValueEventListener(valueEventListenerPerfil);

        if (verificarProviderLogin() == true){
            nome.setEnabled(false);
            email.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Login Feito pelo facebook", Toast.LENGTH_LONG).show();
        }

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarOpcoes();
            }
        });
    }

    public void mostrarOpcoes(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("De onde deseja, tirar a foto de Perfil");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(ConfiguracoesActivity.this, "camera" + arg1, Toast.LENGTH_SHORT).show();
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

    public Bitmap diminuirImagem(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // RECREATE THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    private void carregarFoto(){
        storageReference = ConfiguracaoFirebase.getStorage().child(identificadorUsuario);
        if (storageReference != null) {
            long dim = 1024 * 1024;
            storageReference.getBytes(dim).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    imagemPerfil = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageViewPerfil.setImageBitmap(Bitmap.createScaledBitmap(imagemPerfil,
                            imageViewPerfil.getWidth(),
                            imageViewPerfil.getHeight(),
                            false));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Erro ao Carregar Foto ",Toast.LENGTH_LONG).show();
                    Log.i("erroFotoCarregar", e.toString() + " " + e.getCause().toString());
                }
            });
        }
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

    private void salvarDados(){
        salvarNome();
        salvarImagem();
        mudarTelaFinish(getApplicationContext(), MainActivity.class);
    }

    private void salvarNome() {
        if (!nome.getText().toString().equals("")) {
            firebase.child("usuarios")
                    .child(identificadorUsuario)
                    .child("nome").setValue(nome.getText().toString());
        }
        else{
            Toast.makeText(getApplicationContext(),"Favor Preencha o nome",Toast.LENGTH_LONG).show();
        }
    }

    private void salvarImagem(){
        if (imagemPerfilParametro != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagemPerfilParametro.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byteArray = stream.toByteArray();

            storageReference = ConfiguracaoFirebase.getStorage().child(identificadorUsuario);
            UploadTask uploadTask = storageReference.putBytes(byteArray);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "Falha ao enviar foto", Toast.LENGTH_LONG).show();
                    Log.i("erroFoto", exception.toString() + " " + exception.getCause().toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null){
            Uri localImagemSelecionada = data.getData();
            try {
                imagemPerfilParametro = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                imagemPerfilParametro = diminuirImagem(imagemPerfilParametro, 300,300);
                imageViewPerfil.setImageBitmap(imagemPerfilParametro);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeListener();
    }

    private void removeListener() {
        firebase.removeEventListener(valueEventListenerPerfil);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: salvarDados();return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

}
