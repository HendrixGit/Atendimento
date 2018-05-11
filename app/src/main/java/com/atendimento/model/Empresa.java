package com.atendimento.model;


import android.content.Context;

import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.util.Preferencias;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Empresa {

    private String id;
    private String nome;
    private String Categoria;
    private Preferencias preferencias;

    public Empresa(Context context) {
        preferencias = new Preferencias(context);
    }


    public void salvar(){
        DatabaseReference referenciaDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        referenciaDatabase.child("empresas").child(preferencias.getIdentificador()).child(getId()).setValue(this);
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
