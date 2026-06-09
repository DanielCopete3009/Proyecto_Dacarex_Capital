package com.dacarex.capital.vista;

import com.dacarex.capital.dao.CategoriaDAO;
import com.dacarex.capital.enums.TipoMovimiento;
import com.dacarex.capital.modelo.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCategorias extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private final CategoriaDAO dao = new CategoriaDAO();

    public PanelCategorias() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── CABECERA ──
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(new Color(245, 247, 250));

        JLabel lblTitulo = new JLabel("🗂️ Categorías");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(30, 30, 45));
        cabecera.add(lblTitulo, BorderLayout.WEST);
        add(cabecera, BorderLayout.NORTH);

        // ── BOTONES ──
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        botones.setBackground(new Color(245, 247, 250));

        JButton btnNueva = new JButton("➕ Nueva categoría");
        btnNueva.setBackground(new Color(40, 167, 69));
        btnNueva.setForeground(Color.WHITE);
        btnNueva.setFocusPainted(false);
        btnNueva.addActionListener(e -> abrirFormularioNueva());
        botones.add(btnNueva);

        JButton btnEliminar = new JButton("🗑 Eliminar");
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> eliminarSeleccionada());
        botones.add(btnEliminar);

        add(botones, BorderLayout.CENTER);

        // ── TABLA ──
        String[] columnas = {"ID", "Nombre", "Tipo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Arial", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(30, 30, 45));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(new Color(100, 180, 255));
        tabla.setGridColor(new Color(220, 220, 230));
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(scroll, BorderLayout.SOUTH);

        recargar();
    }

    public void recargar() {
        cargarTabla(dao.buscarTodos());
    }

    private void cargarTabla(List<Categoria> lista) {
        modeloTabla.setRowCount(0);
        for (Categoria c : lista) {
            modeloTabla.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getTipo().getValor()
            });
        }
    }

    private void abrirFormularioNueva() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Nueva Categoría", true);
        dialog.setSize(320, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        dialog.add(new JLabel("Nombre:"));
        JTextField txtNombre = new JTextField();
        dialog.add(txtNombre);

        dialog.add(new JLabel("Tipo:"));
        JComboBox<TipoMovimiento> cmbTipo = new JComboBox<>(TipoMovimiento.values());
        dialog.add(cmbTipo);

        dialog.add(new JLabel(""));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        dialog.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "El nombre es obligatorio.");
                return;
            }
            TipoMovimiento tipo = (TipoMovimiento) cmbTipo.getSelectedItem();
            Categoria nueva = new Categoria(0, nombre, tipo);
            dao.guardar(nueva);
            recargar();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "✔ Categoría guardada.");
        });

        dialog.setVisible(true);
    }

    private void eliminarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría para eliminar.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Eliminar esta categoría?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dao.eliminar(id);
            recargar();
        }
    }
}