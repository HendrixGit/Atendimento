package com.atendimento.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.fragment.HorarioDialog;
import com.atendimento.util.Util;


public class HorariosEmpresaActivity extends BaseActivity {

    private EditText horaInicio;
    private DialogFragment horariosDialog;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios_empresa);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Cadastrar Horários");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        setSupportActionBar(toolbarBase);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        util = new Util();

        horaInicio = findViewById(R.id.editTextHoraInicio);
        horaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horariosDialog = new HorarioDialog();
                horariosDialog.show(getFragmentManager(), "Horários");
            }
        });
    }
}
