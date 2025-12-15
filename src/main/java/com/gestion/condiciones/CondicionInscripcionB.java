package com.gestion.condiciones;

import com.gestion.Alumno;
import com.gestion.Materia;
import com.gestion.PlanDeEstudios;
import java.util.List;

public class CondicionInscripcionB implements CondicionInscripcion {
    @Override
    public boolean esCumplida(Alumno alumno, Materia materia, PlanDeEstudios plan) {
        List<Materia> correlativas = plan.getCorrelativas(materia);
        for (Materia correlativa : correlativas) {
            if (!alumno.tieneFinalAprobado(correlativa)) {
                return false;
            }
        }
        return true;
    }
}
