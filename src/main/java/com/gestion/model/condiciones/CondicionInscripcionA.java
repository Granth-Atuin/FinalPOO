package com.gestion.model.condiciones;

import com.gestion.model.Alumno;
import com.gestion.model.Materia;
import com.gestion.model.PlanEstudio;
import java.util.List;

public class CondicionInscripcionA implements CondicionInscripcion {
    @Override
    public boolean esCumplida(Alumno alumno, Materia materia, PlanEstudio plan) {
        List<Materia> correlativas = plan.getCorrelativas(materia);
        for (Materia correlativa : correlativas) {
            if (!alumno.tieneCursadaAprobada(correlativa)) {
                return false;
            }
        }
        return true;
    }
}
