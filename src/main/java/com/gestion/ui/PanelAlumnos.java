package com.gestion.ui;

import com.gestion.Alumno;
import com.gestion.Carrera;
import com.gestion.Facultad;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelAlumnos extends JPanel {
    private DefaultTableModel tableModel;
    private Alumno alumnoEditar = null;

    public PanelAlumnos() {
        setLayout(new BorderLayout());

        // Título
        JLabel title = new JLabel("Gestión de Alumnos");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Tabla
        String[] columnNames = { "Legajo", "Nombre", "Apellido", "Carreras" };
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar Alumno");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");

        btnAgregar.addActionListener(e -> mostrarDialogoAlumno(null));
        btnModificar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int legajo = (int) tableModel.getValueAt(selectedRow, 0);
                Alumno a = Facultad.getInstance().buscarAlumnoPorLegajo(legajo);
                if (a != null) {
                    mostrarDialogoAlumno(a);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un alumno.");
            }
        });
        btnEliminar.addActionListener(e -> eliminarAlumno());
        btnRefrescar.addActionListener(e -> refrescarTabla());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        add(panelBotones, BorderLayout.SOUTH);

        // Cargar datos iniciales
        refrescarTabla();
    }

    private void refrescarTabla() {
        tableModel.setRowCount(0); // Limpiar
        for (Alumno a : Facultad.getInstance().getAlumnos()) {
            StringBuilder carrerasStr = new StringBuilder();
            for (Carrera c : a.getCarrerasInscriptas()) {
                carrerasStr.append(c.getNombre()).append(", ");
            }
            if (carrerasStr.length() > 2)
                carrerasStr.setLength(carrerasStr.length() - 2);

            Object[] row = {
                    a.getLegajo(),
                    a.getNombre(),
                    a.getApellido(),
                    carrerasStr.toString()
            };
            tableModel.addRow(row);
        }
    }

    private void mostrarDialogoAlumno(Alumno aEditar) {
        this.alumnoEditar = aEditar;
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                aEditar == null ? "Agregar Alumno" : "Editar Alumno", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JTextField txtNombre = new JTextField(alumnoEditar != null ? alumnoEditar.getNombre() : "");
        JTextField txtApellido = new JTextField(alumnoEditar != null ? alumnoEditar.getApellido() : "");
        JTextField txtLegajo = new JTextField(alumnoEditar != null ? String.valueOf(alumnoEditar.getLegajo()) : "");

        JPanel pnlInputs = new JPanel(new GridBagLayout());
        pnlInputs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        pnlInputs.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        pnlInputs.add(txtNombre, gbc);

        // Apellido
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        pnlInputs.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        pnlInputs.add(txtApellido, gbc);

        // Legajo
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        pnlInputs.add(new JLabel("Legajo:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        pnlInputs.add(txtLegajo, gbc);

        // Selector de Carreras (CheckBoxList)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        pnlInputs.add(new JLabel("Inscribir en Carreras:"), gbc);

        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        CheckBoxListPanel<Carrera> pnlCarreras = new CheckBoxListPanel<>(Facultad.getInstance().getCarreras());
        pnlInputs.add(new JScrollPane(pnlCarreras), gbc);

        dialog.add(pnlInputs, BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBotones.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String apellido = txtApellido.getText();
                int legajo = Integer.parseInt(txtLegajo.getText());

                if (alumnoEditar == null) {
                    // Alta
                    Alumno nuevo = new Alumno(nombre, apellido, legajo);
                    Facultad.getInstance().agregarAlumno(nuevo);
                    // Inscribir a carreras seleccionadas
                    for (Carrera c : pnlCarreras.getSelectedItems()) {
                        nuevo.agregarCarrera(c);
                        c.inscribirAlumno(nuevo);
                    }
                } else {
                    // Modificación
                    alumnoEditar.setNombre(nombre);
                    alumnoEditar.setApellido(apellido);
                    alumnoEditar.setLegajo(legajo);
                    // Nota: No gestionamos cambio de carreras en edición simple por ahora,
                    // o podríamos agregar lógica para agregar nuevas.
                    for (Carrera c : pnlCarreras.getSelectedItems()) {
                        if (!alumnoEditar.getCarreras().contains(c)) {
                            alumnoEditar.agregarCarrera(c);
                            c.inscribirAlumno(alumnoEditar);
                        }
                    }
                }
                refrescarTabla();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Legajo debe ser numérico.");
            }
        });

        pnlBotones.add(btnGuardar);
        dialog.add(pnlBotones, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void eliminarAlumno() {
        int selectedRow = ((JTable) ((JScrollPane) getComponent(1)).getViewport().getView()).getSelectedRow();
        if (selectedRow >= 0) {
            int legajo = (int) tableModel.getValueAt(selectedRow, 0);
            Alumno a = Facultad.getInstance().buscarAlumnoPorLegajo(legajo);
            if (a != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro eliminar a " + a.getNombre() + "?",
                        "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Facultad.getInstance().eliminarAlumno(a);
                    refrescarTabla();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno para eliminar.");
        }
    }
}
