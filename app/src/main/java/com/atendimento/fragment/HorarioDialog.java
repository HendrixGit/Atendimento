package com.atendimento.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.atendimento.R;
import com.atendimento.util.MyDialogFragmentListener;

import java.text.DecimalFormat;


public class HorarioDialog extends DialogFragment {

    private View viewHorarios;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private MyDialogFragmentListener activityChamada;
    private TimePicker timePickerHorarios;
    private String horaParemetro = "";

    private static final int INTERVAL = 15;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");

    private NumberPicker minutePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity(),R.style.dialog);
        inflater           = getActivity().getLayoutInflater();
        viewHorarios       = inflater.inflate(R.layout.hora_dialog, null, false);
        timePickerHorarios = viewHorarios.findViewById(R.id.timePickerHorario);

        timePickerHorarios.setIs24HourView(true);
        timePickerHorarios.setCurrentHour(8);
        timePickerHorarios.setCurrentMinute(00);
        setMinutePicker();

        builder.setView(viewHorarios)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hora = timePickerHorarios.getCurrentHour();
                        horaParemetro = String.valueOf(hora);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }

    public void setMinutePicker() {
        int numValues = 60 / INTERVAL;
        String[] displayedValues = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            displayedValues[i] = FORMATTER.format(i * INTERVAL);
        }

        View minute = timePickerHorarios.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
        if ((minute != null) && (minute instanceof NumberPicker)) {
            minutePicker = (NumberPicker) minute;
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(numValues - 1);
            minutePicker.setDisplayedValues(displayedValues);
        }
    }

    public int getMinute() {
        if (minutePicker != null) {
            return (minutePicker.getValue() * INTERVAL);
        } else {
            return timePickerHorarios.getCurrentMinute();
        }
    }

}
