package com.gestion;

import com.gestion.condiciones.CondicionInscripcion;
import java.util.List;

public class PlanDeEstudiosBuilder {
    private PlanDeEstudios plan;

    public PlanDeEstudiosBuilder(String nombrePlan) {
        this.plan = new PlanDeEstudios(nombrePlan);
    }

    public PlanDeEstudiosBuilder agregarMateriaObligatoria(Materia m, int cuatrimestre, CondicionInscripcion cond) {
        plan.agregarMateriaObligatoria(m, cuatrimestre);
        if (cond != null)
            plan.setCondicionInscripcion(m, cond);
        return this;
    }

    public PlanDeEstudiosBuilder agregarMateriaOptativa(Materia m, int cuatrimestre, CondicionInscripcion cond) {
        plan.agregarMateriaOptativa(m, cuatrimestre);
        if (cond != null)
            plan.setCondicionInscripcion(m, cond);
        return this;
    }

    public PlanDeEstudiosBuilder agregarCorrelativa(Materia materia, Materia correlativa) {
        List<Materia> current = plan.getCorrelativas(materia);
        current.add(correlativa);
        plan.setCorrelativas(materia, current);
        return this;
    }

    public PlanDeEstudiosBuilder setOptativasParaGraduacion(int cantidad) {
        plan.setCantidadOptativasParaGraduacion(cantidad);
        return this;
    }

    public PlanDeEstudios build() {
        return plan;
    }
}
