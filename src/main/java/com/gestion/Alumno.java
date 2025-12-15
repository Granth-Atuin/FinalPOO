package com.gestion;

import java.util.ArrayList;
import java.util.List;

public class Alumno {
    private String nombre;
    private String apellido;
    private int legajo;

    // Cambiado de 'carreras' a 'carrerasInscriptas' para coincidir con lo que
    // escribí en otros lados
    // O mejor, uso 'carreras' que es más estándar, pero ajusto el getter.
    private List<Carrera> carreras;

    // UML: Relación con Cursada (1..*)
    private List<Cursada> historiaAcademica;

    public Alumno(String nombre, String apellido, int legajo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.legajo = legajo;
        this.carreras = new ArrayList<>();
        this.historiaAcademica = new ArrayList<>();
    }

    public void agregarCarrera(Carrera carrera) {
        if (!carreras.contains(carrera)) {
            carreras.add(carrera);
        }
    }

    public void inscribirMateria(Materia materia) {
        // Verificar si ya está inscripto
        for (Cursada cursada : historiaAcademica) {
            if (cursada.getMateria().equals(materia))
                return; // Ya existe
        }
        historiaAcademica.add(new Cursada(materia));
    }

    public boolean finalizoCarrera(Carrera carrera) {
        // Lógica: aprobó todas las materias obligatorias de la carrera?
        // Carrera ahora tiene getMaterias()
        for (Materia m : carrera.getMaterias()) {
            if (m.esObligatoria() && !estaAprobada(m)) {
                return false;
            }
        }
        // Validación de optativas podría ir aquí
        return true;
    }

    public boolean estaAprobada(Materia m) {
        for (Cursada cursada : historiaAcademica) {
            if (cursada.getMateria().equals(m) && cursada.esFinalAprobado()) {
                return true;
            }
        }
        return false;
    }

    public boolean tieneFinalAprobado(Materia m) {
        return estaAprobada(m);
    }

    public boolean tieneCursadaAprobada(Materia m) {
        for (Cursada cursada : historiaAcademica) {
            if (cursada.getMateria().equals(m)) {
                return cursada.esCursadaAprobada();
            }
        }
        return false;
    }

    // Helper para obtener inscripción específica
    public Cursada getInscripcion(Materia m) {
        for (Cursada cursada : historiaAcademica) {
            if (cursada.getMateria().equals(m))
                return cursada;
        }
        return null;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getLegajo() {
        return legajo;
    }

    public void setLegajo(int legajo) {
        this.legajo = legajo;
    }

    public List<Carrera> getCarrerasInscriptas() { // Alias para compatibilidad
        return carreras;
    }

    public List<Carrera> getCarreras() {
        return carreras;
    }

    public List<Cursada> getHistoriaAcademica() {
        return historiaAcademica;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (Leg: " + legajo + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Alumno alumno = (Alumno) o;
        return legajo == alumno.legajo;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(legajo);
    }
}
