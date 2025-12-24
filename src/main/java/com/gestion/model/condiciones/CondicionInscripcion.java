package com.gestion.model.condiciones;

import com.gestion.model.Alumno;
import com.gestion.model.Materia;
import com.gestion.model.PlanEstudio;

public interface CondicionInscripcion {
    /**
     * Verifica si el alumno cumple con los requisitos para inscribirse a la materia
     * dado el plan de estudios.
     */
    boolean esCumplida(Alumno alumno, Materia materia, PlanEstudio plan);
}
