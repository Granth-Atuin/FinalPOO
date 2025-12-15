package com.gestion.condiciones;

import com.gestion.Alumno;
import com.gestion.Materia;
import com.gestion.PlanDeEstudios;

public interface CondicionInscripcion {
    /**
     * Verifica si el alumno cumple con los requisitos para inscribirse a la materia
     * dado el plan de estudios.
     */
    boolean esCumplida(Alumno alumno, Materia materia, PlanDeEstudios plan);
}
