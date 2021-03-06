package com.atendimento.model;

import com.atendimento.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Empresa implements Serializable {

    private String idUsuario;
    private String id;
    private String nome;
    private String Categoria;
    private String urlImagem;
    private int tipoCadastro;
    private Boolean selecionado = false;
    private byte[] imageArray;

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

    public String getUrlImagem() { return urlImagem; }

    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }

    public int getTipoCadastro() { return tipoCadastro; }

    public void setTipoCadastro(int tipoCadastro) { this.tipoCadastro = tipoCadastro; }

    @Exclude
    public Boolean getSelecionado() { return selecionado; }

    @Exclude
    public void setSelecionado(Boolean selecionado) { this.selecionado = selecionado; }

    @Exclude
    public byte[] getImageArray() { return imageArray; }

    @Exclude
    public void setImageArray(byte[] imageArray) { this.imageArray = imageArray; }
}
