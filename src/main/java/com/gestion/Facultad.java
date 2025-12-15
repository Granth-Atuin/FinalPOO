package com.gestion;

import java.util.ArrayList;
import java.util.List;

public class Facultad {
    private static Facultad instance;
    private List<Carrera> carreras;
    private List<Alumno> alumnos;
    private List<Materia> materiasGlobales; // Para el catálogo global

    private Facultad() {
        carreras = new ArrayList<>();
        alumnos = new ArrayList<>();
        materiasGlobales = new ArrayList<>();
    }

    public static Facultad getInstance() {
        if (instance == null) {
            instance = new Facultad();
        }
        return instance;
    }

    public void agregarCarrera(Carrera c) {
        carreras.add(c);
    }

    public void agregarAlumno(Alumno a) {
        alumnos.add(a);
    }

    public void eliminarAlumno(Alumno a) {
        alumnos.remove(a);
    }

    public void agregarMateria(Materia m) {
        materiasGlobales.add(m);
    }

    public List<Carrera> getCarreras() {
        return carreras;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public List<Materia> getMaterias() {
        return materiasGlobales;
    }

    public Alumno buscarAlumnoPorLegajo(int legajo) {
        return alumnos.stream().filter(a -> a.getLegajo() == legajo).findFirst().orElse(null);
    }

    public boolean existeMateria(String codigo) {
        return materiasGlobales.stream().anyMatch(m -> m.getCodigo().equalsIgnoreCase(codigo));
    }

    // --- Gestión de materias global ---
    public void agregarMateriaGlobal(Materia materia) {
        if (!materiasGlobales.contains(materia)) {
            materiasGlobales.add(materia);
        }
    }

    // --- Secuencias para códigos automáticos ---
    private java.util.Map<Integer, Integer> secuenciasMaterias = new java.util.HashMap<>();

    public String generarProximoCodigoMateria(int carreraId) {
        int nextNum = secuenciasMaterias.getOrDefault(carreraId, 0) + 1;
        secuenciasMaterias.put(carreraId, nextNum);
        return carreraId + "-" + nextNum;
    }

    public void cargarDatosFake() {
        // Evitar duplicar si ya hay datos
        if (!carreras.isEmpty() || !alumnos.isEmpty())
            return;

        // Datos de UNTDF - Licenciatura en Sistemas (1er Año)

        // 1er Cuatrimestre
        Materia m1 = new Materia("Elementos de informática", "IF001");
        Materia m2 = new Materia("Álgebra", "MA045");
        Materia m3 = new Materia("Expresión de Problemas y algoritmos", "IF002");

        // 2do Cuatrimestre
        Materia m4 = new Materia("Algorítmica y Programación I", "IF003");
        Materia m5 = new Materia("Análisis Matemático - S", "MA048");
        Materia m6 = new Materia("Elementos de Lógica y Matemática Discreta", "MA008");

        agregarMateria(m1);
        agregarMateria(m2);
        agregarMateria(m3);
        agregarMateria(m4);
        agregarMateria(m5);
        agregarMateria(m6);

        // Simulación: 'Elementos de informática' equivale a 'Intro a la Computación'
        // (Ficticia)
        Materia mEquiv = new Materia("Intro a la Computación (Plan Viejo)", "OLD-001");
        agregarMateria(mEquiv);
        m1.agregarEquivalencia(mEquiv);

        // Plan UNTDF
        PlanDeEstudios planSist = new PlanDeEstudios("Licenciatura en Sistemas (Plan 2015)");
        // 1er Cuatri
        planSist.agregarMateriaObligatoria(m1, 1);
        planSist.agregarMateriaObligatoria(m2, 1);
        planSist.agregarMateriaObligatoria(m3, 1);
        // 2do Cuatri
        planSist.agregarMateriaObligatoria(m4, 2);
        planSist.agregarMateriaObligatoria(m5, 2);
        planSist.agregarMateriaObligatoria(m6, 2);

        // Correlativas: Algorítmica y Programación I (IF003) requiere Expresión (IF002)
        java.util.List<Materia> corralesM4 = new java.util.ArrayList<>();
        corralesM4.add(m3);
        planSist.setCorrelativas(m4, corralesM4);

        // Carrera
        Carrera c1 = new Carrera("Licenciatura en Sistemas", planSist);
        agregarCarrera(c1);

        // Alumnos
        Alumno a1 = new Alumno("Usuario", "Demo", 1001);
        a1.agregarCarrera(c1);

        agregarAlumno(a1);

        // Inicializar contadores para evitar colisiones si el usuario crea cosas
        secuenciasMaterias.put(c1.getId(), 100);
    }
}
