package com.gestion.model;

import java.util.List;

public interface PlanEstudio {
    boolean puedeCursar(Alumno alumno, Materia materia);

    List<Materia> getAllMaterias();

    void agregarMateria(Materia materia);

    String getNombre();

    List<Materia> getCorrelativas(Materia materia);

    void setCorrelativas(Materia materia, List<Materia> correlativas);

    List<Materia> getMateriasHastaCuatrimestre(int cuatrimestreLimite);

    int getCuatrimestre(Materia materia);
}
