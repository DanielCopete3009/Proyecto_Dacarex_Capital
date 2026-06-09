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

// PanelMovimientos es la sección visual que gestiona el historial de transacciones, saldos y filtros
public class PanelMovimientos extends Panel {

    private List<Movimiento> movimientos;       // Lista en memoria de los movimientos que se ven en pantalla
    private java.awt.List listaAWT;             // Componente visual de lista
    private Label lblSaldo;                     // Etiqueta para mostrar el saldo neto acumulado

    private final MovimientoDAO movimientoDAO = new MovimientoDAO(); // Conector de movimientos a la BD
    private final CategoriaDAO  categoriaDAO  = new CategoriaDAO();  // Conector de categorías a la BD

    // Constructor: Crea el panel y dibuja la interfaz gráfica
    public PanelMovimientos() {
        inicializarComponentes();
    }

    // Configura la distribución del panel principal de movimientos
    private void inicializarComponentes() {
        setLayout(new BorderLayout(5, 5));

        // ── CABECERA (Zona Norte) ──
        Panel cabecera = new Panel(new BorderLayout());

        Label lblTitulo = new Label("Movimientos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        cabecera.add(lblTitulo, BorderLayout.WEST); // Título a la izquierda

        lblSaldo = new Label("Saldo: 0.00 EUR", Label.RIGHT);
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 14));
        cabecera.add(lblSaldo, BorderLayout.EAST); // Saldo a la derecha

        add(cabecera, BorderLayout.NORTH);

        // ── LISTA CENTRAL (Zona Centro) ──
        listaAWT = new java.awt.List(10);
        listaAWT.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Letra fija para cuadrar columnas
        add(listaAWT, BorderLayout.CENTER);

        // ── BOTONERA INFERIOR (Zona Sur) ──
        Panel botones = new Panel(new FlowLayout(FlowLayout.LEFT, 8, 5));

        Button btnNuevo = new Button("+ Nuevo");
        btnNuevo.addActionListener(e -> abrirFormularioNuevo()); // Abre formulario de inserción
        botones.add(btnNuevo);

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        botones.add(btnEliminar);

        Button btnExportar = new Button("Exportar CSV");
        btnExportar.addActionListener(e -> exportarCSV()); // Exporta los datos a Excel/CSV
        botones.add(btnExportar);

        Button btnTodos = new Button("Todos");
        btnTodos.addActionListener(e -> recargar()); // Quita filtros y muestra todo
        botones.add(btnTodos);

        Button btnIngresos = new Button("Ingresos");
        btnIngresos.addActionListener(e -> cargarLista(
            movimientoDAO.buscarPorTipo(TipoMovimiento.INGRESO))); // Filtra solo ingresos
        botones.add(btnIngresos);

        Button btnGastos = new Button("Gastos");
        btnGastos.addActionListener(e -> cargarLista(
            movimientoDAO.buscarPorTipo(TipoMovimiento.GASTO))); // Filtra solo gastos
        botones.add(btnGastos);

        add(botones, BorderLayout.SOUTH);

