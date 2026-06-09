package com.dacarex.capital.vista;

import com.dacarex.capital.dao.CategoriaDAO;
import com.dacarex.capital.modelo.Categoria;
import com.dacarex.capital.modelo.TipoMovimiento;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

// PanelCategorias es una sección visual (Panel) que se incrusta en la ventana principal
public class PanelCategorias extends Panel {

    private java.awt.List listaAWT;             // Componente visual de lista para mostrar texto
    private List<Categoria> categorias;         // Lista interna para almacenar los objetos de la BD
    private final CategoriaDAO dao = new CategoriaDAO(); // Conector para las operaciones de Base de Datos

    // Constructor: Se ejecuta al crear el panel y arranca la interfaz
    public PanelCategorias() {
        inicializarComponentes();
    }

    // Configura y distribuye los botones, listas y textos dentro del panel
    private void inicializarComponentes() {
        // Usa BorderLayout (Organiza en Norte, Sur, Este, Oeste y Centro) con 5px de separación
        setLayout(new BorderLayout(5, 5));

        // Título superior (Zona Norte)
        Label lblTitulo = new Label("Categorias");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        // Lista de elementos (Zona Central, ocupa todo el espacio disponible)
        listaAWT = new java.awt.List(10);
        listaAWT.setFont(new Font("Monospaced", Font.PLAIN, 13)); // Fuente monoespaciada para alinear texto
        add(listaAWT, BorderLayout.CENTER);

        // Sub-panel inferior para agrupar los botones (Zona Sur)
        Panel botones = new Panel(new FlowLayout(FlowLayout.LEFT, 8, 5));

        // Botón para añadir categorías
        Button btnNueva = new Button("+ Nueva");
        btnNueva.addActionListener(e -> abrirFormularioNueva()); // Escucha el clic y abre el formulario
        botones.add(btnNueva);

        // Botón para borrar categorías
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.addActionListener(e -> eliminarSeleccionada()); // Escucha el clic y borra
        botones.add(btnEliminar);

        add(botones, BorderLayout.SOUTH);

        // Carga los datos de la base de datos en la lista por primera vez
        recargar();
    }

    // Limpia la lista visual, lee la Base de Datos y la vuelve a llenar
    public void recargar() {
        categorias = dao.buscarTodos(); // Pide las categorías actualizadas al DAO
        listaAWT.removeAll();           // Vacía la lista visual de la pantalla
        
        // Formatea y añade cada categoría a la lista de forma alineada (ej: "Alquiler             | Gasto")
        for (Categoria c : categorias) {
            listaAWT.add(String.format("%-25s | %s",
                c.getNombre(), c.getTipo().getValor()));
        }
        // Si la base de datos está vacía, añade un aviso visual
        if (categorias.isEmpty()) listaAWT.add("   No hay categorias.");
    }

    // Abre una ventana emergente ("Modal") con un formulario para crear una categoría
    private void abrirFormularioNueva() {
        Frame parent = (Frame) BuscadorVentanas.getParentFrame(this); // Detecta la ventana principal
        Dialog dialog = new Dialog(parent, "Nueva Categoria", true); // "true" bloquea la ventana de atrás
        dialog.setSize(300, 180);
        dialog.setLocationRelativeTo(parent); // Centra la ventana emergente
        dialog.setLayout(new GridLayout(4, 2, 8, 8)); // Rejilla cuadriculada de 4 filas y 2 columnas

        // Fila 1: Etiqueta y Caja de texto para el nombre
        dialog.add(etiqueta("Nombre:"));
        TextField txtNombre = new TextField();
        dialog.add(txtNombre);

        // Fila 2: Etiqueta y Desplegable para el tipo
        dialog.add(etiqueta("Tipo:"));
        Choice cmbTipo = new Choice();
        cmbTipo.add("INGRESO");
        cmbTipo.add("GASTO");
        dialog.add(cmbTipo);

        // Fila 3: Espacio para mensajes de error en rojo/texto
        Label lblError = new Label("", Label.CENTER);
        dialog.add(lblError);

        // Fila 4: Botón de confirmación
        Button btnGuardar = new Button("Guardar");
        dialog.add(btnGuardar);

        // Acción al pulsar "Guardar"
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim(); // Quita espacios en blanco innecesarios
            if (nombre.isEmpty()) {
                lblError.setText("El nombre es obligatorio."); // Valida que no esté vacío
                return;
            }
            // Obtiene el Enum correspondiente al texto seleccionado en el Choice
            TipoMovimiento tipo = TipoMovimiento.valueOf(cmbTipo.getSelectedItem());
            
            // Envía la nueva categoría al DAO para que la grabe en ObjectDB
            dao.guardar(new Categoria(nombre, tipo));
            
            recargar();       // Actualiza la lista principal de la pantalla
            dialog.dispose(); // Cierra y destruye la ventana emergente
        });

        // Evento que cierra la ventana si el usuario pulsa la "X" de arriba a la derecha
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dialog.dispose(); }
        });

        dialog.setVisible(true); // Hace visible la ventana del formulario (pausa el flujo por ser modal)
    }

    // Detecta qué elemento de la lista está seleccionado y lo borra
    private void eliminarSeleccionada() {
        int idx = listaAWT.getSelectedIndex(); // Devuelve la posición seleccionada (0, 1, 2...) o -1 si ninguna
        
        // Valida si no hay nada seleccionado o si el índice está fuera de los límites de la lista
        if (idx < 0 || categorias == null || idx >= categorias.size()) {
            mostrarMensaje("Selecciona una categoria para eliminar.");
            return;
        }
        
        // Obtiene el ID del objeto en esa posición y le ordena al DAO que lo borre de la BD
        dao.eliminar(categorias.get(idx).getId());
        recargar(); // Refresca la pantalla para que desaparezca el elemento
    }

    // Genera una ventana emergente simple de aviso (equivalente a un alert o JOptionPane)
    private void mostrarMensaje(String msg) {
        Frame parent = (Frame) BuscadorVentanas.getParentFrame(this);
        Dialog d = new Dialog(parent, "Aviso", true);
        d.setSize(300, 100);
        d.setLocationRelativeTo(parent);
        d.setLayout(new BorderLayout());
        
        Label lbl = new Label(msg, Label.CENTER);
        Button ok  = new Button("OK");
        ok.addActionListener(e -> d.dispose()); // El botón OK cierra la advertencia
        
        d.add(lbl, BorderLayout.CENTER);
        d.add(ok, BorderLayout.SOUTH);
        
        d.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { d.dispose(); }
        });
        d.setVisible(true);
    }

    // Método auxiliar abreviado para crear rápidamente etiquetas de texto estáticas
    private Label etiqueta(String texto) {
        return new Label(texto);
    }
}