package com.atendimento.activity;

import android.app.DialogFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.fragment.HorarioDialog;
import com.atendimento.model.Horario;
import com.atendimento.util.MyDialogFragmentListener;

import java.util.ArrayList;
import java.util.List;

public class HorariosEmpresaActivity extends BaseActivity implements MyDialogFragmentListener {

    private List<Horario> empresaHorarios;
    private Horario horarioSegunda;
    private Horario horarioTerca;
    private ArrayAdapter<String> dataHorarios;
    private ArrayAdapter<String> dataDuracao;
    private Spinner spinnerHorarios;
    private List<String> listaHorarios;
    private List<String> listaDuracao;
    private TextView inicio;
    private TextView fim;
    private Button ok;
    private Button confirmar;
    private Button cancel;
    private CheckBox checkBoxDiaAtivo;
    private ConstraintLayout constraintLayoutHorarios;
    private DialogFragment fragmentHorarios;
    private int op;
    private Spinner spinnerDuracao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios_empresa);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Cadastrar Horários");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        constraintLayoutHorarios = findViewById(R.id.layoutHorarios);

        inicio = findViewById(R.id.textViewInicioHorarios);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = 0;
                abrirHorarios();
            }
        });
        fim    = findViewById(R.id.textViewFinalHorarios);
        fim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = 1;
                abrirHorarios();
            }
        });
        checkBoxDiaAtivo = findViewById(R.id.checkedTextViewDiaAtivo);
        checkBoxDiaAtivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxDiaAtivo.isChecked()){
                    constraintLayoutHorarios.setBackgroundColor(getResources().getColor(R.color.colorSelecao));
                }
                else{
                    constraintLayoutHorarios.setBackgroundColor(getResources().getColor(R.color.colorDiaSelecionado));
                }
            }
        });

        sqLiteDatabasePar = databaseCategorias();
        Cursor cursor = cursorDuracao(sqLiteDatabasePar,"");
        int indiceColunaDescricao = cursor.getColumnIndex("descricao");
        cursor.moveToFirst();
        listaDuracao = new ArrayList<>();

        while (!cursor.isAfterLast()){
            listaDuracao.add(cursor.getString(indiceColunaDescricao));
            cursor.moveToNext();
        }

        dataDuracao = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listaDuracao);
        spinnerDuracao = findViewById(R.id.spinnerDuracao);
        spinnerDuracao.setAdapter(dataDuracao);

        confirmar = findViewById(R.id.buttonOKConfirmarHorarios);
        confirmar.setOnClickListener(new View.OnClickListener() {
            int pos;
            Boolean diaAtivo;
            @Override
            public void onClick(View view) {
                if (spinnerHorarios.getSelectedItemPosition() == 0){
                    empresaHorarios.get(0).setHoraInicio(inicio.getText().toString());
                    empresaHorarios.get(0).setHoraFinal(fim.getText().toString());
                    empresaHorarios.get(0).setDiaAtivo(retornoDiaAtivo());
                    setCheckbox(empresaHorarios.get(0));
                    diaAtivo = empresaHorarios.get(0).getDiaAtivo();
                    pos = spinnerHorarios.getSelectedItemPosition();
                }
                else
                if(spinnerHorarios.getSelectedItemPosition() == 1){
                    empresaHorarios.get(1).setHoraInicio(inicio.getText().toString());
                    empresaHorarios.get(1).setHoraFinal(fim.getText().toString());
                    empresaHorarios.get(1).setDiaAtivo(retornoDiaAtivo());
                    setCheckbox(empresaHorarios.get(1));
                    diaAtivo = empresaHorarios.get(1).getDiaAtivo();
                    pos = spinnerHorarios.getSelectedItemPosition();
                }
                preencheSpinner();
                setSpinnerValues(pos, diaAtivo);
                spinnerHorarios.setSelection(pos);
            }
        });

        ok     = findViewById(R.id.buttonOKHorarios);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
        horarioTerca.setHoraFinal(getResources().getString(R.string.horarioPadraoMeioDia));
        horarioTerca.setDiaSemana(2);
        empresaHorarios.add(horarioTerca);

        listaHorarios = new ArrayList<>();
        spinnerHorarios = findViewById(R.id.spinnerHorarios);
        preencheSpinner();
        spinnerHorarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    setCampos(empresaHorarios.get(0));
                }
                else{
                    setCampos(empresaHorarios.get(1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setCheckbox(Horario horarioCheckBox){
        if (horarioCheckBox.getDiaAtivo()){
            constraintLayoutHorarios.setBackgroundColor(getResources().getColor(R.color.colorSelecao));
        }
        else{
            constraintLayoutHorarios.setBackgroundColor(getResources().getColor(R.color.colorDiaSelecionado));
        }
        checkBoxDiaAtivo.setChecked(horarioCheckBox.getDiaAtivo());
        setSpinnerValues(-1,horarioCheckBox.getDiaAtivo());
    }

    private Boolean retornoDiaAtivo(){
        if (checkBoxDiaAtivo.isChecked()){
            return true;
        }
        else{
            return false;
        }

    }

    private void setCampos(Horario horarioParametro){
        inicio.setText(horarioParametro.getHoraInicio());
        fim.setText(horarioParametro.getHoraFinal());
        setCheckbox(horarioParametro);
    }

    private void preencheSpinner(){
        listaHorarios.clear();
        for (Horario horario : empresaHorarios){
            listaHorarios.add(horario.getDescricaoDia() + " - " + horario.getHoraInicio() + " - " + horario.getHoraFinal());
        }
        dataHorarios = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listaHorarios);
        spinnerHorarios.setAdapter(dataHorarios);
    }

    private void telaHorarios(Integer dia, String diaDescricao, String inicio, String fim, Boolean diaAtivo, Horario horarioParametro){
        horarioParametro = new Horario();
        horarioParametro.setDiaSemana(dia);
        horarioParametro.setHoraInicio(inicio);
        horarioParametro.setHoraFinal(fim);
        horarioParametro.setDiaAtivo(diaAtivo);
        horarioParametro.setDescricaoDia(diaDescricao);
    }

    private void abrirHorarios(){
        fragmentHorarios = new HorarioDialog();
        Bundle bundle = new Bundle();
        if (op == 0) {
            bundle.putString("hora", inicio.getText().toString());
        }
        else{
            bundle.putString("hora", fim.getText().toString());
        }
        fragmentHorarios.setArguments(bundle);
        fragmentHorarios.show(getFragmentManager(), "horarios");
    }

    private void setSpinnerValues(int posicao, Boolean diaAtivo){
        if (posicao != -1){
            spinnerHorarios.setSelection(posicao);
        }
        if (diaAtivo){
            spinnerHorarios.setBackgroundColor(getResources().getColor(R.color.colorFacebook));
        }
        else{
            spinnerHorarios.setBackgroundColor(getResources().getColor(R.color.colorDiaSelecionado));
        }
    }

    @Override
    public void onReturnValue(String resultadoParametro) {
        if (!resultadoParametro.isEmpty()){
            if (op == 0) {
                inicio.setText(resultadoParametro);
            }
            else{
                fim.setText(resultadoParametro);
            }
        }
    }
}