        // Carga y calcula el estado inicial de la pantalla
        recargar();
    }

    // Carga todos los registros sin filtrar y refresca el saldo de la cabecera
    public void recargar() {
        cargarLista(movimientoDAO.buscarTodos());
        actualizarSaldo();
    }

    // Vacía la lista visual y añade los movimientos de la lista que recibe por parámetro
    private void cargarLista(List<Movimiento> lista) {
        this.movimientos = lista;
        listaAWT.removeAll();
        for (Movimiento m : lista) {
            // Formatea el texto en columnas perfectas usando longitudes fijas (%-12s, %10.2f, etc.)
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

    // Calcula la resta matemática de Ingresos menos Gastos consultando al DAO y actualiza la cabecera
    private void actualizarSaldo() {
        double saldo = movimientoDAO.calcularTotalIngresos() 
                     - movimientoDAO.calcularTotalGastos();
        lblSaldo.setText(String.format("Saldo: %.2f EUR", saldo));
    }

    // Abre un formulario modal flotante con los campos necesarios para registrar un movimiento
    private void abrirFormularioNuevo() {
        Frame parent = (Frame) SwingUtilities_AWT.getParentFrame(this);
        Dialog dialog = new Dialog(parent, "Nuevo Movimiento", true);
        dialog.setSize(380, 320);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new GridLayout(7, 2, 8, 8)); // Rejilla de 7 filas y 2 columnas

        // Campos básicos del formulario
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

        // Desplegable de Categorías cargado directamente de la BD
        dialog.add(etiqueta("Categoria:"));
        Choice cmbCat = new Choice();
        List<Categoria> cats = categoriaDAO.buscarTodos();
        cats.forEach(c -> cmbCat.add(c.getNombre()));
        dialog.add(cmbCat);

        // Fecha (Autocompleta con la fecha de hoy por comodidad)
        dialog.add(etiqueta("Fecha (YYYY-MM-DD):"));
        TextField txtFecha = new TextField(LocalDate.now().toString());
        dialog.add(txtFecha);

        dialog.add(etiqueta("Notas:"));
        TextField txtNotas = new TextField();
        dialog.add(txtNotas);

        Label lblError = new Label("", Label.CENTER);
        dialog.add(lblError);

        Button btnGuardar = new Button("Guardar");
        dialog.add(btnGuardar);

        // Evento que procesa, valida y guarda los datos capturados
        btnGuardar.addActionListener(e -> {
            try {
                TipoMovimiento tipo = TipoMovimiento.valueOf(cmbTipo.getSelectedItem());
                String desc   = txtDesc.getText().trim();
                double importe = Double.parseDouble(txtImporte.getText().trim()); // Convierte texto a decimal
                LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());     // Convierte texto a fecha
                String notas  = txtNotas.getText().trim();

                // Validaciones de negocio obligatorias
                if (desc.isEmpty()) {
                    lblError.setText("La descripcion es obligatoria.");
                    return;
                }
                if (importe <= 0) {
                    lblError.setText("El importe debe ser mayor que 0.");
                    return;
                }

                // Obtiene el objeto Categoria seleccionado a través de su posición en el Choice
                int idxCat = cmbCat.getSelectedIndex();
                Categoria cat = cats.get(idxCat);

                // Construye el objeto, lo guarda, refresca la pantalla central y cierra el formulario
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

        // Cierre manual con la "X" de la ventana emergente
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dialog.dispose(); }
        });

        dialog.setVisible(true);
    }

    // Identifica la fila seleccionada por el usuario y borra el registro de la BD
    private void eliminarSeleccionado() {
        int idx = listaAWT.getSelectedIndex();
        if (idx < 0 || movimientos == null || idx >= movimientos.size()) {
            mostrarMensaje("Selecciona un movimiento para eliminar.");
            return;
        }
        Movimiento m = movimientos.get(idx);
        movimientoDAO.eliminar(m.getId()); // Borra usando el ID de la base de datos
        recargar(); // Vuelve a leer todo y recalcula saldos automáticamente
    }

    // Solicita al ExportadorCSV que procese todos los movimientos y muestra un aviso con la ruta resultante
    private void exportarCSV() {
        try {
            List<Movimiento> todos = movimientoDAO.buscarTodos();
            if (todos.isEmpty()) {
                mostrarMensaje("No hay movimientos para exportar.");
                return;
            }
            String ruta = ExportadorCSV.exportar(todos); // Genera el fichero físico en el disco duro
            mostrarMensaje("Exportado en:\n" + ruta);
        } catch (IOException ex) {
            mostrarMensaje("Error al exportar: " + ex.getMessage());
        }
    }

    // Ventana modal rápida de alerta/información
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

    // Generador de etiquetas para simplificar el código del formulario
    private Label etiqueta(String texto) {
        return new Label(texto);
    }
}