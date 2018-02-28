package com.atendimento.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ConfiguracoesActivity extends BaseActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private String identificadorUsuario;
    private DatabaseReference firebase;
    private FirebaseAuth autenticacao;
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
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    usuario = new Usuario();
                    usuario.setNome(dataSnapshot.child("usuarios").child(identificadorUsuario).child("nome").getValue().toString());
                    usuario.setEmail(dataSnapshot.child("usuarios").child(identificadorUsuario).child("email").getValue().toString());
                }
                nome.setText(usuario.getNome().toString());
                email.setText(usuario.getEmail().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebase.addValueEventListener(valueEventListenerPerfil);
        if (verificarProviderLogin() == true){
            nome.setEnabled(false);
            email.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Login Feito pelo facebook", Toast.LENGTH_LONG).show();
        }

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,0);
            }
        });
    }

    private void carregarFoto(){

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
        if (!nome.getText().toString().equals("")) {
            firebase.child("usuarios")
                    .child(identificadorUsuario)
                    .child("nome").setValue(nome.getText().toString());
            mudarTelaFinish(getApplicationContext(), MainActivity.class);
        }
        else{
            Toast.makeText(getApplicationContext(),"Favor Preencha o nome",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null){
            Uri localImagemSelecionada = data.getData();
            try {
                imagemPerfil = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagemPerfil.compress(Bitmap.CompressFormat.PNG,75,stream);
                imageViewPerfil.setImageBitmap(imagemPerfil);
                byte[] byteArray = stream.toByteArray();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl("gs://atendimento-23915.appspot.com").child(identificadorUsuario);

                UploadTask uploadTask = storageReference.putBytes(byteArray);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(),"Falhou",Toast.LENGTH_LONG).show();
                        Log.i("erroFoto",exception.toString() + " " + exception.getMessage() );
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });


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
