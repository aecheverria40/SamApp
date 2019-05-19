package com.example.lashope.SamAPP.Models;

import com.example.lashope.SamAPP.Models.Maestro;

import java.io.Serializable;

public class Reserva implements Serializable {
    private String Uid;
    private String audiovisual;
    private String fecha;
    private String hora;
    private Maestro maestro;
    private String tema;
    private String comment;
    /*
    public Reserva(String uid, String audiovisual, String fecha, String hora, Maestro maestro, String tema, String comment) {
        Uid = uid;
        this.audiovisual = audiovisual;
        this.fecha = fecha;
        this.hora = hora;
        this.maestro = maestro;
        this.tema = tema;
        this.comment = comment;
    }
    */
    public String getAudiovisual() {
        return audiovisual;
    }

    public void setAudiovisual(String audiovisual) {
        this.audiovisual = audiovisual;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Maestro getMaestro() {
        return maestro;
    }

    public void setMaestro(Maestro maestro) {
        this.maestro = maestro;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    @Override
    public String toString() {
        return this.getHora();
    }
}
