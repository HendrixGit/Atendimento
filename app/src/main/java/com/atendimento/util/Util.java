package com.atendimento.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;

import com.atendimento.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Util{

    private String sql;

    public String returnDataString(Date data) throws ParseException {
        long now = System.currentTimeMillis();
        Date dataAtual = new Date(now);
        String dataFormatada = "";
        SimpleDateFormat sdf;

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        dataAtual = sdf.parse(sdf.format(dataAtual));
        Date dataParametro = sdf.parse(sdf.format(data));

        if (dataAtual.compareTo(dataParametro) <= 0 ) {
            sdf = new SimpleDateFormat("HH:mm");
        }
        else{
            sdf = new SimpleDateFormat("dd/MM/yyyy");
        }
        dataFormatada = sdf.format(data);
        return dataFormatada;
    }

    public Bitmap diminuirImagem(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    public AlertDialog.Builder YesNoDialog(String titulo, Context context, DialogInterface.OnClickListener onClickListenerYes){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        builder.setTitle(titulo);
        builder.setPositiveButton("Sim", onClickListenerYes);
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder;
    }

    public AlertDialog.Builder CustomDialog(String titulo, Context context, String textoBotao1, DialogInterface.OnClickListener onClickListenerBotao1,
                                            String textoBotao2, DialogInterface.OnClickListener onClickListenerBotao2){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        builder.setTitle(titulo);
        builder.setPositiveButton(textoBotao1, onClickListenerBotao1);
        builder.setNegativeButton(textoBotao2, onClickListenerBotao2);
        return builder;
    }

    public AlertDialog.Builder HorarioDialog(String titulo, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialogHorario);
        builder.setTitle(titulo);
        builder.setPositiveButton(titulo,null);
        builder.setNegativeButton("Cancelar",null);
        return builder;
    }

    public Bundle bundleStringGenerico(String nomeParemetro, String valor){
        Bundle bundle = new Bundle();
        bundle.putString(nomeParemetro, valor);
        return bundle;
    }

}
