package com.gestion;

import com.gestion.condiciones.CondicionInscripcion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanDeEstudios implements PlanEstudio {
    private String nombre;
    private List<Materia> materiasObligatorias;
    private List<Materia> materiasOptativas;
    private Map<Materia, List<Materia>> correlativas;
    private Map<Materia, CondicionInscripcion> condicionesInscripcion;
    private Map<Materia, Integer> cuatrimestres; // 1er cuatrimestre, 2do...

    // Configuración de graduación
    private int cantidadOptativasParaGraduacion;

    public PlanDeEstudios(String nombre) {
        this.nombre = nombre;
        this.materiasObligatorias = new ArrayList<>();
        this.materiasOptativas = new ArrayList<>();
        this.correlativas = new HashMap<>();
        this.condicionesInscripcion = new HashMap<>();
        this.cuatrimestres = new HashMap<>();
        this.cantidadOptativasParaGraduacion = 0;
    }

    // --- Getters de Información del Plan ---

    public List<Materia> getCorrelativas(Materia materia) {
        return correlativas.getOrDefault(materia, new ArrayList<>());
    }

    public int getCuatrimestre(Materia materia) {
        return cuatrimestres.getOrDefault(materia, 0); // 0 implica no asignado
    }

    public CondicionInscripcion getCondicionInscripcion(Materia materia) {
        return condicionesInscripcion.get(materia); // Puede ser null
    }

    /**
     * Retorna todas las materias obligatorias y optativas hasta el cuatrimestre N
     * (inclusive).
     */
    public List<Materia> getMateriasHastaCuatrimestre(int cuatrimestreLimite) {
        List<Materia> resultado = new ArrayList<>();
        for (Materia m : materiasObligatorias) {
            if (getCuatrimestre(m) <= cuatrimestreLimite && getCuatrimestre(m) > 0) {
                resultado.add(m);
            }
        }
        for (Materia m : materiasOptativas) {
            if (getCuatrimestre(m) <= cuatrimestreLimite && getCuatrimestre(m) > 0) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    public List<Materia> getAllMaterias() {
        List<Materia> all = new ArrayList<>(materiasObligatorias);
        all.addAll(materiasOptativas);
        return all;
    }

    public boolean puedeCursar(Alumno alumno, Materia materia) {
        CondicionInscripcion condicion = condicionesInscripcion.get(materia);
        if (condicion == null) {
            // Si no hay condición explícita, asumimos que puede cursar (o definir regla
            // default)
            return true;
        }
        return condicion.esCumplida(alumno, materia, this);
    }

    public int getCantidadOptativasParaGraduacion() {
        return cantidadOptativasParaGraduacion;
    }

    // --- Configuración (Usado por Builder) ---

    public void agregarMateriaObligatoria(Materia materia, int cuatrimestre) {
        materiasObligatorias.add(materia);
        cuatrimestres.put(materia, cuatrimestre);
    }

    public void agregarMateriaOptativa(Materia materia, int cuatrimestre) {
        materiasOptativas.add(materia);
        cuatrimestres.put(materia, cuatrimestre);
    }

    public void setCorrelativas(Materia materia, List<Materia> correlativasList) {
        this.correlativas.put(materia, correlativasList);
    }

    public void setCondicionInscripcion(Materia materia, CondicionInscripcion condicion) {
        this.condicionesInscripcion.put(materia, condicion);
    }

    public void setCantidadOptativasParaGraduacion(int cantidad) {
        this.cantidadOptativasParaGraduacion = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public void agregarMateria(Materia materia) {
        agregarMateriaObligatoria(materia, 0); // Default behavior
    }
}
