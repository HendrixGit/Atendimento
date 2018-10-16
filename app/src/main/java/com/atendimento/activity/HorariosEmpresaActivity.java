package com.atendimento.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.model.Empresa;
import com.atendimento.model.Horario;
import com.atendimento.util.Util;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HorariosEmpresaActivity extends BaseActivity {

    private ArrayList<Horario> empresaHorarios;
    private Horario horarioSegunda;
    private Horario horarioTerca;
    private Horario horarioQuarta;
    private Horario horarioQuinta;
    private Horario horarioSexta;
    private Horario horarioSabado;
    private Horario horarioDomingo;
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
    private Spinner spinnerDuracao;
    private int pos = 0;
    private Boolean diaAtivo =  true;
    private ImageView setarHorarios;
    private Util util;
    private Dialog.OnClickListener yesClickDialog;
    private AlertDialog perguntaHorarios;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios_empresa);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Cadastrar Horários");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        constraintLayoutHorarios = findViewById(R.id.layoutHorarios);
        util = new Util();
        yesClickDialog = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int x = 0;
                while (empresaHorarios.size() > x){
                    empresaHorarios.get(x).setHoraInicio(inicio.getText().toString());
                    empresaHorarios.get(x).setHoraFinal(fim.getText().toString());
                    empresaHorarios.get(x).setDiaAtivo(retornoDiaAtivo());
                    x++;
                }
                preencheSpinner(spinnerHorarios.getSelectedItemPosition(), empresaHorarios.get(spinnerHorarios.getSelectedItemPosition()).getDiaAtivo());
            }
        };

        setarHorarios = findViewById(R.id.imageViewSetarHorarios);
        setarHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = util.YesNoDialog(
                        "Atribuir o horário selecionado para todos os dias?",
                        HorariosEmpresaActivity.this,
                        yesClickDialog);

                perguntaHorarios = builder.create();
                perguntaHorarios.show();
            }
        });

        inicio = findViewById(R.id.textViewInicioHorarios);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirHorarios("Horário Inicial",0);
            }
        });
        fim    = findViewById(R.id.textViewFinalHorarios);
        fim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirHorarios("Horário Final", 1);
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
        final Cursor cursor = cursorDuracao(sqLiteDatabasePar,"");
        final int indiceColunaDescricao = cursor.getColumnIndex("descricao");
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
            @Override
            public void onClick(View view) {
                float duracao = getValorDuracao(spinnerDuracao.getSelectedItem().toString());

                float horaInicio = Integer.parseInt((String) inicio.getText().subSequence(0,2));
                float horaFim    = Integer.parseInt((String) fim.getText().subSequence(0,2));
                float resultadoHoras = (horaFim - horaInicio) * 60;

                if (resultadoHoras < duracao){
                    Toast.makeText(getApplicationContext(), "Duração maior que o período de horas", Toast.LENGTH_SHORT).show();
                }
                else {
                    empresaHorarios.get(spinnerHorarios.getSelectedItemPosition()).setHoraInicio(inicio.getText().toString());
                    empresaHorarios.get(spinnerHorarios.getSelectedItemPosition()).setHoraFinal(fim.getText().toString());
                    empresaHorarios.get(spinnerHorarios.getSelectedItemPosition()).setDiaAtivo(retornoDiaAtivo());
                    Integer duracaoParametro = getValorDuracao(spinnerDuracao.getSelectedItem().toString());
                    empresaHorarios.get(spinnerHorarios.getSelectedItemPosition()).setDuracao(duracaoParametro);


                    setCheckbox(empresaHorarios.get(spinnerHorarios.getSelectedItemPosition()));
                    diaAtivo = empresaHorarios.get(spinnerHorarios.getSelectedItemPosition()).getDiaAtivo();
                    pos = spinnerHorarios.getSelectedItemPosition();

                    preencheSpinner(pos, diaAtivo);
                }
            }
        });

        ok     = findViewById(R.id.buttonOKHorarios);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = 0;
                while (empresaHorarios.size() > x){
                    Cursor cursorHorararios = cursorDuracao(sqLiteDatabasePar, spinnerDuracao.getSelectedItem().toString());
                    int duracao = cursorHorararios.getColumnIndex("duracaoHorario");
                    cursorHorararios.moveToFirst();
                    empresaHorarios.get(x).setDuracao(cursorHorararios.getInt(duracao));
                    x++;
                }


                Intent intent = new Intent(getApplicationContext(), CadastrarEmpresaActivity.class);
                Empresa empresa = (Empresa) getIntent().getSerializableExtra("empresa");
                intent.putExtra("empresa", empresa);
                intent.putParcelableArrayListExtra("horarios", empresaHorarios);
                startActivity(intent);
                finish();
            }
        });
        cancel = findViewById(R.id.buttonCancelarHorarios);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent().getSerializableExtra("horarios") != null) {

            empresaHorarios = (ArrayList<Horario>) getIntent().getSerializableExtra("horarios");

            horarioSegunda = setarHorarios(empresaHorarios.get(0).getDiaSemana(), empresaHorarios.get(0).getDescricaoDia(),
                    empresaHorarios.get(0).getHoraInicio(), empresaHorarios.get(0).getHoraFinal(), empresaHorarios.get(0).getDiaAtivo(),
                    empresaHorarios.get(0).getOrdem());

            horarioTerca = setarHorarios(empresaHorarios.get(1).getDiaSemana(), empresaHorarios.get(1).getDescricaoDia(),
                    empresaHorarios.get(1).getHoraInicio(), empresaHorarios.get(1).getHoraFinal(), empresaHorarios.get(1).getDiaAtivo(),
                    empresaHorarios.get(1).getOrdem());

            horarioQuarta = setarHorarios(empresaHorarios.get(2).getDiaSemana(), empresaHorarios.get(2).getDescricaoDia(),
                    empresaHorarios.get(2).getHoraInicio(), empresaHorarios.get(2).getHoraFinal(), empresaHorarios.get(2).getDiaAtivo(),
                    empresaHorarios.get(2).getOrdem());

            horarioQuinta = setarHorarios(empresaHorarios.get(3).getDiaSemana(), empresaHorarios.get(1).getDescricaoDia(),
                    empresaHorarios.get(3).getHoraInicio(), empresaHorarios.get(3).getHoraFinal(), empresaHorarios.get(3).getDiaAtivo(),
                    empresaHorarios.get(3).getOrdem());

            horarioSexta = setarHorarios(empresaHorarios.get(4).getDiaSemana(), empresaHorarios.get(4).getDescricaoDia(),
                    empresaHorarios.get(4).getHoraInicio(), empresaHorarios.get(4).getHoraFinal(), empresaHorarios.get(4).getDiaAtivo(),
                    empresaHorarios.get(4).getOrdem());

            horarioSabado = setarHorarios(empresaHorarios.get(5).getDiaSemana(), empresaHorarios.get(5).getDescricaoDia(),
                    empresaHorarios.get(5).getHoraInicio(), empresaHorarios.get(5).getHoraFinal(), empresaHorarios.get(5).getDiaAtivo(),
                    empresaHorarios.get(5).getOrdem());

            horarioDomingo = setarHorarios(empresaHorarios.get(6).getDiaSemana(), empresaHorarios.get(6).getDescricaoDia(),
                    empresaHorarios.get(6).getHoraInicio(), empresaHorarios.get(6).getHoraFinal(), empresaHorarios.get(6).getDiaAtivo(),
                    empresaHorarios.get(6).getOrdem());

            preencheSpinnerDuracao(String.valueOf(empresaHorarios.get(0).getDuracao()));

        }
        else{

            empresaHorarios = new ArrayList<>();

            horarioSegunda = setarHorarios(Calendar.MONDAY, getResources().getString(R.string.segunda),
                    getResources().getString(R.string.horarioPadraoInicial),
                    getResources().getString(R.string.horarioPadraoFinal), true, 1);

            horarioTerca = setarHorarios(Calendar.TUESDAY, getResources().getString(R.string.terca),
                    getResources().getString(R.string.horarioPadraoInicial),
                    getResources().getString(R.string.horarioPadraoFinal), true, 2);

            horarioQuarta = setarHorarios(Calendar.WEDNESDAY, getResources().getString(R.string.quarta),
                    getResources().getString(R.string.horarioPadraoInicial),
                    getResources().getString(R.string.horarioPadraoFinal), true, 3);

            horarioQuinta = setarHorarios(Calendar.THURSDAY, getResources().getString(R.string.quinta),
                    getResources().getString(R.string.horarioPadraoInicial),
                    getResources().getString(R.string.horarioPadraoFinal), true, 4);

            horarioSexta = setarHorarios(Calendar.FRIDAY, getResources().getString(R.string.sexta),
                    getResources().getString(R.string.horarioPadraoInicial),
                    getResources().getString(R.string.horarioPadraoFinal), true, 5);

            horarioSabado = setarHorarios(Calendar.SATURDAY, getResources().getString(R.string.sabado),
                    getResources().getString(R.string.horarioPadraoInicial),
                    getResources().getString(R.string.horarioPadraoMeioDia), true, 6);

            horarioDomingo = setarHorarios(Calendar.SUNDAY, getResources().getString(R.string.domingo),
                    getResources().getString(R.string.horarioPadraoInicial),
                    getResources().getString(R.string.horarioPadraoMeioDia), false, 7);

            empresaHorarios.add(horarioSegunda);
            empresaHorarios.add(horarioTerca);
            empresaHorarios.add(horarioQuarta);
            empresaHorarios.add(horarioQuinta);
            empresaHorarios.add(horarioSexta);
            empresaHorarios.add(horarioSabado);
            empresaHorarios.add(horarioDomingo);
        }


        listaHorarios = new ArrayList<>();
        spinnerHorarios = findViewById(R.id.spinnerHorarios);
        preencheSpinner(pos,diaAtivo);
        spinnerHorarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setCampos(empresaHorarios.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void preencheSpinnerDuracao(String descricaoDuracao) {
        Integer opDuracaoFinal = getCodigoDuracao(descricaoDuracao);
        spinnerDuracao.setSelection(opDuracaoFinal - 1);
    }

    private int getCodigoDuracao(String descricaoDuracao){
        Cursor cursorDuracao = cursorDuracao(sqLiteDatabasePar, descricaoDuracao);
        Integer duracao = cursorDuracao.getColumnIndex("codigo");
        cursorDuracao.moveToFirst();
        Integer opDuracao = cursorDuracao.getInt(duracao);
        return opDuracao;
    }

    private int getValorDuracao(String descricaoDuracao){
        Cursor cursorDuracao = cursorDuracao(sqLiteDatabasePar, descricaoDuracao);
        Integer duracao = cursorDuracao.getColumnIndex("duracaoHorario");
        cursorDuracao.moveToFirst();
        Integer opDuracao = cursorDuracao.getInt(duracao);
        return opDuracao;
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
        preencheSpinnerDuracao(String.valueOf(horarioParametro.getDuracao()));
    }

    private void preencheSpinner(Integer posicao, Boolean diaParametro){
        listaHorarios.clear();
        for (Horario horario : empresaHorarios){
            listaHorarios.add(horario.getDescricaoDia() + " - " + horario.getHoraInicio() + " - " + horario.getHoraFinal());
        }
        dataHorarios = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listaHorarios);
        spinnerHorarios.setAdapter(dataHorarios);

        setSpinnerValues(posicao, diaParametro);
        spinnerHorarios.setSelection(posicao);
    }

    private Horario setarHorarios(Integer dia, String diaDescricao, String inicio, String fim, Boolean diaAtivo, Integer ordemParametro){
        Horario horarioParametro = new Horario();
        horarioParametro.setDiaSemana(dia);
        horarioParametro.setDescricaoDia(diaDescricao);
        horarioParametro.setHoraInicio(inicio);
        horarioParametro.setHoraFinal(fim);
        horarioParametro.setDiaAtivo(diaAtivo);
        horarioParametro.setOrdem(ordemParametro);
        return horarioParametro;
    }

    private void abrirHorarios(String titulo, final Integer operacao){
        String hora = "";
        String horaInicial = "";
        timePickerDialog = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        String zeros = "";
                        if (hourOfDay <= 9){  zeros = "0";  }
                        if(operacao == 0) {
                            inicio.setText(zeros + hourOfDay + ":" + minute + 0);
                        }
                        else{
                            fim.setText(zeros + hourOfDay + ":" + minute + 0);
                        }
                    }}, true);

        timePickerDialog.setTitle(titulo);
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.setInitialSelection(8, 00);
        timePickerDialog.setMaxTime(18,00,00);
        timePickerDialog.setMinTime(8, 00,00);
        timePickerDialog.setTimeInterval(1, 60);
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);

        if(operacao == 0){
            hora = inicio.getText().toString().substring(0,2);
        }
        else{
            horaInicial = inicio.getText().toString().substring(0,2);
            timePickerDialog.setMinTime(Integer.parseInt(horaInicial) + 1, 00, 00);
            hora = fim.getText().toString().substring(0,2);
        }

        timePickerDialog.setInitialSelection(Integer.parseInt(hora), 00);
        timePickerDialog.show(getFragmentManager(), "Horarios");
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

}
