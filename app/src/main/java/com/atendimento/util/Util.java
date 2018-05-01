package com.atendimento.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import com.atendimento.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Util{

    private SQLiteDatabase databaseAtendimento;

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

    public AlertDialog.Builder YesNoDialog(String titulo, Context context, DialogInterface.OnClickListener onClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        builder.setTitle(titulo);
        builder.setPositiveButton("Sim", onClickListener);
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder;
    }

    public String categoriasDatabase(int codigo){
        databaseAtendimento = SQLiteDatabase.openOrCreateDatabase("database", null);

        databaseAtendimento.execSQL("CREATE TABLE IF NOT EXISTS categorias(codigo INT(3),descricao VARCHAR) ");
        databaseAtendimento.execSQL("INSERT INTO categorias(codigo,descricao) VALUES('Clínicas Médicas',1)");
        databaseAtendimento.execSQL("INSERT INTO categorias(codigo,descricao) VALUES('Pet Shops',2)");
        databaseAtendimento.execSQL("INSERT INTO categorias(codigo,descricao) VALUES('Laboratórios',3)");
        databaseAtendimento.execSQL("INSERT INTO categorias(codigo,descricao) VALUES('Manicure/Pedicuere',4)");
        databaseAtendimento.execSQL("INSERT INTO categorias(codigo,descricao) VALUES('Salões de Beleza',5)");
        databaseAtendimento.execSQL("INSERT INTO categorias(codigo,descricao) VALUES('Escritórios Advocacia',6)");

        Cursor cursor = databaseAtendimento.rawQuery("SELECT * FROM categorias WHERE codigo = " + codigo,null);
        int indiceColunaDescricao = cursor.getColumnIndex("descricao");
        cursor.moveToFirst();

        return cursor.getString(indiceColunaDescricao);
    }

}
