package com.atendimento.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.atendimento.R;
import com.atendimento.adapter.AdapterHorarios;
import com.atendimento.bases.BaseActivity;
import com.atendimento.model.Horario;
import com.atendimento.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

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

    private RecyclerView recyclerViewHorarios;
    private AdapterHorarios adapter;
    private List<Horario> empresaHorarios;
    private Horario horarioSegunda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios_empresa);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Cadastrar Horários");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));

        horarioSegunda = new Horario();
        horarioSegunda.setDescricaoDia("Segunda");
        horarioSegunda.setDiaAtivo(true);
        horarioSegunda.setHoraInicio("08:00");
        horarioSegunda.setHoraFinal("18:00");
        horarioSegunda.setDiaSemana(1);
        horarioSegunda.setDuracao(15);
        empresaHorarios = new ArrayList<>();
        empresaHorarios.add(horarioSegunda);


        adapter = new AdapterHorarios(empresaHorarios, getApplicationContext());
        recyclerViewHorarios = findViewById(R.id.recyclerViewHorarios);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewHorarios.setLayoutManager(layoutManager);
        recyclerViewHorarios.setHasFixedSize(true);
        recyclerViewHorarios.setAdapter(adapter);
        recyclerViewHorarios.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerViewHorarios,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                ));
        //telaHorarios(1, getResources().getString(R.string.segunda), "08:00","18:00", true, horarioSegunda);
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
