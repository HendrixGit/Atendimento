package com.atendimento.activity;

import android.app.DialogFragment;
import android.os.Bundle;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;

public class HorariosDiasActivity extends BaseActivity {

    private DialogFragment horariosDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios_dias);
    }
}
