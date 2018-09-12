package com.atendimento.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.atendimento.R;
import com.atendimento.util.MyDialogFragmentListener;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;


public class HorarioDialog extends DialogFragment {

    private View viewHorarios;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private MyDialogFragmentListener activityChamada;
    private MaterialCalendarView timePicker;
    private TimePicker timePickerHorarios;
    private String horaParemetro = "";

    private static final int INTERVAL = 60;
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
        setMinutePicker(INTERVAL);

        if (!getArguments().isEmpty()) {
            if (!getArguments().getString("hora").equals("")){
                String horaParemetro = getArguments().getString("hora");
                int hora = Integer.parseInt(horaParemetro.substring(0,2));
                timePickerHorarios.setCurrentHour(hora);
            }
        }

        builder.setView(viewHorarios)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Calendar calendar = Calendar.getInstance();
                        if (Build.VERSION.SDK_INT < 23){
                            calendar.set(Calendar.HOUR_OF_DAY, timePickerHorarios.getCurrentHour());
                            calendar.set(Calendar.MINUTE, getMinute());
                        }
                        else {
                            calendar.set(Calendar.HOUR_OF_DAY, timePickerHorarios.getHour());
                            calendar.set(Calendar.MINUTE, getMinute());
                        }
                        Time time = new Time(Location.FORMAT_MINUTES);
                        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(getActivity());
                        horaParemetro = dateFormat.format(calendar.getTime());
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        activityChamada = (MyDialogFragmentListener) getActivity();
        activityChamada.onReturnValue(horaParemetro);
        super.onDismiss(dialog);
    }

    public void setMinutePicker(Integer intervalo) {
        int numValues = 60 / intervalo;
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
        }
        else {
            return timePickerHorarios.getCurrentMinute();
        }
    }

}
