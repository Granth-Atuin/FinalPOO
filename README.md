# Sistema de gestión académica

Aplicación de escritorio en Java para administrar carreras, planes de estudio, materias y alumnos, con inscripción a materias que controla las correlatividades. Trabajo final de Programación Orientada a Objetos (Tecnicatura en Desarrollo Web y Aplicaciones, UNTDF).

## Funcionalidades

- Alta y listado de alumnos, carreras y materias.
- Armado de planes de estudio a partir de un catálogo de materias.
- Inscripción de alumnos a materias, validando que cumplan los requisitos del plan.
- Seguimiento del estado de cada cursada: inscripto, parcial aprobado o desaprobado, cursada aprobada, promocionada y final aprobado.
- Interfaz gráfica con pestañas, tablas filtrables y diálogos.

## Diseño

- **MVC:** el modelo (`model`), las vistas en Swing (`view`) y los controladores (`controller`) están separados.
- **Strategy:** cada materia de un plan tiene su propia regla de inscripción (`CondicionInscripcionA` a `CondicionInscripcionE`), todas detrás de la misma interfaz `CondicionInscripcion`. Así se puede agregar una regla nueva sin tocar el resto del sistema.
- **Builder:** los planes de estudio se arman con `PlanDeEstudiosBuilder`.

## Tecnologías

- Java 17 o superior
- Swing para la interfaz gráfica

## Cómo correrlo

Desde la raíz del proyecto:

```bash
mkdir -p out
javac -d out $(find src/main/java -name "*.java")
java -cp out com.gestion.Main
```

También se puede abrir en IntelliJ IDEA, NetBeans o Eclipse y ejecutar la clase `com.gestion.Main`.

## Estructura

```
src/main/java/com/gestion/
├── Main.java
├── model/          Alumno, Carrera, Materia, Cursada, PlanDeEstudios, Facultad
│   └── condiciones/  Reglas de inscripción (patrón Strategy)
├── view/           Ventana principal, paneles y diálogos en Swing
└── controller/     Controladores de alumnos y carreras
```

## Autor

Juan Pablo Ocampo · [GitHub](https://github.com/Granth-Atuin)
