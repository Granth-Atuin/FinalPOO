package com.gestion;

public class Materia {
    private String nombre;
    private String codigo;
    private int cuatrimestre;
    private boolean esObligatoria;
    private java.util.List<Materia> correlativas;

    // Constructor
    public Materia(String nombre, String codigo, int cuatrimestre, boolean esObligatoria) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.cuatrimestre = cuatrimestre;
        this.esObligatoria = esObligatoria;
        this.correlativas = new java.util.ArrayList<>();
    }

    // Constructor simple para compatibilidad temporal (default: cuatri 0,
    // obligatoria true)
    public Materia(String nombre, String codigo) {
        this(nombre, codigo, 0, true);
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public boolean esObligatoria() {
        return esObligatoria;
    }

    public void setEsObligatoria(boolean esObligatoria) {
        this.esObligatoria = esObligatoria;
    }

    public void agregarCorrelativa(Materia m) {
        if (!correlativas.contains(m)) {
            correlativas.add(m);
        }
    }

    public java.util.List<Materia> getCorrelativas() {
        return correlativas;
    }

    @Override
    public String toString() {
        return nombre + " (" + codigo + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Materia materia = (Materia) o;
        return codigo.equals(materia.codigo); // Basamos la igualdad en el código único
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    // Equivalencias
    private java.util.List<Materia> equivalencias = new java.util.ArrayList<>();

    public void agregarEquivalencia(Materia m) {
        if (!equivalencias.contains(m)) {
            equivalencias.add(m);
        }
    }

    public java.util.List<Materia> getEquivalencias() {
        return equivalencias;
    }
}
