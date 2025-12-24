package com.gestion.view;

import com.gestion.controller.CarreraController;
import com.gestion.model.Carrera;
import com.gestion.model.Materia;
import com.gestion.model.PlanDeEstudios;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelCarreras extends JPanel {
    private DefaultTableModel tableModel;
    private CarreraController controller;

    public PanelCarreras() {
        this.controller = new CarreraController();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Gestión de Carreras");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        String[] columnNames = { "Nombre", "Plan de Estudios", "Materias", "Optativas Graduación" };
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Botón Refrescar
        JButton btnNuevaMateria = new JButton("Nueva Materia");
        JButton btnNuevaCarrera = new JButton("Nueva Carrera");
        JButton btnCatalogo = new JButton("Catálogo Materias");
        JButton btnVerPlan = new JButton("Ver Plan / Correlativas");
        JButton btnRefrescar = new JButton("Refrescar");

        btnNuevaMateria.addActionListener(e -> agregarMateria());
        btnNuevaCarrera.addActionListener(e -> agregarCarrera());
        btnCatalogo.addActionListener(
                e -> new DialogoCatalogoMaterias((Frame) SwingUtilities.getWindowAncestor(this), controller)
                        .setVisible(true));
        btnVerPlan.addActionListener(
                e -> verPlanSeleccionado((JTable) ((JScrollPane) getComponent(1)).getViewport().getView()));
        btnRefrescar.addActionListener(e -> refrescarTabla());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnNuevaMateria);
        panelBotones.add(btnNuevaCarrera);
        panelBotones.add(btnCatalogo);
        panelBotones.add(btnVerPlan);
        panelBotones.add(btnRefrescar);

        add(panelBotones, BorderLayout.SOUTH);

        refrescarTabla();
    }

    private void refrescarTabla() {
        tableModel.setRowCount(0);
        for (Carrera c : controller.listarCarreras()) {
            // PlanEstudio es interfaz, casteamos a PlanBasico si necesitamos nombre o
            // usamos nombre generico
            String nombrePlan = c.getPlan().getNombre();
            Object[] row = {
                    c.getNombre(),
                    nombrePlan,
                    c.getMaterias().size() + " materias",
                    c.getCantidadOptativasNecesarias()
            };
            tableModel.addRow(row);
        }
    }

    private void agregarMateria() {
        JTextField txtNombre = new JTextField();
        JComboBox<Carrera> cmbCarreras = new JComboBox<>();

        for (Carrera c : controller.listarCarreras()) {
            cmbCarreras.addItem(c);
        }

        // Panel personalizado en lugar de Object array para controlar layout
        JPanel pnl = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnl.add(new JLabel("Nombre Materia:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnl.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pnl.add(new JLabel("Pertenece a Carrera (para Código):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnl.add(cmbCarreras, gbc);

        pnl.setPreferredSize(new Dimension(350, 120));

        int option = JOptionPane.showConfirmDialog(this, pnl, "Nueva Materia", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            Carrera carreraSeleccionada = (Carrera) cmbCarreras.getSelectedItem();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar el nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (carreraSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Debe existir al menos una carrera para asignar el código.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String codigo = controller.generarProximoCodigoMateria(carreraSeleccionada.getId());
            Materia m = new Materia(nombre, codigo);
            // Agregar a global
            controller.agregarMateria(m);
            // Agregar a la carrera (Ahora la carrera tiene la lista)
            carreraSeleccionada.agregarMateria(m);

            JOptionPane.showMessageDialog(this, "Materia creada y asignada a " + carreraSeleccionada.getNombre());
        }
    }

    private void agregarCarrera() {
        JTextField txtNombre = new JTextField();
        // Selección con CheckBoxList
        java.util.List<Materia> todas = controller.getMaterias();
        CheckBoxListPanel<Materia> listaMaterias = new CheckBoxListPanel<>(todas);

        JPanel pnl = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnl.add(new JLabel("Nombre Carrera:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnl.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.gridwidth = 2;
        pnl.add(new JLabel("Seleccionar Materias (Plan Inicial):"), gbc);

        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        pnl.add(new JScrollPane(listaMaterias), gbc);

        pnl.setPreferredSize(new Dimension(350, 300));

        int option = JOptionPane.showConfirmDialog(this, pnl, "Nueva Carrera", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText();
            if (!nombre.isEmpty()) {
                // Crear Plan Estrategia
                PlanDeEstudios plan = new PlanDeEstudios("Plan " + nombre);
                Carrera nueva = new Carrera(nombre, plan);

                // Agregar materias seleccionadas a la carrera
                for (Materia m : listaMaterias.getSelectedItems()) {
                    nueva.agregarMateria(m);
                }

                controller.agregarCarrera(nueva);
                refrescarTabla();
            }
        }
    }

    private void verPlanSeleccionado(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una carrera de la tabla.");
            return;
        }

        String nombreCarrera = (String) table.getValueAt(row, 0);
        Carrera carrera = controller.listarCarreras().stream()
                .filter(c -> c.getNombre().equals(nombreCarrera)).findFirst().orElse(null);

        if (carrera != null) {
            new DialogoPlanEstudios((Frame) SwingUtilities.getWindowAncestor(this), carrera, controller)
                    .setVisible(true);
        }
    }
}
