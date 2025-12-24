package com.gestion.controller;

import com.gestion.model.Carrera;
import com.gestion.model.Facultad;
import com.gestion.model.Materia;
import com.gestion.model.PlanEstudio;
import java.util.List;

public class CarreraController {

    private Facultad facultad;

    public CarreraController() {
        this.facultad = Facultad.getInstance();
    }

    public List<Carrera> listarCarreras() {
        return facultad.getCarreras();
    }

    public void asignarCorrelativas(PlanEstudio plan, Materia materiaObjetivo, List<Materia> correlativas) {
        if (plan != null) {
            plan.setCorrelativas(materiaObjetivo, correlativas);
        }
    }

    public void agregarCarrera(Carrera carrera) {
        facultad.agregarCarrera(carrera);
    }

    public List<Materia> getMaterias() {
        return facultad.getMaterias();
    }

    public void agregarMateria(Materia materia) {
        facultad.agregarMateria(materia);
    }

    public String generarProximoCodigoMateria(int carreraId) {
        return facultad.generarProximoCodigoMateria(carreraId);
    }

    // Additional methods for Carrera management can be added here
    public boolean existeMateria(String codigo) {
        return facultad.existeMateria(codigo);
    }
}
