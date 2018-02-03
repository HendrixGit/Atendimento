package com.atendimento.bases;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.atendimento.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected void mudarTela(Context contexto, Class classe) {
        Intent intent = new Intent(contexto, classe);
        startActivity(intent);
    }

    protected void mudarTelaFinish(Context contexto, Class classe) {
        Intent intent = new Intent(contexto, classe);
        startActivity(intent);
        finish();
    }

}
