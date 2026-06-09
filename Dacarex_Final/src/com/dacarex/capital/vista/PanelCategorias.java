package com.dacarex.capital.vista;

import com.dacarex.capital.dao.CategoriaDAO;
import com.dacarex.capital.modelo.Categoria;
import com.dacarex.capital.modelo.TipoMovimiento;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PanelCategorias extends Panel {

    private java.awt.List listaAWT;
    private List<Categoria> categorias;
    private final CategoriaDAO dao = new CategoriaDAO();

    public PanelCategorias() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(245, 247, 250));

        Label lblTitulo = new Label("Categorias");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(30, 30, 45));
        add(lblTitulo, BorderLayout.NORTH);

        listaAWT = new java.awt.List(10);
        listaAWT.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(listaAWT, BorderLayout.CENTER);

        Panel botones = new Panel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        botones.setBackground(new Color(245, 247, 250));

        Button btnNueva = new Button("+ Nueva");
        btnNueva.setBackground(new Color(40, 167, 69));
        btnNueva.setForeground(Color.WHITE);
        btnNueva.addActionListener(e -> abrirFormularioNueva());
        botones.add(btnNueva);

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarSeleccionada());
        botones.add(btnEliminar);

        add(botones, BorderLayout.SOUTH);

        recargar();
    }

    public void recargar() {
        categorias = dao.buscarTodos();
        listaAWT.removeAll();
        for (Categoria c : categorias) {
            listaAWT.add(String.format("%-25s | %s",
                c.getNombre(), c.getTipo().getValor()));
        }
        if (categorias.isEmpty()) listaAWT.add("   No hay categorias.");
    }

    private void abrirFormularioNueva() {
        Frame parent = (Frame) SwingUtilities_AWT.getParentFrame(this);
        Dialog dialog = new Dialog(parent, "Nueva Categoria", true);
        dialog.setSize(300, 180);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new GridLayout(4, 2, 8, 8));
        dialog.setBackground(new Color(40, 40, 60));

        dialog.add(etiqueta("Nombre:"));
        TextField txtNombre = new TextField();
        dialog.add(txtNombre);

        dialog.add(etiqueta("Tipo:"));
        Choice cmbTipo = new Choice();
        cmbTipo.add("INGRESO");
        cmbTipo.add("GASTO");
        dialog.add(cmbTipo);

        Label lblError = new Label("", Label.CENTER);
        lblError.setForeground(new Color(255, 100, 100));
        dialog.add(lblError);

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        dialog.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                lblError.setText("El nombre es obligatorio.");
                return;
            }
            TipoMovimiento tipo = TipoMovimiento.valueOf(cmbTipo.getSelectedItem());
            dao.guardar(new Categoria(nombre, tipo));
            recargar();
            dialog.dispose();
        });

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dialog.dispose(); }
        });

        dialog.setVisible(true);
    }

    private void eliminarSeleccionada() {
        int idx = listaAWT.getSelectedIndex();
        if (idx < 0 || categorias == null || idx >= categorias.size()) {
            mostrarMensaje("Selecciona una categoria para eliminar.");
            return;
        }
        dao.eliminar(categorias.get(idx).getId());
        recargar();
    }

    private void mostrarMensaje(String msg) {
        Frame parent = (Frame) SwingUtilities_AWT.getParentFrame(this);
        Dialog d = new Dialog(parent, "Aviso", true);
        d.setSize(300, 100);
        d.setLocationRelativeTo(parent);
        d.setLayout(new BorderLayout());
        Label lbl = new Label(msg, Label.CENTER);
        Button ok  = new Button("OK");
        ok.addActionListener(e -> d.dispose());
        d.add(lbl, BorderLayout.CENTER);
        d.add(ok, BorderLayout.SOUTH);
        d.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { d.dispose(); }
        });
        d.setVisible(true);
    }

    private Label etiqueta(String texto) {
        Label l = new Label(texto);
        l.setForeground(Color.WHITE);
        return l;
    }
}