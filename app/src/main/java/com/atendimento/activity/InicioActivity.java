package com.atendimento.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;

public class InicioActivity extends BaseActivity {

    private ImageView botaoFacebook;
    private ImageView botaoEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        botaoFacebook = findViewById(R.id.imageFacebook);
        botaoEmail    = findViewById(R.id.imageEmail);

        botaoFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        botaoEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTela(getApplicationContext(), LoginActivity.class);
            }
        });

    }


}
