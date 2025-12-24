package com.gestion.model;

import java.util.ArrayList;
import java.util.List;

public class Facultad {
    private static Facultad instance;
    private List<Carrera> carreras;
    private List<Alumno> alumnos;
    private List<Materia> materiasGlobales; // Para el catálogo global
    private int ultimoLegajo = 1000;

    private Facultad() {
        carreras = new ArrayList<>();
        alumnos = new ArrayList<>();
        materiasGlobales = new ArrayList<>();
    }

    public int generarNuevoLegajo() {
        return ++ultimoLegajo;
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

    public void cargarDatosIniciales() {
        // Evitar duplicar si ya hay datos
        if (!carreras.isEmpty() || !alumnos.isEmpty())
            return;

        // --- DEFINICIÓN DE MATERIAS ---

        // 1er Año - 1er Cuatrimestre
        Materia m_ElemInfo = new Materia("Elementos de Informática", "IF001");
        Materia m_ExpProb = new Materia("Expresión de Problemas y Algoritmos", "IF002");
        Materia m_Algebra = new Materia("Álgebra", "MA045");

        // 1er Año - 2do Cuatrimestre
        Materia m_Algo1 = new Materia("Algorítmica y Programación I", "IF003");
        Materia m_ElemLogica = new Materia("Elementos de Lógica y Matemática Discreta", "MA008");
        Materia m_AnalisisMat = new Materia("Análisis Matemático", "MA048");

        // 2do Año - 3er Cuatrimestre
        Materia m_SistOrg = new Materia("Sistemas y Organizaciones", "IF004");
        Materia m_ArqComp = new Materia("Arquitectura de Computadoras", "IF005");
        Materia m_Algo2 = new Materia("Algorítmica y Programación II", "IF006");
        Materia m_Estadistica = new Materia("Estadística", "MA009");

        // 2do Año - 4to Cuatrimestre
        Materia m_BD1 = new Materia("Bases de Datos I", "IF007");
        Materia m_PDOO = new Materia("Programación y Diseño Orientado a Objetos", "IF008");
        Materia m_IngSoft1 = new Materia("Ingeniería de Software I", "IF009");

        // 3er Año - 5to Cuatrimestre
        Materia m_LabProg = new Materia("Laboratorio de Programación y Lenguajes", "IF010");
        Materia m_IngSoft2 = new Materia("Ingeniería de Software II", "IF011");
        Materia m_IntroConc = new Materia("Introducción a la Concurrencia", "IF012");
        Materia m_FundTeoricos = new Materia("Fundamentos Teóricos de Informática", "IF013");

        // 3er Año - 6to Cuatrimestre
        Materia m_SistOp = new Materia("Sistemas Operativos", "IF014");
        Materia m_LabSoft = new Materia("Laboratorio de Software", "IF015");
        Materia m_BD2 = new Materia("Bases de Datos II", "IF016");
        Materia m_SemLegal1 = new Materia("Seminario de Aspectos Legales y Profesionales I", "IF017");

        // 4to Año - 7mo Cuatrimestre
        Materia m_Redes = new Materia("Redes y Transmisión de Datos", "IF018");
        Materia m_Paradigmas = new Materia("Paradigmas y Lenguajes de Programación", "IF019");
        Materia m_TallerNuevas = new Materia("Taller de Nuevas Tecnologías", "IF020");

        // 4to Año - 8vo Cuatrimestre
        Materia m_SistDist = new Materia("Sistemas Distribuidos", "IF021");
        Materia m_IngSoft3 = new Materia("Ingeniería de Software III", "IF022");
        Materia m_SemLegal2 = new Materia("Seminario de Aspectos Legales y Profesionales II", "IF023");
        Materia m_SistIntel = new Materia("Sistemas Inteligentes", "IF024");

        // 5to Año - 9no Cuatrimestre
        Materia m_TiempoReal = new Materia("Sistemas de Tiempo Real", "IF025");
        Materia m_SistParalelos = new Materia("Sistemas Paralelos", "IF026");
        Materia m_BD_Dist = new Materia("Bases de Datos Distribuidas", "IF027");
        Materia m_SemSeguridad = new Materia("Seminario de Seguridad", "IF028");

        // 5to Año - 10mo Cuatrimestre
        Materia m_ModSimulacion = new Materia("Modelos y Simulación", "IF029");
        Materia m_ProySoftware = new Materia("Proyecto de Software", "IF030");

        // Agregar todas al catálogo global
        agregarMateria(m_ElemInfo);
        agregarMateria(m_ExpProb);
        agregarMateria(m_Algebra);
        agregarMateria(m_Algo1);
        agregarMateria(m_ElemLogica);
        agregarMateria(m_AnalisisMat);
        agregarMateria(m_SistOrg);
        agregarMateria(m_ArqComp);
        agregarMateria(m_Algo2);
        agregarMateria(m_Estadistica);
        agregarMateria(m_BD1);
        agregarMateria(m_PDOO);
        agregarMateria(m_IngSoft1);
        agregarMateria(m_LabProg);
        agregarMateria(m_IngSoft2);
        agregarMateria(m_IntroConc);
        agregarMateria(m_FundTeoricos);
        agregarMateria(m_SistOp);
        agregarMateria(m_LabSoft);
        agregarMateria(m_BD2);
        agregarMateria(m_SemLegal1);
        agregarMateria(m_Redes);
        agregarMateria(m_Paradigmas);
        agregarMateria(m_TallerNuevas);
        agregarMateria(m_SistDist);
        agregarMateria(m_IngSoft3);
        agregarMateria(m_SemLegal2);
        agregarMateria(m_SistIntel);
        agregarMateria(m_TiempoReal);
        agregarMateria(m_SistParalelos);
        agregarMateria(m_BD_Dist);
        agregarMateria(m_SemSeguridad);
        agregarMateria(m_ModSimulacion);
        agregarMateria(m_ProySoftware);

        // --- CREACIÓN DEL PLAN DE ESTUDIOS ---

        PlanDeEstudios planSist = new PlanDeEstudios("Licenciatura en Sistemas (Plan 2015)");

        // Asignación de Materias y Cuatrimestres
        // Año 1
        planSist.agregarMateriaObligatoria(m_ElemInfo, 1);
        planSist.agregarMateriaObligatoria(m_ExpProb, 1);
        planSist.agregarMateriaObligatoria(m_Algebra, 1);
        planSist.agregarMateriaObligatoria(m_Algo1, 2);
        planSist.agregarMateriaObligatoria(m_ElemLogica, 2);
        planSist.agregarMateriaObligatoria(m_AnalisisMat, 2);

        // Año 2
        planSist.agregarMateriaObligatoria(m_SistOrg, 3);
        planSist.agregarMateriaObligatoria(m_ArqComp, 3);
        planSist.agregarMateriaObligatoria(m_Algo2, 3);
        planSist.agregarMateriaObligatoria(m_Estadistica, 3);
        planSist.agregarMateriaObligatoria(m_BD1, 4);
        planSist.agregarMateriaObligatoria(m_PDOO, 4);
        planSist.agregarMateriaObligatoria(m_IngSoft1, 4);

        // Año 3
        planSist.agregarMateriaObligatoria(m_LabProg, 5);
        planSist.agregarMateriaObligatoria(m_IngSoft2, 5);
        planSist.agregarMateriaObligatoria(m_IntroConc, 5);
        planSist.agregarMateriaObligatoria(m_FundTeoricos, 5);
        planSist.agregarMateriaObligatoria(m_SistOp, 6);
        planSist.agregarMateriaObligatoria(m_LabSoft, 6);
        planSist.agregarMateriaObligatoria(m_BD2, 6);
        planSist.agregarMateriaObligatoria(m_SemLegal1, 6);

        // Año 4
        planSist.agregarMateriaObligatoria(m_Redes, 7);
        planSist.agregarMateriaObligatoria(m_Paradigmas, 7);
        planSist.agregarMateriaObligatoria(m_TallerNuevas, 7);
        planSist.agregarMateriaObligatoria(m_SistDist, 8);
        planSist.agregarMateriaObligatoria(m_IngSoft3, 8);
        planSist.agregarMateriaObligatoria(m_SemLegal2, 8);
        planSist.agregarMateriaObligatoria(m_SistIntel, 8);

        // Año 5
        planSist.agregarMateriaObligatoria(m_TiempoReal, 9);
        planSist.agregarMateriaObligatoria(m_SistParalelos, 9);
        planSist.agregarMateriaObligatoria(m_BD_Dist, 9);
        planSist.agregarMateriaObligatoria(m_SemSeguridad, 9);
        planSist.agregarMateriaObligatoria(m_ModSimulacion, 10);
        planSist.agregarMateriaObligatoria(m_ProySoftware, 10);

        // --- CORRELATIVAS ---
        // Helper para crear listas rápido
        // (Nota: Las correlativas "Standard" implican tener la cursada APROBADA de la
        // correlativa)

        // 2do Cuatri
        // Algorítmica I -> Expresión
        planSist.setCorrelativas(m_Algo1, java.util.List.of(m_ExpProb));

        // 3er Cuatri
        // Arq. de Computadoras -> Elementos de Informática
        planSist.setCorrelativas(m_ArqComp, java.util.List.of(m_ElemInfo));
        // Algorítmica II -> Algorítmica I, Elem. Lógica
        planSist.setCorrelativas(m_Algo2, java.util.List.of(m_Algo1, m_ElemLogica));
        // Estadística -> Álgebra, Análisis Matemático
        planSist.setCorrelativas(m_Estadistica, java.util.List.of(m_Algebra, m_AnalisisMat));

        // 4to Cuatri
        // BD I -> Algorítmica II
        planSist.setCorrelativas(m_BD1, java.util.List.of(m_Algo2));
        // PDOO -> Algorítmica II
        planSist.setCorrelativas(m_PDOO, java.util.List.of(m_Algo2));
        // Ing. Software I -> Algorítmica I
        planSist.setCorrelativas(m_IngSoft1, java.util.List.of(m_Algo1));

        // 5to Cuatri
        // Lab. Prog. y Lenguajes -> Algorítmica II
        planSist.setCorrelativas(m_LabProg, java.util.List.of(m_Algo2));
        // Ing. Software II -> Ing. Software I, Estadística
        planSist.setCorrelativas(m_IngSoft2, java.util.List.of(m_IngSoft1, m_Estadistica));
        // Intro a la Concurrencia -> Arq. Computadoras, Algorítmica II
        planSist.setCorrelativas(m_IntroConc, java.util.List.of(m_ArqComp, m_Algo2));
        // Fundamentos Teóricos -> Algorítmica II, Elem. Lógica
        planSist.setCorrelativas(m_FundTeoricos, java.util.List.of(m_Algo2, m_ElemLogica));

        // 6to Cuatri
        // Sist. Operativos -> Intro Concurrencia
        planSist.setCorrelativas(m_SistOp, java.util.List.of(m_IntroConc));
        // Lab. Software -> BD I, PDOO, Ing. Soft I
        planSist.setCorrelativas(m_LabSoft, java.util.List.of(m_BD1, m_PDOO, m_IngSoft1));
        // BD II -> BD I
        planSist.setCorrelativas(m_BD2, java.util.List.of(m_BD1));
        // Sem. Legal I -> Sist. y Organizaciones
        planSist.setCorrelativas(m_SemLegal1, java.util.List.of(m_SistOrg));

        // 7mo Cuatri
        // Redes -> Sist. Operativos
        planSist.setCorrelativas(m_Redes, java.util.List.of(m_SistOp));
        // Paradigmas -> Fundamentos Teoricos, PDOO
        planSist.setCorrelativas(m_Paradigmas, java.util.List.of(m_FundTeoricos, m_PDOO));
        // Taller Nuevas Tec -> Lab. Software
        planSist.setCorrelativas(m_TallerNuevas, java.util.List.of(m_LabSoft));

        // 8vo Cuatri
        // Sist. Distribuidos -> Redes
        planSist.setCorrelativas(m_SistDist, java.util.List.of(m_Redes));
        // Ing. Soft III -> Ing. Soft II
        planSist.setCorrelativas(m_IngSoft3, java.util.List.of(m_IngSoft2));
        // Sem. Legal II -> Sem. Legal I
        planSist.setCorrelativas(m_SemLegal2, java.util.List.of(m_SemLegal1));
        // Sist. Inteligentes -> Fundamentos Teoricos
        planSist.setCorrelativas(m_SistIntel, java.util.List.of(m_FundTeoricos));

        // 9no Cuatri
        // Sist. Tiempo Real -> Sist. Operativos
        planSist.setCorrelativas(m_TiempoReal, java.util.List.of(m_SistOp));
        // Sist. Paralelos -> Lab. Prog y Lenguajes, Sist. Distribuidos
        planSist.setCorrelativas(m_SistParalelos, java.util.List.of(m_LabProg, m_SistDist));
        // BD Distribuidas -> BD II, Redes
        planSist.setCorrelativas(m_BD_Dist, java.util.List.of(m_BD2, m_Redes));
        // Sem. Seguridad -> Redes
        planSist.setCorrelativas(m_SemSeguridad, java.util.List.of(m_Redes));

        // 10mo Cuatri
        // Modelos y Simulación -> Estadística, Paradigmas
        planSist.setCorrelativas(m_ModSimulacion, java.util.List.of(m_Estadistica, m_Paradigmas));
        // Proyecto de Software -> Ing. Soft III
        planSist.setCorrelativas(m_ProySoftware, java.util.List.of(m_IngSoft3));

        // --- FINALIZACIÓN ---

        // Carrera
        Carrera c1 = new Carrera("Licenciatura en Sistemas", planSist);
        agregarCarrera(c1);

        // Alumnos
        Alumno a1 = new Alumno("Usuario", "Demo", 1001);
        a1.agregarCarrera(c1);
        agregarAlumno(a1);

        // Inicializar contadores
        secuenciasMaterias.put(c1.getId(), 100);
    }
}
