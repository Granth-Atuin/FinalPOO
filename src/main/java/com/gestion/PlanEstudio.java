package com.gestion;

public interface PlanEstudio {
    boolean puedeCursar(Alumno alumno, Materia materia);

    java.util.List<Materia> getAllMaterias();

    void agregarMateria(Materia materia);

    String getNombre();
}
