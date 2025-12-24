package com.gestion.view;

import com.gestion.controller.CarreraController;
import com.gestion.model.Carrera;
import com.gestion.model.Materia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DialogoPlanEstudios extends JDialog {
    private Carrera carrera;
    private DefaultTableModel tableModel;
    private CarreraController controller;

    public DialogoPlanEstudios(Frame owner, Carrera carrera, CarreraController controller) {
        super(owner, "Plan de Estudios: " + carrera.getNombre(), true);
        this.carrera = carrera;
        this.controller = controller;
        setSize(700, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // Tabla
        String[] cols = { "Cuatrimestre", "Código", "Materia", "Correlativas", "Obligatoria" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);

        // Listener para doble clic
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    int col = table.getSelectedColumn();
                    if (row != -1 && col != -1) {
                        Object data = table.getValueAt(row, col);
                        if (data != null && !data.toString().isEmpty()) {
                            JOptionPane.showMessageDialog(DialogoPlanEstudios.this,
                                    data.toString(),
                                    "Detalle de " + table.getColumnName(col),
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnCorrelativas = new JButton("Asignar Correlativa");
        JButton btnCerrar = new JButton("Cerrar");

        btnCorrelativas.addActionListener(e -> asignarCorrelativa(table));
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnCorrelativas);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        cargarPlan();
    }

    private void cargarPlan() {
        tableModel.setRowCount(0);
        // Ahora las materias están en la Carrera
        List<Materia> materias = carrera.getMaterias();
        // Ordenar por cuatrimestre usando el Plan
        materias.sort((m1, m2) -> Integer.compare(carrera.getPlan().getCuatrimestre(m1),
                carrera.getPlan().getCuatrimestre(m2)));

        for (Materia m : materias) {
            // Correlativas ahora se obtienen del Plan
            List<Materia> corrs = carrera.getPlan().getCorrelativas(m);
            StringBuilder sb = new StringBuilder();
            for (Materia c : corrs)
                sb.append(c.getNombre()).append(", "); // Mostrar Nombre
            if (sb.length() > 2)
                sb.setLength(sb.length() - 2);

            tableModel.addRow(new Object[] {
                    carrera.getPlan().getCuatrimestre(m), // Obtener del Plan
                    m.getCodigo(),
                    m.getNombre(),
                    sb.toString(),
                    m.esObligatoria() ? "Sí" : "No"
            });
        }
    }

    private void asignarCorrelativa(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una materia del plan.");
            return;
        }

        String codigo = (String) table.getValueAt(row, 1);
        Materia materiaSeleccionada = carrera.getMaterias().stream()
                .filter(m -> m.getCodigo().equals(codigo)).findFirst().orElse(null);

        if (materiaSeleccionada == null)
            return;

        // Mostrar lista de materias CANDIDATAS (deben ser de la misma carrera)
        java.util.List<Materia> candidatos = new ArrayList<>(carrera.getMaterias());
        candidatos.remove(materiaSeleccionada); // No puede ser correlativa de sí misma

        JList<Materia> listCandidatos = new JList<>(candidatos.toArray(new Materia[0]));
        listCandidatos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        int opt = JOptionPane.showConfirmDialog(this, new JScrollPane(listCandidatos),
                "Seleccione Correlativas Previas", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            java.util.List<Materia> seleccionadas = listCandidatos.getSelectedValuesList();

            // Usar controlador
            controller.asignarCorrelativas(carrera.getPlan(), materiaSeleccionada, seleccionadas);

            cargarPlan();
        }
    }
}
