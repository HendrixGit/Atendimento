package com.atendimento.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import com.atendimento.fragment.SenhaDialog;
import com.atendimento.util.MyDialogFragmentListener;
import com.atendimento.util.Preferencias;
import com.atendimento.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivity extends BaseActivity implements MyDialogFragmentListener {

    private android.support.v7.widget.Toolbar toolbar;
    private String identificadorUsuario;
    private DatabaseReference firebase;
    private FirebaseUser firebaseUser;
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
    private DialogFragment dialogFragment;
    private AuthCredential credential;
    private Dialog.OnClickListener clickYesDialogCancelarConta;

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
        firebaseUser = ConfiguracaoFirebase.getAutenticacao().getCurrentUser();

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

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarOpcoes();
            }
        });

        clickYesDialogCancelarConta = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Worked",Toast.LENGTH_LONG).show();
                deletarUsuario();
            }
        };

        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = util.YesNoDialog("Tem certeza que deseja Excluir sua conta ?",
                        ConfiguracoesActivity.this,
                        clickYesDialogCancelarConta);
                opcoes = builder.create();
                opcoes.show();
            }
        });
    }

    private void deletarUsuario() {
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    deletaDadosUsuario();

                }
                else{
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthRecentLoginRequiredException recentLogin) {
                        erroExcecao = "Erro Login Recente";
                        dialogFragment = new SenhaDialog();
                        dialogFragment.show(getFragmentManager(), "senha");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        erroExcecao = "Erro ao excluiir conta";
                    }
                    Toast.makeText(getApplicationContext(),erroExcecao,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void deletaDadosUsuario() {
        try {
            firebase.child("usuarios").child(identificadorUsuario).removeValue();
        }
        finally {
            mudarTelaFinish(getApplicationContext(),InicioActivity.class);
        }
    }


    public void mostrarOpcoes(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog);
        builder.setTitle("De onde deseja, tirar a foto de Perfil");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera,1);
                }
                catch (Exception e){
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

    @Override
    public void onReturnValue(String resultadoParametro) {
        if (!resultadoParametro.equals("")){
            credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), resultadoParametro);
            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        deletarUsuario();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Senha inválida", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
