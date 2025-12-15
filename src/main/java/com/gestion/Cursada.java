package com.gestion;

public class Cursada {
    private Materia materia;
    private EstadoCursada estado;
    private double nota; // Opcional, por si se requiere en el futuro

    public Cursada(Materia materia) {
        this.materia = materia;
        this.estado = EstadoCursada.INSCRIPTO;
    }

    public Materia getMateria() {
        return materia;
    }

    public EstadoCursada getEstado() {
        return estado;
    }

    public void setEstado(EstadoCursada estado) {
        this.estado = estado;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public boolean esCursadaAprobada() {
        return estado == EstadoCursada.CURSADA_APROBADA ||
                estado == EstadoCursada.PROMOCIONADA ||
                estado == EstadoCursada.FINAL_APROBADO;
    }

    public boolean esFinalAprobado() {
        return estado == EstadoCursada.PROMOCIONADA ||
                estado == EstadoCursada.FINAL_APROBADO;
    }

    @Override
    public String toString() {
        if (nota > 0) {
            return materia.getNombre() + " [" + estado + "] Nota: " + nota;
        }
        return materia.getNombre() + " [" + estado + "]";
    }
}
