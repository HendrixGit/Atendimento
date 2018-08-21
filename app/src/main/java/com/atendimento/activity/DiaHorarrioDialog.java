package com.atendimento.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import com.atendimento.R;
import com.atendimento.fragment.HorarioDialog;
import com.atendimento.util.HorarioFragmentListener;
import com.atendimento.util.Util;


public class DiaHorarrioDialog extends DialogFragment {

    private View viewDiaHorarios;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private TextView inicio;
    private TextView fim;
    private CheckBox checkBoxDia;
    private HorarioFragmentListener horarioFragmentListener;
    private int op = 0;
    private HorarioDialog horariosDialog;
    private Util util;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder  = new AlertDialog.Builder(getActivity(), R.style.dialog);
        inflater = getActivity().getLayoutInflater();
        viewDiaHorarios = inflater.inflate(R.layout.horariosdialog, null, false);
        inicio = viewDiaHorarios.findViewById(R.id.textViewInicioHorario);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = 0;
                TimePicker timePicker = new TimePicker(getActivity());
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker timePicker, int i, int i1) {

                    }
                });
                timePicker.showContextMenu();
            }
        });
        fim    = viewDiaHorarios.findViewById(R.id.textViewFimHorario);
        fim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = 1;
            }
        });
        checkBoxDia = viewDiaHorarios.findViewById(R.id.checkBodDiaHorario);
        builder.setView(viewDiaHorarios)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }

    @Override
    public void dismiss() {
        horarioFragmentListener = (HorarioFragmentListener) getActivity();
        horarioFragmentListener.diaAtivo(true);
        horarioFragmentListener.horarioInicial(inicio.getText().toString());
        horarioFragmentListener.horarioFinal(fim.getText().toString());
    }

}