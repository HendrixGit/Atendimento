package com.atendimento.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.fragment.HorarioDialog;
import com.atendimento.model.Horario;
import com.atendimento.util.MyDialogFragmentListener;
import com.atendimento.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private String horaFimParametro    = "";
    private Spinner spinnerDuracao;
    private ArrayAdapter<String> dataDuracao;
    private List<String> listaDuracao;
    private Horario horarioParametro;
    private Boolean opCheckBox = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios_dias);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Definir Horários");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));

        checkBoxDias = findViewById(R.id.checkedTextViewDia);
        checkBoxDias.setChecked(true);
        checkBoxDias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opCheckBox){
                    opCheckBox = false;
                }
                else{
                    opCheckBox = true;
                }
            }
        });

        inicio = findViewById(R.id.textViewHorarioInicial);
        fim    = findViewById(R.id.textViewHorarioFinal);
        spinnerDuracao = findViewById(R.id.spinnerDuracao);
        sqLiteDatabasePar = databaseCategorias();

        listaDuracao = new ArrayList<String>();
        final Cursor cursor = cursorDuracao(sqLiteDatabasePar,"");
        final int indiceColunaDescricao = cursor.getColumnIndex("descricao");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            listaDuracao.add(cursor.getString(indiceColunaDescricao));
            cursor.moveToNext();
        }

        dataDuracao = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listaDuracao);
        spinnerDuracao.setAdapter(dataDuracao);

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
                Cursor cursor2 = cursorDuracao(sqLiteDatabasePar, spinnerDuracao.getSelectedItem().toString());
                Integer indiceHorario = cursor2.getColumnIndex("duracaoHorario");
                cursor2.moveToFirst();


                Horario horario = new Horario();
                horario.setIdEmpresa("");
                horario.setDescricaoDia("");
                horario.setDiaAtivo(opCheckBox);
                horario.setDiaSemana(1);
                horario.setDuracao(cursor2.getInt(indiceHorario));
                horario.setHoraInicio(inicio.getText().toString());
                horario.setHoraFinal(fim.getText().toString());
                voltar(horario);
            }
        });

        cancel = findViewById(R.id.buttonCancelHorario);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltar(horarioParametro);
            }
        });

        Intent intent = getIntent();
        horarioParametro = (Horario) intent.getSerializableExtra("horario");
        if (horarioParametro != null){
            inicio.setText(horarioParametro.getHoraInicio());
            fim.setText(horarioParametro.getHoraFinal());
            checkBoxDias.setChecked(horarioParametro.getDiaAtivo());
            checkBoxDias.setText(horarioParametro.getDescricaoDia());
            opCheckBox = horarioParametro.getDiaAtivo();
        }
    }

    private void voltar(Serializable serializable){
        Intent intent = new Intent(getApplicationContext(), HorariosEmpresaActivity.class);
        intent.putExtra("horario", serializable);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
