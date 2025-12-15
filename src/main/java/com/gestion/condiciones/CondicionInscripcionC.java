package com.gestion.condiciones;

import com.gestion.Alumno;
import com.gestion.Materia;
import com.gestion.PlanDeEstudios;
import java.util.List;

public class CondicionInscripcionC implements CondicionInscripcion {
    @Override
    public boolean esCumplida(Alumno alumno, Materia materia, PlanDeEstudios plan) {
        // 1. Cursadas de correlativas
        List<Materia> correlativas = plan.getCorrelativas(materia);
        for (Materia corr : correlativas) {
            if (!alumno.tieneCursadaAprobada(corr))
                return false;
        }

        // 2. Finales de 5 cuatrimestres previos
        int cuatrimestreActual = plan.getCuatrimestre(materia);
        int limite = cuatrimestreActual - 5;
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
