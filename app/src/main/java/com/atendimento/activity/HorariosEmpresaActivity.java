package com.atendimento.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.util.Util;

public class HorariosEmpresaActivity extends BaseActivity {

    private Button horaInicio;
    private Button horaFinal;
    private DialogFragment horariosDialog;
    private TextView inicio;
    private TextView fim;
    private CheckBox segunda;
    private CheckBox terca;
    private CheckBox quarta;
    private CheckBox quinta;
    private CheckBox sexta;
    private CheckBox sabado;
    private CheckBox domingo;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios_empresa);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Cadastrar Horários");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));

        segunda = findViewById(R.id.checkBoxSegunda);
        terca   = findViewById(R.id.checkBoxTerca);
        quarta  = findViewById(R.id.checkBoxQuarta);
        quinta  = findViewById(R.id.checkBoxQuinta);
        sexta   = findViewById(R.id.checkBoxSexta);
        sabado  = findViewById(R.id.checkBoxSabado);
        domingo = findViewById(R.id.checkBoxDomingo);

        segunda.setText(R.string.segunda);
        segunda.setChecked(true);
        terca.setText(R.string.terca);
        terca.setChecked(true);
        quarta.setText(R.string.quarta);
        quarta.setChecked(true);
        quinta.setText(R.string.quinta);
        quinta.setChecked(true);
        sexta.setText(R.string.sexta);
        sexta.setChecked(true);
        sabado.setText(R.string.sabado);
        sabado.setChecked(true);
        domingo.setText(R.string.domingo);
    }
}
