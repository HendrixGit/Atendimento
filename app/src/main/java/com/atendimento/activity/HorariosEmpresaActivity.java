package com.atendimento.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.atendimento.fragment.HorarioDialog;
import com.atendimento.model.Empresa;
import com.atendimento.model.Horario;
import com.atendimento.util.MyDialogFragmentListener;
import com.atendimento.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HorariosEmpresaActivity extends BaseActivity implements MyDialogFragmentListener {

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
    private DialogFragment fragmentHorarios;
    private int op;
    private Spinner spinnerDuracao;
    private int pos = 0;
    private Boolean diaAtivo =  true;
    private ImageView setarHorarios;
    private Util util;
    private Dialog.OnClickListener yesClickDialog;
    private AlertDialog perguntaHorarios;

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
                        "Deseja atribuir o horário selecionado para todos os dias?",
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
                empresaHorarios.get(spinnerHorarios.getSelectedItemPosition()).setHoraInicio(inicio.getText().toString());
                empresaHorarios.get(spinnerHorarios.getSelectedItemPosition()).setHoraFinal(fim.getText().toString());
                empresaHorarios.get(spinnerHorarios.getSelectedItemPosition()).setDiaAtivo(retornoDiaAtivo());
                setCheckbox(empresaHorarios.get(spinnerHorarios.getSelectedItemPosition()));
                diaAtivo = empresaHorarios.get(spinnerHorarios.getSelectedItemPosition()).getDiaAtivo();
                pos = spinnerHorarios.getSelectedItemPosition();

                preencheSpinner(pos, diaAtivo);
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

        empresaHorarios = new ArrayList<>();

        horarioSegunda = setarHorarios(Calendar.MONDAY, getResources().getString(R.string.segunda),
                getResources().getString(R.string.horarioPadraoInicial),
                getResources().getString(R.string.horarioPadraoFinal), true);
        empresaHorarios.add(horarioSegunda);

        horarioTerca = setarHorarios(Calendar.TUESDAY, getResources().getString(R.string.terca),
                getResources().getString(R.string.horarioPadraoInicial),
                getResources().getString(R.string.horarioPadraoFinal), true);
        empresaHorarios.add(horarioTerca);

        horarioQuarta = setarHorarios(Calendar.WEDNESDAY, getResources().getString(R.string.quarta),
                getResources().getString(R.string.horarioPadraoInicial),
                getResources().getString(R.string.horarioPadraoFinal), true);
        empresaHorarios.add(horarioQuarta);

        horarioQuinta = setarHorarios(Calendar.THURSDAY, getResources().getString(R.string.quinta),
                getResources().getString(R.string.horarioPadraoInicial),
                getResources().getString(R.string.horarioPadraoFinal), true);
        empresaHorarios.add(horarioQuinta);

        horarioSexta = setarHorarios(Calendar.FRIDAY, getResources().getString(R.string.sexta),
                getResources().getString(R.string.horarioPadraoInicial),
                getResources().getString(R.string.horarioPadraoFinal), true);
        empresaHorarios.add(horarioSexta);

        horarioSabado = setarHorarios(Calendar.SATURDAY, getResources().getString(R.string.sabado),
                getResources().getString(R.string.horarioPadraoInicial),
                getResources().getString(R.string.horarioPadraoMeioDia), true);
        empresaHorarios.add(horarioSabado);

        horarioDomingo = setarHorarios(Calendar.SUNDAY, getResources().getString(R.string.domingo),
                getResources().getString(R.string.horarioPadraoInicial),
                getResources().getString(R.string.horarioPadraoFinal), false);
        empresaHorarios.add(horarioDomingo);

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

    private Horario setarHorarios(Integer dia, String diaDescricao, String inicio, String fim, Boolean diaAtivo){
        Horario horarioParametro = new Horario();
        horarioParametro.setDiaSemana(dia);
        horarioParametro.setDescricaoDia(diaDescricao);
        horarioParametro.setHoraInicio(inicio);
        horarioParametro.setHoraFinal(fim);
        horarioParametro.setDiaAtivo(diaAtivo);
        return horarioParametro;
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
