package com.atendimento.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.fragment.HorarioDialog;
import com.atendimento.util.MyDialogFragmentListener;
import com.atendimento.util.Util;

public class HorariosDiasActivity extends BaseActivity implements MyDialogFragmentListener {

    private DialogFragment horariosDialog;
    private CheckBox checkBoxDias;
    private TextView inicio;
    private TextView fim;
    private ImageView editInicio;
    private ImageView editFim;
    private Button ok;
    private Button cancel;
    private Util util;
    private Boolean op;
    private String horaInicioParametro = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios_dias);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Definir Horários");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));

        checkBoxDias = findViewById(R.id.checkedTextViewDia);
        checkBoxDias.setChecked(true);

        inicio = findViewById(R.id.textViewHorarioInicial);
        fim    = findViewById(R.id.textViewHorarioFinal);

        editInicio = findViewById(R.id.imageViewEditInicio);
        editInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = true;
                setHorariosTextViews(inicio, inicio.getText().toString());
            }
        });
        editFim    = findViewById(R.id.imageViewEditFim);
        editFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = false;
                setHorariosTextViews(fim, fim.getText().toString());
            }
        });

        ok = findViewById(R.id.buttonOKHorario);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTelaParametroFlag(getApplicationContext(),HorariosEmpresaActivity.class, inicio.getText().toString());
            }
        });

        cancel = findViewById(R.id.buttonCancelHorario);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTelaParametroFlag(getApplicationContext(),HorariosEmpresaActivity.class,horaInicioParametro);
            }
        });

        horaInicioParametro = getIntent().getExtras().getString("hora");
        if (horaInicioParametro != null) {
            if (!horaInicioParametro.isEmpty()) {
                inicio.setText(getIntent().getExtras().getString("hora"));
            }
        }
    }

    private void setHorariosTextViews(TextView textViewParemetro, String textoComparacao){
        horariosDialog = new HorarioDialog();
        util = new Util();
        Bundle bundle;
        bundle = util.bundleStringGenerico("hora",textViewParemetro.getText().toString());
        horariosDialog.setArguments(bundle);
        horariosDialog.show(getFragmentManager(), "Horários");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: mudarTela(getApplicationContext(), HorariosEmpresaActivity.class); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onReturnValue(String resultadoParametro) {
        if (!resultadoParametro.equals("")){
            if (op){
                inicio.setText(resultadoParametro);
            }
            else{
                fim.setText(resultadoParametro);
            }
        }
    }
}
