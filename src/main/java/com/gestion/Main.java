package com.gestion;

import com.gestion.condiciones.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Verificación de Sistema de Gestión Académica ---");

        // 1. Crear Materias
        Materia m1 = new Materia("Programación I", "SYS-101");
        Materia m2 = new Materia("Programación II", "SYS-102");
        Materia m3 = new Materia("Bases de Datos", "SYS-103");

        System.out.println("\n1. Se han creado las materias.");

        // 2. Crear Plan de Estudios con Builder y Correlativas
        // Regla: Para Prog II se necesita Prog I (Condicion A - Cursada Aprobada)
        System.out.println("\n2. Configurando Plan de Estudios...");
        PlanDeEstudios plan = new PlanDeEstudiosBuilder("Ingeniería en Sistemas 2025")
                .agregarMateriaObligatoria(m1, 1, null) // Sin correlativas
                .agregarMateriaObligatoria(m2, 2, new CondicionInscripcionA()) // Requiere correlativas cursadas
                .agregarMateriaObligatoria(m3, 2, null)
                .agregarCorrelativa(m2, m1) // Prog II correla con Prog I
                .build();

        // 3. Crear Carrera asociado al Plan
        Carrera carrera = new Carrera("Ingeniería en Sistemas", plan);
        System.out.println("   Carrera creada: " + carrera);

        // 4. Crear Alumno e Inscribir a Carrera
        Alumno alumno = new Alumno("Juan", "Perez", 1001);
        alumno.agregarCarrera(carrera);
        System.out.println("\n3. Alumno inscripto: " + alumno);

        // 5. Simulación de Inscripciones

        // Intento 1: Inscribirse a Programación II (Debería fallar porque no cursó Prog
        // I)
        System.out.println("\n--- Intento de Inscripción a Programación II (Fallo esperado) ---");
        if (plan.puedeCursar(alumno, m2)) {
            alumno.inscribirMateria(m2);
            System.out.println("ERROR: Se permitió inscribirse a Prog II sin correlativas.");
        } else {
            System.out.println("ÉXITO VALIDACIÓN: No se puede cursar Prog II (Faltan correlativas).");
        }

        // Paso intermedio: Cursar y Aprobar Programación I
        System.out.println("\n--- Cursando Programación I... ---");
        alumno.inscribirMateria(m1);
        Cursada c1 = alumno.getInscripcion(m1);
        c1.setEstado(EstadoCursada.CURSADA_APROBADA);
        System.out.println("Estado de Prog I: " + c1.getEstado());

        // Intento 2: Inscribirse a Programación II (Debería funcionar ahora)
        System.out.println("\n--- Intento de Inscripción a Programación II (Éxito esperado) ---");
        if (plan.puedeCursar(alumno, m2)) {
            alumno.inscribirMateria(m2);
            System.out.println(
                    "INSCRIPCIÓN EXITOSA: Alumno cursando " + alumno.getInscripcion(m2).getMateria().getNombre());
        } else {
            System.out.println("ERROR: Se denegó inscripción válida.");
        }

        System.out.println("\n--- Fin de Demo Core Logic (60%) ---");
    }
}
