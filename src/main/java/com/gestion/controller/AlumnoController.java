package com.gestion.controller;

import com.gestion.model.Alumno;
import com.gestion.model.Carrera;
import com.gestion.model.Facultad;
import java.util.List;

public class AlumnoController {

    private Facultad facultad;

    public AlumnoController() {
        this.facultad = Facultad.getInstance(); // Access Model
    }

    public List<Alumno> listarAlumnos() {
        return facultad.getAlumnos();
    }

    public void crearAlumno(String nombre, String apellido) {
        int legajo = facultad.generarNuevoLegajo();
        Alumno nuevo = new Alumno(nombre, apellido, legajo);
        facultad.agregarAlumno(nuevo);
    }

    public void agregarAlumno(Alumno alumno) {
        facultad.agregarAlumno(alumno);
    }

    public Alumno buscarAlumnoPorLegajo(int legajo) {
        return facultad.buscarAlumnoPorLegajo(legajo);
    }

    public int generarNuevoLegajo() {
        return facultad.generarNuevoLegajo();
    }

    public void eliminarAlumno(Alumno alumno) {
        facultad.eliminarAlumno(alumno);
    }

    public void inscribirACarrera(Alumno alumno, Carrera carrera) {
        alumno.agregarCarrera(carrera);
        carrera.inscribirAlumno(alumno);
        // Persistencia si fuera necesaria se manejaria aqui o en Facultad
    }
}
