package com.gestion.view;

import com.gestion.controller.CarreraController;
import com.gestion.model.Carrera;
import com.gestion.model.Materia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DialogoCatalogoMaterias extends JDialog {
    private JComboBox<Object> comboFiltro; // Object para permitir String "Todas" y Carrera
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private CarreraController controller;

    public DialogoCatalogoMaterias(Frame owner, CarreraController controller) {
        super(owner, "Catálogo de Materias", true);
        this.controller = controller;
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // --- Panel Superior: Filtros ---
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.add(new JLabel("Filtrar por Carrera:"));

        comboFiltro = new JComboBox<>();
        comboFiltro.addItem("Todas");
        for (Carrera c : controller.listarCarreras()) {
            comboFiltro.addItem(c);
        }

        comboFiltro.addActionListener(e -> filtrar());
        panelFiltro.add(comboFiltro);
        add(panelFiltro, BorderLayout.NORTH);

        // --- Panel Central: Tabla ---
        String[] cols = { "Código", "Nombre" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Edición via dialogo/botón
            }
        };
        JTable table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Doble click para editar
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarMateriaSeleccionada(table);
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Panel Inferior: Botones ---
        JPanel panelBotones = new JPanel();
        JButton btnEditar = new JButton("Editar Nombre");
        JButton btnEquivalencias = new JButton("Equivalencias");
        JButton btnCerrar = new JButton("Cerrar");

        btnEditar.addActionListener(e -> editarMateriaSeleccionada(table));
        btnEquivalencias.addActionListener(e -> gestionarEquivalencias(table));
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnEditar);
        panelBotones.add(btnEquivalencias);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        cargarTodasLasMaterias();
    }

    private void cargarTodasLasMaterias() {
        tableModel.setRowCount(0);
        List<Materia> materias = new ArrayList<>(controller.getMaterias());
        materias.sort(Comparator.comparing(Materia::getNombre)); // Orden alfabético

        for (Materia m : materias) {
            tableModel.addRow(new Object[] { m.getCodigo(), m.getNombre() });
        }
    }

    private void filtrar() {
        Object item = comboFiltro.getSelectedItem();
        if ("Todas".equals(item)) {
            cargarTodasLasMaterias();
        } else if (item instanceof Carrera) {
            Carrera c = (Carrera) item;
            mostrarMateriasDeCarrera(c);
        }
    }

    private void mostrarMateriasDeCarrera(Carrera c) {
        tableModel.setRowCount(0);
        // Obtener materias del plan de esa carrera
        // Nota: PlanDeEstudios.getAllMaterias devuelve las materias del plan
        List<Materia> materias = c.getPlan().getAllMaterias();
        materias.sort(Comparator.comparing(Materia::getNombre));

        for (Materia m : materias) {
            tableModel.addRow(new Object[] { m.getCodigo(), m.getNombre() });
        }
    }

    private void editarMateriaSeleccionada(JTable table) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String codigo = (String) table.getValueAt(row, 0); // View index
            // Buscar la materia real (podríamos optimizar, pero búsqueda lineal global es
            // segura por ahora)
            Materia target = null;
            for (Materia m : controller.getMaterias()) {
                if (m.getCodigo().equals(codigo)) {
                    target = m;
                    break;
                }
            }

            if (target != null) {
                String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo Nombre:", target.getNombre());
                if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                    target.setNombre(nuevoNombre);
                    // Refrescar vista actual
                    filtrar();
                }
            }
        }
    }

    private void gestionarEquivalencias(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una materia primero.");
            return;
        }

        String codigo = (String) table.getValueAt(row, 0);
        Materia materiaActual = controller.getMaterias().stream()
                .filter(m -> m.getCodigo().equals(codigo)).findFirst().orElse(null);

        if (materiaActual == null)
            return;

        // Diálogo para equivalencias
        JDialog d = new JDialog(this, "Equivalencias de " + materiaActual.getNombre(), true);
        d.setSize(400, 300);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());

        DefaultTableModel modelEq = new DefaultTableModel(new String[] { "Código", "Nombre" }, 0);
        JTable tableEq = new JTable(modelEq);
        d.add(new JScrollPane(tableEq), BorderLayout.CENTER);

        JButton btnAgregar = new JButton("Agregar Equivalencia");
        btnAgregar.addActionListener(e -> {
            // Selector simple
            String inputCodigo = JOptionPane.showInputDialog(d, "Ingrese Código de materia equivalente:");
            if (inputCodigo != null) {
                Materia equiv = controller.getMaterias().stream()
                        .filter(m -> m.getCodigo().equalsIgnoreCase(inputCodigo)).findFirst().orElse(null);
                if (equiv != null && !equiv.equals(materiaActual)) {
                    materiaActual.agregarEquivalencia(equiv);
                    JOptionPane.showMessageDialog(d, "Agregada.");
                    // Refrescar tabla local
                    actualizarTablaEquivalencias(modelEq, materiaActual);
                } else {
                    JOptionPane.showMessageDialog(d, "Materia no encontrada o inválida.");
                }
            }
        });

        d.add(btnAgregar, BorderLayout.SOUTH);

        actualizarTablaEquivalencias(modelEq, materiaActual);
        d.setVisible(true);
    }

    private void actualizarTablaEquivalencias(DefaultTableModel model, Materia m) {
        model.setRowCount(0);
        for (Materia eq : m.getEquivalencias()) {
            model.addRow(new Object[] { eq.getCodigo(), eq.getNombre() });
        }
    }
}
