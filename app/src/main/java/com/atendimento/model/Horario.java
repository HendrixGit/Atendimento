package com.atendimento.model;

import java.io.Serializable;

public class Horario implements Serializable {

    private String idEmpresa;
    private int duracao;
    private int    diaSemana;
    private String descricaoDia;
    private String horaInicio;
    private String horaFinal;
    private Boolean diaAtivo;

    public Horario() {

    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getDescricaoDia() {
        return descricaoDia;
    }

    public void setDescricaoDia(String descricaoDia) {
        this.descricaoDia = descricaoDia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public Boolean getDiaAtivo() {
        return diaAtivo;
    }

    public void setDiaAtivo(Boolean diaAtivo) {
        this.diaAtivo = diaAtivo;
    }
}
