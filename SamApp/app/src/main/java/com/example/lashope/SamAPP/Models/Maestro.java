package com.example.lashope.SamAPP.Models;

public class Maestro {
    private String Uid;
    private String nombre;
    private String correo;
    private long telefono;


    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    /*

    public String toString() {
        return "Name: " + this.nombre + "\nCorreo:" + this.correo + "\nTelefono:"+ this.telefono;
    }
    */


    public String toString() {
        return this.nombre;
    }
}