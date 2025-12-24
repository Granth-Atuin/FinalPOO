package com.gestion.view;

import com.gestion.controller.AlumnoController;
import com.gestion.model.Alumno;
import com.gestion.model.Carrera;
import com.gestion.model.Cursada;

import com.gestion.model.Materia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelInscripciones extends JPanel {
    private JComboBox<Alumno> comboAlumnos;
    private JComboBox<Carrera> comboCarreras;
    private JComboBox<Materia> comboMaterias;
    private DefaultTableModel modelHistorial;
    private JTable tablaHistorial;
    private AlumnoController alumnoController;

    public PanelInscripciones() {
        this.alumnoController = new AlumnoController();
        setLayout(new BorderLayout());

        // Panel Superior: Selectores
        JPanel panelSelectores = new JPanel(new GridLayout(3, 2, 5, 5));
        panelSelectores.setBorder(BorderFactory.createTitledBorder("Inscripción"));

        comboAlumnos = new JComboBox<>();
        comboCarreras = new JComboBox<>();
        comboMaterias = new JComboBox<>();

        JButton btnInscribir = new JButton("Inscribir");

        panelSelectores.add(new JLabel("Alumno:"));
        panelSelectores.add(comboAlumnos);
        panelSelectores.add(new JLabel("Carrera:"));
        panelSelectores.add(comboCarreras);
        panelSelectores.add(new JLabel("Materia a Inscribir:"));
        JPanel pnlMateria = new JPanel(new BorderLayout());
        pnlMateria.add(comboMaterias, BorderLayout.CENTER);
        pnlMateria.add(btnInscribir, BorderLayout.EAST);
        panelSelectores.add(pnlMateria);

        add(panelSelectores, BorderLayout.NORTH);

        // Panel Central: Historial / Estado
        String[] cols = { "Materia", "Estado", "Nota", "Fecha" };
        modelHistorial = new DefaultTableModel(cols, 0);
        tablaHistorial = new JTable(modelHistorial);
        add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        // Listeners
        comboAlumnos.addActionListener(e -> actualizarCarreras());
        comboCarreras.addActionListener(e -> actualizarMaterias());
        btnInscribir.addActionListener(e -> inscribir());

        cargarAlumnos();
    }

    private void cargarAlumnos() {
        comboAlumnos.removeAllItems();
        for (Alumno a : alumnoController.listarAlumnos()) {
            comboAlumnos.addItem(a);
        }
    }

    private void actualizarCarreras() {
        comboCarreras.removeAllItems();
        Alumno a = (Alumno) comboAlumnos.getSelectedItem();
        if (a != null) {
            for (Carrera c : a.getCarreras()) {
                comboCarreras.addItem(c);
            }
            actualizarHistorial(a);
        }
    }

    private void actualizarMaterias() {
        comboMaterias.removeAllItems();
        Carrera c = (Carrera) comboCarreras.getSelectedItem();
        Alumno a = (Alumno) comboAlumnos.getSelectedItem();
        if (c != null && a != null) {
            for (Materia m : c.getPlan().getAllMaterias()) {
                if (!a.tieneCursadaAprobada(m) && !a.estaCursando(m)) {
                    // Verificar correlativas
                    if (c.getPlan().puedeCursar(a, m)) {
                        comboMaterias.addItem(m);
                    }
                }
            }
        }
    }

    private void actualizarHistorial(Alumno a) {
        modelHistorial.setRowCount(0);
        if (a != null) {
            for (Cursada c : a.getHistoriaAcademica()) {
                Object[] row = {
                        c.getMateria().getNombre(),
                        c.getEstado(),
                        c.getNota() > 0 ? c.getNota() : "-", // Muestra nota o guión
                        "Hoy" // Fecha placeholder
                };
                modelHistorial.addRow(row);
            }
        }
    }

    private void inscribir() {
        Alumno a = (Alumno) comboAlumnos.getSelectedItem();
        Materia m = (Materia) comboMaterias.getSelectedItem();
        Carrera c = (Carrera) comboCarreras.getSelectedItem();

        if (a != null && m != null && c != null) {
            if (c.getPlan().puedeCursar(a, m)) {
                a.inscribirMateria(m);
                JOptionPane.showMessageDialog(this, "Inscripto correctamente a " + m.getNombre());
                actualizarHistorial(a); // Necesitamos implementar esto
            } else {
                JOptionPane.showMessageDialog(this, "No cumple correlativas para " + m.getNombre(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
