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
import com.atendimento.model.Horario;
import com.atendimento.util.Util;

public class HorariosEmpresaActivity extends BaseActivity {

    private Button ok;
    private Button cancelar;
    private DialogFragment horariosDialog;
    private LinearLayout segunda;
    private TextView inicioSegunda;
    private TextView fimSegunda;
    private Boolean  segundaAtivo = true;
    private TextView inicioTerca;
    private TextView fimTerca;
    private Boolean  tercaAtivo = true;
    private LinearLayout terca;
    private CheckBox quarta;
    private CheckBox quinta;
    private CheckBox sexta;
    private CheckBox sabado;
    private CheckBox domingo;
    private Util util;
    private String parametroHoraInicial;
    private String parametroHoraFinal;
    private Horario horarioParametro;

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
                telaHorarios(1, getResources().getString(R.string.segunda), inicioSegunda.getText().toString(), fimSegunda.getText().toString(), segundaAtivo);
            }
        });

        inicioTerca = findViewById(R.id.textViewInicioTerca);
        fimTerca    = findViewById(R.id.textViewFimTerca);
        terca = findViewById(R.id.layoutTerca);
        terca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                telaHorarios(2, getResources().getString(R.string.terca), inicioTerca.getText().toString(), fimTerca.getText().toString(), tercaAtivo);
            }
        });

        ok       = findViewById(R.id.buttonOKHorario);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancelar = findViewById(R.id.buttonCancelHorario);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        horarioParametro = (Horario) intent.getSerializableExtra("horario");
        if (horarioParametro != null){
            if (horarioParametro.getDiaSemana() == 1){
                inicioSegunda.setText(horarioParametro.getHoraInicio());
                fimSegunda.setText(horarioParametro.getHoraFinal());
                segundaAtivo = horarioParametro.getDiaAtivo();
                setLineayLayout(segunda, horarioParametro.getDiaAtivo());
            }
            else
            if (horarioParametro.getDiaSemana() == 2){
                inicioTerca.setText(horarioParametro.getHoraInicio());
                fimTerca.setText(horarioParametro.getHoraFinal());
                tercaAtivo = horarioParametro.getDiaAtivo();
                setLineayLayout(terca, horarioParametro.getDiaAtivo());
            }
        }
    }

    private void setLineayLayout(LinearLayout layout, Boolean booleanParametro){
        if (booleanParametro){
            layout.setBackgroundColor(getResources().getColor(R.color.colorDiaAtivo));
        }
        else{
            layout.setBackgroundColor(getResources().getColor(R.color.colorDiaSelecionado));
        }
    }

    private void telaHorarios(Integer dia, String diaDescricao,  String inicio, String fim, Boolean diaAtivo){
        horarioParametro = new Horario();
        horarioParametro.setDiaSemana(dia);
        horarioParametro.setHoraInicio(inicio);
        horarioParametro.setHoraFinal(fim);
        horarioParametro.setDiaAtivo(diaAtivo);
        horarioParametro.setDescricaoDia(diaDescricao);

        Intent intent = new Intent(getApplicationContext(), HorariosDiasActivity.class);
        intent.putExtra("horario",horarioParametro);
        intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        finish();
    }
}
