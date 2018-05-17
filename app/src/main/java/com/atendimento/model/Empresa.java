package com.atendimento.model;



import com.atendimento.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Empresa {

    private String idUsuario;
    private String id;
    private String nome;
    private String Categoria;

    public Empresa() {

    }


    public void salvar(){
        DatabaseReference referenciaDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        referenciaDatabase.child("empresas").child(getIdUsuario()).setValue(this);
    }


    public String getIdUsuario() { return idUsuario; }

    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

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

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

}
