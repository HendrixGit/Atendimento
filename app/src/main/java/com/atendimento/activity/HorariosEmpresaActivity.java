package com.atendimento.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.fragment.HorarioDialog;
import com.atendimento.util.MyDialogFragmentListener;
import com.atendimento.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HorariosEmpresaActivity extends BaseActivity implements MyDialogFragmentListener {

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
    private Boolean op;

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
        inicio = findViewById(R.id.textViewInicio);
        fim    = findViewById(R.id.textViewFinal);

        segunda = findViewById(R.id.checkBoxSegunda);
        terca   = findViewById(R.id.checkBoxTerca);
        quarta  = findViewById(R.id.checkBoxQuarta);
        quinta  = findViewById(R.id.checkBoxQuinta);
        sexta   = findViewById(R.id.checkBoxSexta);
        sabado  = findViewById(R.id.checkBoxSabado);
        domingo = findViewById(R.id.checkBoxDomingo);


        DateFormat dateFormat = new SimpleDateFormat("EEEE");
        Calendar calendar = Calendar.getInstance();


        segunda.setText("Segunda-Feira");


        horaInicio = findViewById(R.id.buttonHorarioInicio);
        horaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = false;
                setHorariosTextViews(inicio,getResources().getString(R.string.inicio));
            }
        });

        horaFinal = findViewById(R.id.buttonHorarioFinal);
        horaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = true;
                setHorariosTextViews(fim,getResources().getString(R.string.Fim));
            }
        });

    }

    private void setHorariosTextViews(TextView textViewParemetro, String textoComparacao){
        horariosDialog = new HorarioDialog();
        Bundle bundle;
        if (textViewParemetro.getText().equals(textoComparacao)){
            bundle = util.bundleStringGenerico("hora","");
        }
        else{
            bundle = util.bundleStringGenerico("hora",textViewParemetro.getText().toString());
        }
        horariosDialog.setArguments(bundle);
        horariosDialog.show(getFragmentManager(), "Horários");
    }

    @Override
    public void onReturnValue(String resultadoParametro) {
        if (!resultadoParametro.equals("")) {
            if (op == false) {
                inicio.setText(resultadoParametro);
            }
            else{
                fim.setText(resultadoParametro);
            }
        }
    }
}
