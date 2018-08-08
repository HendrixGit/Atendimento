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
                Intent intent = new Intent(getApplicationContext(), HorariosDiasActivity.class);
                intent.putExtra("horario",horarioParametro);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        horarioParametro = (Horario) intent.getSerializableExtra("horario");
        if (horarioParametro != null){
            if (horarioParametro.getDiaSemana() == 1){
                if (horarioParametro.getDiaAtivo()) {
                    segunda.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                else{
                    segunda.setBackgroundColor(getResources().getColor(R.color.colorDiaSelecionado));
                }
                inicioSegunda.setText(horarioParametro.getHoraInicio());
                fimSegunda.setText(horarioParametro.getHoraFinal());
            }
        }
    }
    private void telaHorarios(Intent dia, String inicio, String fim){
        Intent intent = new Intent(getApplicationContext(), HorariosDiasActivity.class);
        intent.putExtra("horario",horarioParametro);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
