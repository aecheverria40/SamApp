package com.example.samapp;

public class dboProyector {
    private String id;
    private String marca;
    private String modelo;
    private String disponible;
    private String proyector;
    private String maestro;

    public dboProyector() {

    }

    public dboProyector(String id, String marca, String modelo, String disponible,String proyector, String maestro) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.disponible = disponible;
        this.proyector = proyector;
        this.maestro = maestro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDisponible() {
        return disponible;
    }

    public void setDisponible(String disponible) {
        this.disponible = disponible;
    }

    public String getProyector() {
        return proyector;
    }

    public void setProyector(String proyector) {
        this.proyector = proyector;
    }

    public String getMaestro() {
        return maestro;
    }

    public void setMaestro(String maestro) {
        this.maestro = maestro;
    }

    @Override
    public String toString() {
        return this.marca;
    }
}
