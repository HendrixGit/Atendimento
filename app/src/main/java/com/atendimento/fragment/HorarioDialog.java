package com.atendimento.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.atendimento.R;
import com.atendimento.util.MyDialogFragmentListener;

public class HorarioDialog extends DialogFragment {

    private View viewHorarios;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private MyDialogFragmentListener activityChamada;
    private TimePicker timePickerHorarios;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity(),R.style.dialog);
        //builder.setCancelable(false);
        inflater           = getActivity().getLayoutInflater();
        viewHorarios       = inflater.inflate(R.layout.hora_dialog, null, false);
        timePickerHorarios = viewHorarios.findViewById(R.id.timePickerHorario);

        timePickerHorarios.setEnabled(true);
        timePickerHorarios.setVisibility(View.VISIBLE);
        return builder.create();
    }
}
