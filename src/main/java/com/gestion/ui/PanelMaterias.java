package com.gestion.ui;

import com.gestion.Facultad;
import com.gestion.Materia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelMaterias extends JPanel {
    private DefaultTableModel tableModel;

    public PanelMaterias() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Gestión de Materias Globales");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        String[] cols = { "Código", "Nombre" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarMateriaSeleccionada(table);
                }
            }
        });

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Nueva Materia");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar"); // Opcional, requiere cuidado
        JButton btnRefrescar = new JButton("Refrescar");

        btnAgregar.addActionListener(e -> agregarMateria());
        btnEditar.addActionListener(e -> editarMateriaSeleccionada(table));
        btnEliminar.addActionListener(
                e -> JOptionPane.showMessageDialog(this, "Eliminación no implementada por seguridad."));
        btnRefrescar.addActionListener(e -> refrescarTabla());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);

        add(panelBotones, BorderLayout.SOUTH);

        refrescarTabla();
    }

    private void refrescarTabla() {
        tableModel.setRowCount(0);
        for (Materia m : Facultad.getInstance().getMaterias()) {
            tableModel.addRow(new Object[] { m.getCodigo(), m.getNombre() });
        }
    }

    private void agregarMateria() {
        JPanel pnl = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNombre = new JTextField();
        JTextField txtCodigo = new JTextField();

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnl.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnl.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pnl.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnl.add(txtCodigo, gbc);

        pnl.setPreferredSize(new Dimension(300, 100));

        int res = JOptionPane.showConfirmDialog(this, pnl, "Nueva Materia Global", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String nom = txtNombre.getText().trim();
            String cod = txtCodigo.getText().trim();

            if (!nom.isEmpty() && !cod.isEmpty()) {
                if (Facultad.getInstance().existeMateria(cod)) {
                    JOptionPane.showMessageDialog(this, "El código ya existe.");
                } else {
                    Facultad.getInstance().agregarMateria(new Materia(nom, cod));
                    refrescarTabla();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            }
        }
    }

    private void editarMateriaSeleccionada(JTable table) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String codigo = (String) table.getValueAt(row, 0);
            Materia m = Facultad.getInstance().getMaterias().stream()
                    .filter(mat -> mat.getCodigo().equals(codigo))
                    .findFirst().orElse(null);

            if (m != null) {
                String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre para " + codigo + ":",
                        m.getNombre());
                if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                    m.setNombre(nuevoNombre);
                    refrescarTabla();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una materia.");
        }
    }
}
