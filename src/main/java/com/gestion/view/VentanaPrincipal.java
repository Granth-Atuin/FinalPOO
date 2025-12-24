package com.gestion.view;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión Académica - TP 2025");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializar Menu
        initMenu();

        // Panel de Bienvenida simple
        JLabel welcomeLabel = new JLabel("Bienvenido al Sistema de Gestión Académica", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.CENTER);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();

        // 1. Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);

        // 2. Menú Alumnos
        JMenu menuAlumnos = new JMenu("Alumnos");
        JMenuItem itemGestionarAlumnos = new JMenuItem("Gestión de Alumnos");
        itemGestionarAlumnos.addActionListener(e -> mostrarPanelAlumnos());
        menuAlumnos.add(itemGestionarAlumnos);

        // 3. Menú Carreras
        JMenu menuCarreras = new JMenu("Carreras");
        JMenuItem itemGestionarCarreras = new JMenuItem("Ver Carreras y Planes");
        itemGestionarCarreras.addActionListener(e -> mostrarPanelCarreras());
        menuCarreras.add(itemGestionarCarreras);

        // 4. Menú Inscripciones
        JMenu menuInscripciones = new JMenu("Inscripciones");
        JMenuItem itemInscribir = new JMenuItem("Inscribir a Materia");
        itemInscribir.addActionListener(e -> mostrarPanelInscripcion());
        menuInscripciones.add(itemInscribir);

        menuBar.add(menuArchivo);
        menuBar.add(menuAlumnos);
        menuBar.add(menuCarreras);
        menuBar.add(menuInscripciones);

        // 5. Menú Materias (Nuevo)
        JMenu menuMaterias = new JMenu("Materias");
        JMenuItem itemGestionarMaterias = new JMenuItem("Gestión de Materias");
        itemGestionarMaterias.addActionListener(e -> mostrarPanelMaterias());
        menuMaterias.add(itemGestionarMaterias);
        menuBar.add(menuMaterias);

        setJMenuBar(menuBar);
    }

    private void mostrarPanelAlumnos() {
        setContentPane(new PanelAlumnos());
        revalidate();
    }

    private void mostrarPanelCarreras() {
        setContentPane(new PanelCarreras());
        revalidate();
    }

    private void mostrarPanelInscripcion() {
        setContentPane(new PanelInscripciones());
        revalidate();
    }

    private void mostrarPanelMaterias() {
        setContentPane(new PanelMaterias());
        revalidate();
    }

    public static void main(String[] args) {
        // Cargar datos de prueba para ejecución aislada
        com.gestion.model.Facultad.getInstance().cargarDatosIniciales();

        System.out.println("Iniciando interfaz gráfica...");
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
