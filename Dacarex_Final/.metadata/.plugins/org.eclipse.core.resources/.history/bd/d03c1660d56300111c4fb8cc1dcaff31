package com.dacarex.capital.vista;

import com.dacarex.capital.dao.CategoriaDAO;
import com.dacarex.capital.dao.MovimientoDAO;
import com.dacarex.capital.modelo.Categoria;
import com.dacarex.capital.modelo.Movimiento;
import com.dacarex.capital.modelo.TipoMovimiento;
import com.dacarex.capital.util.ExportadorCSV;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class PanelMovimientos extends Panel {

    private List<Movimiento> movimientos;
    private java.awt.List listaAWT;
    private Label lblSaldo;

    private final MovimientoDAO movimientoDAO = new MovimientoDAO();
    private final CategoriaDAO  categoriaDAO  = new CategoriaDAO();

    public PanelMovimientos() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(245, 247, 250));

        // ── CABECERA ──
        Panel cabecera = new Panel(new BorderLayout());
        cabecera.setBackground(new Color(245, 247, 250));

        Label lblTitulo = new Label("Movimientos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(30, 30, 45));
        cabecera.add(lblTitulo, BorderLayout.WEST);

        lblSaldo = new Label("Saldo: 0.00 EUR", Label.RIGHT);
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldo.setForeground(new Color(40, 167, 69));
        cabecera.add(lblSaldo, BorderLayout.EAST);

        add(cabecera, BorderLayout.NORTH);

        // ── LISTA ──
        listaAWT = new java.awt.List(10);
        listaAWT.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(listaAWT, BorderLayout.CENTER);

        // ── BOTONES ──
        Panel botones = new Panel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        botones.setBackground(new Color(245, 247, 250));

        Button btnNuevo = new Button("+ Nuevo");
        btnNuevo.setBackground(new Color(40, 167, 69));
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.addActionListener(e -> abrirFormularioNuevo());
        botones.add(btnNuevo);

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        botones.add(btnEliminar);

        Button btnExportar = new Button("Exportar CSV");
        btnExportar.setBackground(new Color(255, 153, 0));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.addActionListener(e -> exportarCSV());
        botones.add(btnExportar);

        Button btnTodos = new Button("Todos");
        btnTodos.setBackground(new Color(100, 100, 130));
        btnTodos.setForeground(Color.WHITE);
        btnTodos.addActionListener(e -> recargar());
        botones.add(btnTodos);

        Button btnIngresos = new Button("Ingresos");
        btnIngresos.setBackground(new Color(0, 123, 255));
        btnIngresos.setForeground(Color.WHITE);
        btnIngresos.addActionListener(e -> cargarLista(
            movimientoDAO.buscarPorTipo(TipoMovimiento.INGRESO)));
        botones.add(btnIngresos);

        Button btnGastos = new Button("Gastos");
        btnGastos.setBackground(new Color(220, 53, 69));
        btnGastos.setForeground(Color.WHITE);
        btnGastos.addActionListener(e -> cargarLista(
            movimientoDAO.buscarPorTipo(TipoMovimiento.GASTO)));
        botones.add(btnGastos);

        add(botones, BorderLayout.SOUTH);

        recargar();
    }

    public void recargar() {
        cargarLista(movimientoDAO.buscarTodos());
        actualizarSaldo();
    }

    private void cargarLista(List<Movimiento> lista) {
        this.movimientos = lista;
        listaAWT.removeAll();
        for (Movimiento m : lista) {
            listaAWT.add(String.format("%-12s | %-8s | %-20s | %10.2f EUR | %s",
                m.getFecha(),
                m.getTipo().getValor(),
                m.getDescripcion(),
                m.getImporte(),
                m.getCategoria() != null ? m.getCategoria().getNombre() : "-"
            ));
        }
        if (lista.isEmpty()) listaAWT.add("   No hay movimientos.");
    }

    private void actualizarSaldo() {
        double saldo = movimientoDAO.calcularTotalIngresos()
                     - movimientoDAO.calcularTotalGastos();
        lblSaldo.setText(String.format("Saldo: %.2f EUR", saldo));
        lblSaldo.setForeground(saldo >= 0
            ? new Color(40, 167, 69)
            : new Color(220, 53, 69));
    }

    private void abrirFormularioNuevo() {
        Frame parent = (Frame) SwingUtilities_AWT.getParentFrame(this);
        Dialog dialog = new Dialog(parent, "Nuevo Movimiento", true);
        dialog.setSize(380, 320);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new GridLayout(7, 2, 8, 8));
        dialog.setBackground(new Color(40, 40, 60));

        dialog.add(etiqueta("Tipo:"));
        Choice cmbTipo = new Choice();
        cmbTipo.add("INGRESO");
        cmbTipo.add("GASTO");
        dialog.add(cmbTipo);

        dialog.add(etiqueta("Descripcion:"));
        TextField txtDesc = new TextField();
        dialog.add(txtDesc);

        dialog.add(etiqueta("Importe:"));
        TextField txtImporte = new TextField();
        dialog.add(txtImporte);

        dialog.add(etiqueta("Categoria:"));
        Choice cmbCat = new Choice();
        List<Categoria> cats = categoriaDAO.buscarTodos();
        cats.forEach(c -> cmbCat.add(c.getNombre()));
        dialog.add(cmbCat);

        dialog.add(etiqueta("Fecha (YYYY-MM-DD):"));
        TextField txtFecha = new TextField(LocalDate.now().toString());
        dialog.add(txtFecha);

        dialog.add(etiqueta("Notas:"));
        TextField txtNotas = new TextField();
        dialog.add(txtNotas);

        Label lblError = new Label("", Label.CENTER);
        lblError.setForeground(new Color(255, 100, 100));
        dialog.add(lblError);

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        dialog.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            try {
                TipoMovimiento tipo = TipoMovimiento.valueOf(cmbTipo.getSelectedItem());
                String desc   = txtDesc.getText().trim();
                double importe = Double.parseDouble(txtImporte.getText().trim());
                LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
                String notas  = txtNotas.getText().trim();

                if (desc.isEmpty()) {
                    lblError.setText("La descripcion es obligatoria.");
                    return;
                }
                if (importe <= 0) {
                    lblError.setText("El importe debe ser mayor que 0.");
                    return;
                }

                int idxCat = cmbCat.getSelectedIndex();
                Categoria cat = cats.get(idxCat);

                Movimiento nuevo = new Movimiento(tipo, desc, importe, cat, fecha, notas);
                movimientoDAO.guardar(nuevo);
                recargar();
                dialog.dispose();

            } catch (NumberFormatException ex) {
                lblError.setText("Importe no valido.");
            } catch (Exception ex) {
                lblError.setText("Error: " + ex.getMessage());
            }
        });

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dialog.dispose(); }
        });

        dialog.setVisible(true);
    }

    private void eliminarSeleccionado() {
        int idx = listaAWT.getSelectedIndex();
        if (idx < 0 || movimientos == null || idx >= movimientos.size()) {
            mostrarMensaje("Selecciona un movimiento para eliminar.");
            return;
        }
        Movimiento m = movimientos.get(idx);
        movimientoDAO.eliminar(m.getId());
        recargar();
    }

    private void exportarCSV() {
        try {
            List<Movimiento> todos = movimientoDAO.buscarTodos();
            if (todos.isEmpty()) {
                mostrarMensaje("No hay movimientos para exportar.");
                return;
            }
            String ruta = ExportadorCSV.exportar(todos);
            mostrarMensaje("Exportado en:\n" + ruta);
        } catch (IOException ex) {
            mostrarMensaje("Error al exportar: " + ex.getMessage());
        }
    }

    private void mostrarMensaje(String msg) {
        Frame parent = (Frame) SwingUtilities_AWT.getParentFrame(this);
        Dialog d = new Dialog(parent, "Aviso", true);
        d.setSize(320, 120);
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