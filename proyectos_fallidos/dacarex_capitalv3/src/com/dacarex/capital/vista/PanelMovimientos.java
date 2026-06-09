package com.dacarex.capital.vista;

import com.dacarex.capital.dao.CategoriaDAO;
import com.dacarex.capital.enums.TipoMovimiento;
import com.dacarex.capital.modelo.Categoria;
import com.dacarex.capital.modelo.Movimiento;
import com.dacarex.capital.servicio.MovimientoServicio;
import com.dacarex.capital.util.ExportadorCSV;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class PanelMovimientos extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JComboBox<String> cmbTipo;
    private JLabel lblSaldo;

    private final MovimientoServicio servicio = new MovimientoServicio();
    private final CategoriaDAO categoriaDAO   = new CategoriaDAO();

    public PanelMovimientos() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── CABECERA ──
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(new Color(245, 247, 250));

        JLabel lblTitulo = new JLabel("💸 Movimientos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(30, 30, 45));
        cabecera.add(lblTitulo, BorderLayout.WEST);

        lblSaldo = new JLabel("Saldo: calculando...");
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 14));
        cabecera.add(lblSaldo, BorderLayout.EAST);

        add(cabecera, BorderLayout.NORTH);

        // ── FILTROS ──
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filtros.setBackground(new Color(245, 247, 250));

        txtBuscar = new JTextField(18);
        txtBuscar.setBorder(BorderFactory.createTitledBorder("Buscar"));
        filtros.add(txtBuscar);

        cmbTipo = new JComboBox<>(new String[]{"Todos", "INGRESO", "GASTO"});
        cmbTipo.setBorder(BorderFactory.createTitledBorder("Tipo"));
        filtros.add(cmbTipo);

        JButton btnFiltrar = new JButton("🔍 Filtrar");
        btnFiltrar.setBackground(new Color(100, 180, 255));
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.addActionListener(e -> filtrar());
        filtros.add(btnFiltrar);

        JButton btnNuevo = new JButton("➕ Nuevo");
        btnNuevo.setBackground(new Color(40, 167, 69));
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFocusPainted(false);
        btnNuevo.addActionListener(e -> abrirFormularioNuevo());
        filtros.add(btnNuevo);

        JButton btnEliminar = new JButton("🗑 Eliminar");
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        filtros.add(btnEliminar);

        JButton btnExportar = new JButton("📥 Exportar CSV");
        btnExportar.setBackground(new Color(255, 153, 0));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.addActionListener(e -> exportarCSV());
        filtros.add(btnExportar);

        add(filtros, BorderLayout.CENTER);

        // ── TABLA ──
        String[] columnas = {"ID", "Fecha", "Tipo", "Descripción", "Importe (€)", "Categoría", "Notas"};
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
        cargarTabla(servicio.obtenerTodos());
        double saldo = servicio.calcularSaldo();
        lblSaldo.setText(String.format("Saldo: %.2f €", saldo));
        lblSaldo.setForeground(saldo >= 0
            ? new Color(40, 167, 69)
            : new Color(220, 53, 69));
    }

    private void cargarTabla(List<Movimiento> lista) {
        modeloTabla.setRowCount(0);
        for (Movimiento m : lista) {
            modeloTabla.addRow(new Object[]{
                m.getId(),
                m.getFecha(),
                m.getTipo().getValor(),
                m.getDescripcion(),
                String.format("%.2f", m.getImporte()),
                m.getCategoria() != null ? m.getCategoria().getNombre() : "-",
                m.getNotas() != null ? m.getNotas() : ""
            });
        }
    }

    private void filtrar() {
        String texto = txtBuscar.getText().trim();
        String tipo  = (String) cmbTipo.getSelectedItem();

        List<Movimiento> resultado;

        if (!"Todos".equals(tipo)) {
            resultado = servicio.filtrarPorTipo(TipoMovimiento.valueOf(tipo));
        } else if (!texto.isEmpty()) {
            resultado = servicio.filtrarPorTexto(texto);
        } else {
            resultado = servicio.obtenerTodos();
        }

        cargarTabla(resultado);
    }

    private void abrirFormularioNuevo() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Nuevo Movimiento", true);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));
        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Campos
        dialog.add(new JLabel("Tipo:"));
        JComboBox<TipoMovimiento> cmbTipoMov = new JComboBox<>(TipoMovimiento.values());
        dialog.add(cmbTipoMov);

        dialog.add(new JLabel("Descripción:"));
        JTextField txtDesc = new JTextField();
        dialog.add(txtDesc);

        dialog.add(new JLabel("Importe (€):"));
        JTextField txtImporte = new JTextField();
        dialog.add(txtImporte);

        dialog.add(new JLabel("Categoría:"));
        List<Categoria> categorias = categoriaDAO.buscarTodos();
        JComboBox<Categoria> cmbCat = new JComboBox<>(categorias.toArray(new Categoria[0]));
        dialog.add(cmbCat);

        dialog.add(new JLabel("Fecha (YYYY-MM-DD):"));
        JTextField txtFecha = new JTextField(LocalDate.now().toString());
        dialog.add(txtFecha);

        dialog.add(new JLabel("Notas:"));
        JTextField txtNotas = new JTextField();
        dialog.add(txtNotas);

        dialog.add(new JLabel(""));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        dialog.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            try {
                TipoMovimiento tipo = (TipoMovimiento) cmbTipoMov.getSelectedItem();
                String desc   = txtDesc.getText().trim();
                double importe = Double.parseDouble(txtImporte.getText().trim());
                Categoria cat  = (Categoria) cmbCat.getSelectedItem();
                LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
                String notas  = txtNotas.getText().trim();

                Movimiento nuevo = new Movimiento(0, tipo, desc, importe, cat, fecha, notas);
                servicio.guardar(nuevo);
                recargar();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "✔ Movimiento guardado.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "El importe debe ser un número.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un movimiento para eliminar.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Eliminar este movimiento?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            servicio.eliminar(id);
            recargar();
        }
    }

    private void exportarCSV() {
        try {
            List<Movimiento> todos = servicio.obtenerTodos();
            if (todos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay movimientos para exportar.");
                return;
            }
            String ruta = ExportadorCSV.exportar(todos);
            JOptionPane.showMessageDialog(this, "✔ Exportado en:\n" + ruta);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
        }
    }
}