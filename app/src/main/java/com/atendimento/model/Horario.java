package com.atendimento.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Horario implements Parcelable {

    private String idEmpresa;
    private int duracao;
    private int    diaSemana;
    private String descricaoDia;
    private String horaInicio;
    private String horaFinal;
    private Boolean diaAtivo;

    public Horario() {

    }

    protected Horario(Parcel in) {
        idEmpresa = in.readString();
        duracao = in.readInt();
        diaSemana = in.readInt();
        descricaoDia = in.readString();
        horaInicio = in.readString();
        horaFinal = in.readString();
        byte tmpDiaAtivo = in.readByte();
        diaAtivo = tmpDiaAtivo == 0 ? null : tmpDiaAtivo == 1;
    }

    public static final Creator<Horario> CREATOR = new Creator<Horario>() {
        @Override
        public Horario createFromParcel(Parcel in) {
            return new Horario(in);
        }

        @Override
        public Horario[] newArray(int size) {
            return new Horario[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idEmpresa);
        parcel.writeInt(duracao);
        parcel.writeInt(diaSemana);
        parcel.writeString(descricaoDia);
        parcel.writeString(horaInicio);
        parcel.writeString(horaFinal);
        parcel.writeByte((byte) (diaAtivo == null ? 0 : diaAtivo ? 1 : 2));
    }
}
