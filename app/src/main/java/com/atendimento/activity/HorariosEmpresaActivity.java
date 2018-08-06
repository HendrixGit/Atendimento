package com.atendimento.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.util.Util;

public class HorariosEmpresaActivity extends BaseActivity {

    private Button horaInicio;
    private Button horaFinal;
    private DialogFragment horariosDialog;
    private LinearLayout segunda;
    private TextView inicioSegunda;
    private TextView fimSegunda;
    private CheckBox terca;
    private CheckBox quarta;
    private CheckBox quinta;
    private CheckBox sexta;
    private CheckBox sabado;
    private CheckBox domingo;
    private Util util;
    private String parametroHoraInicial;
    private String parametroHoraFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios_empresa);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Cadastrar Horários");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));

        inicioSegunda = findViewById(R.id.textViewInicioSegunda);
        fimSegunda    = findViewById(R.id.textViewFimSegunda);
        segunda = findViewById(R.id.layoutSegunda);
        segunda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTelaParametroFlag(getApplicationContext(), HorariosDiasActivity.class, inicioSegunda.getText().toString(), fimSegunda.getText().toString(), 0);
            }
        });

        parametroHoraInicial = getIntent().getExtras().getString("hora");
        if (parametroHoraInicial != null) {
            if (!parametroHoraInicial.isEmpty()) {
                inicioSegunda.setText(getIntent().getExtras().getString("hora"));
            }
        }

        parametroHoraFinal = getIntent().getExtras().getString("hora2");
        if (parametroHoraFinal != null) {
            if (!parametroHoraFinal.isEmpty()) {
                fimSegunda.setText(getIntent().getExtras().getString("hora2"));
            }
        }

    }
}
