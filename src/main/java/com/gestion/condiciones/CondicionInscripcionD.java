package com.gestion.condiciones;

import com.gestion.Alumno;
import com.gestion.Materia;
import com.gestion.PlanDeEstudios;
import java.util.List;

public class CondicionInscripcionD implements CondicionInscripcion {
    @Override
    public boolean esCumplida(Alumno alumno, Materia materia, PlanDeEstudios plan) {
        // 1. Cursadas de correlativas
        List<Materia> correlativas = plan.getCorrelativas(materia);
        for (Materia corr : correlativas) {
            if (!alumno.tieneCursadaAprobada(corr))
                return false;
        }

        // 2. Finales de 3 cuatrimestres previos
        int cuatrimestreActual = plan.getCuatrimestre(materia);
        int limite = cuatrimestreActual - 3;
        if (limite > 0) {
            List<Materia> previas = plan.getMateriasHastaCuatrimestre(limite);
            for (Materia prev : previas) {
                if (!alumno.tieneFinalAprobado(prev))
                    return false;
            }
        }

        return true;
    }
}
