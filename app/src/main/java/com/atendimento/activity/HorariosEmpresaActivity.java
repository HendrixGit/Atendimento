package com.atendimento.activity;

import android.app.DialogFragment;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.method.DateTimeKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.fragment.HorarioDialog;
import com.atendimento.util.MyDialogFragmentListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.WeekPagerAdapter;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class HorariosEmpresaActivity extends BaseActivity implements MyDialogFragmentListener {

    private Button horaInicio;
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



        horaInicio = findViewById(R.id.buttonHorario);
        horaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horariosDialog = new HorarioDialog();
                Bundle bundle = new Bundle();
                bundle.putString("hora", "8");
                horariosDialog.setArguments(bundle);
                horariosDialog.show(getFragmentManager(), "Horários");
            }
        });
    }

    @Override
    public void onReturnValue(String resultadoParametro) {
            inicio.setText(resultadoParametro);
    }
}
