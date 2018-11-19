package com.atendimento.model;

import java.io.Serializable;

public class Endereco implements Serializable {
    private String idEmpresa;
    private Double  latitude;
    private Double  longitude;
    private String endereco;
    private String cidade;
    private String pais;
    private String textoPesquisado;

    public Endereco() {
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() { return longitude; }

    public void setLongitude(Double longitudr) {
        this.longitude = longitudr;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTextoPesquisado() { return textoPesquisado; }

    public void setTextoPesquisado(String textoPesquisado) { this.textoPesquisado = textoPesquisado; }
}
