package com.gestion.model;

public class Carrera {
    private static int CONTADOR_IDS = 1;
    private int id;
    private String nombre;
    private int cantidadOptativasNecesarias;
    private PlanEstudio planEstudio; // Strategy

    public Carrera(String nombre, PlanEstudio plan) {
        this.id = CONTADOR_IDS++;
        this.nombre = nombre;
        this.planEstudio = plan;
        this.cantidadOptativasNecesarias = 0;
    }

    public void agregarMateria(Materia m) {
        planEstudio.agregarMateria(m);
    }

    public void inscribirAlumno(Alumno a) {
        a.agregarCarrera(this);
    }

    public void inscribirMateria(Alumno a, Materia m) {
        if (puedeCursar(m, a)) {
            a.inscribirMateria(m);
        } else {
            throw new RuntimeException("El alumno no cumple las condiciones para cursar " + m.getNombre());
        }
    }

    public void definirPlanDeEstudios(PlanEstudio nuevoPlan) {
        this.planEstudio = nuevoPlan;
    }

    public boolean puedeCursar(Materia m, Alumno a) {
        return planEstudio.puedeCursar(a, m);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public java.util.List<Materia> getMaterias() {
        return planEstudio.getAllMaterias();
    }

    public PlanEstudio getPlan() {
        return planEstudio;
    }

    public void setCantidadOptativasNecesarias(int i) {
        this.cantidadOptativasNecesarias = i;
    }

    public int getCantidadOptativasNecesarias() {
        return cantidadOptativasNecesarias;
    }

    @Override
    public String toString() {
        return nombre + " (ID: " + id + ")";
    }
}
