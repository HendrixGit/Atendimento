package com.atendimento.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {

    private Context contexto;
    private SharedPreferences sharedPreferences;
    private String NOME_ARQUIVO = "atendimento.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;
    private String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private String CHAVE_NOME = "nomeUsuarioLogado";


    public Preferencias(Context contextoParametro){
        contexto = contextoParametro;
        sharedPreferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = sharedPreferences.edit();
    }

    public void salvarDados(String identificadorUsuario, String nomeUsuario){
        editor.putString(CHAVE_IDENTIFICADOR ,identificadorUsuario);
        editor.putString(CHAVE_NOME ,nomeUsuario);
        editor.commit();
    }

    public String getIdentificador(){
        return sharedPreferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome(){
        return sharedPreferences.getString(CHAVE_NOME, null);
    }

}
