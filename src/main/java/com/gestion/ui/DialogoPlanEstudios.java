package com.gestion.ui;

import com.gestion.Carrera;

import com.gestion.Materia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class DialogoPlanEstudios extends JDialog {
    private Carrera carrera;
    private DefaultTableModel tableModel;

    public DialogoPlanEstudios(Frame owner, Carrera carrera) {
        super(owner, "Plan de Estudios: " + carrera.getNombre(), true);
        this.carrera = carrera;
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
        // Ordenar por cuatrimestre
        materias.sort(java.util.Comparator.comparingInt(Materia::getCuatrimestre));

        for (Materia m : materias) {
            // Correlativas ahora están en la Materia misma
            List<Materia> corrs = m.getCorrelativas();
            StringBuilder sb = new StringBuilder();
            for (Materia c : corrs)
                sb.append(c.getCodigo()).append(" ");

            tableModel.addRow(new Object[] {
                    m.getCuatrimestre(),
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
        List<Materia> candidatos = new ArrayList<>(carrera.getMaterias());
        candidatos.remove(materiaSeleccionada); // No puede ser correlativa de sí misma

        JList<Materia> listCandidatos = new JList<>(candidatos.toArray(new Materia[0]));
        listCandidatos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        int opt = JOptionPane.showConfirmDialog(this, new JScrollPane(listCandidatos),
                "Seleccione Correlativas Previas", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            List<Materia> seleccionadas = listCandidatos.getSelectedValuesList();
            // Limpiar previas y agregar nuevas
            // Nota: Materia.correlativas es una lista, podriamos hacer set si agregamos el
            // metodo, o limpiar
            // Como no agregué metodo 'set', hare loop.
            // Mejor agrego metodo setCorrelativas a Materia o uso el getter para limpiar.
            materiaSeleccionada.getCorrelativas().clear();
            for (Materia s : seleccionadas) {
                materiaSeleccionada.agregarCorrelativa(s);
            }
            cargarPlan();
        }
    }
}
