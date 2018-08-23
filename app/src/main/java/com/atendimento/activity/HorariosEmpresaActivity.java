package com.atendimento.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.model.Horario;

import java.util.ArrayList;
import java.util.List;

public class HorariosEmpresaActivity extends BaseActivity {

    private List<Horario> empresaHorarios;
    private Horario horarioSegunda;
    private Horario horarioTerca;
    private ArrayAdapter<String> dataHorarios;
    private Spinner spinnerHorarios;
    private List<String> listaHorarios;
    private TextView inicio;
    private TextView fim;
    private Button ok;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios_empresa);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Cadastrar Horários");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));

        inicio = findViewById(R.id.textViewInicioHorarios);
        fim    = findViewById(R.id.textViewFinalHorarios);

        ok     = findViewById(R.id.buttonOKHorarios);
        cancel = findViewById(R.id.buttonCancelarHorarios);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        empresaHorarios = new ArrayList<>();
        horarioSegunda  = new Horario();
        horarioSegunda.setDescricaoDia(getResources().getString(R.string.segunda));
        horarioSegunda.setDiaAtivo(true);
        horarioSegunda.setHoraInicio(getResources().getString(R.string.horarioPadraoInicial));
        horarioSegunda.setHoraFinal(getResources().getString(R.string.horarioPadraoFinal));
        horarioSegunda.setDiaSemana(1);
        empresaHorarios.add(horarioSegunda);

        horarioTerca = new Horario();
        horarioTerca.setDescricaoDia(getResources().getString(R.string.terca));
        horarioTerca.setDiaAtivo(true);
        horarioTerca.setHoraInicio(getResources().getString(R.string.horarioPadraoInicial));
        horarioTerca.setHoraFinal(getResources().getString(R.string.horarioPadraoFinal));
        horarioTerca.setDiaSemana(2);
        empresaHorarios.add(horarioTerca);

        listaHorarios = new ArrayList<>();
        listaHorarios.add(horarioSegunda.getDescricaoDia() + " - " + horarioSegunda.getHoraInicio() + " - " + horarioSegunda.getHoraFinal());
        listaHorarios.add(horarioTerca.getDescricaoDia()   + " - " + horarioTerca.getHoraInicio() + " - " + horarioTerca.getHoraFinal());
        dataHorarios = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listaHorarios);
        spinnerHorarios = findViewById(R.id.spinnerHorarios);
        spinnerHorarios.setAdapter(dataHorarios);

        spinnerHorarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void telaHorarios(Integer dia, String diaDescricao, String inicio, String fim, Boolean diaAtivo, Horario horarioParametro){
        horarioParametro = new Horario();
        horarioParametro.setDiaSemana(dia);
        horarioParametro.setHoraInicio(inicio);
        horarioParametro.setHoraFinal(fim);
        horarioParametro.setDiaAtivo(diaAtivo);
        horarioParametro.setDescricaoDia(diaDescricao);
    }
}
