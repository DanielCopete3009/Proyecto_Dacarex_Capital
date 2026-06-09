package com.dacarex.capital.vista;

import com.dacarex.capital.modelo.Usuario;

import java.awt.*;
import java.awt.event.*;

// VentanaPrincipal es el marco contenedor de la aplicación una vez que pasas el Login
public class VentanaPrincipal extends Frame {

    private final Usuario usuarioActual; // Almacena el usuario que inició sesión para personalizar el saludo
    private Panel panelContenido;        // El espacio del centro que cambiará de pantalla
    private PanelMovimientos panelMovimientos; // Pantalla de historial y registro de dinero
    private PanelCategorias panelCategorias;   // Pantalla de gestión de etiquetas
    private CardLayout cardLayout;       // El gestor que permite apilar paneles y cambiarlos como cartas
    
    // Declaramos las etiquetas del Dashboard a nivel de clase para poder actualizar sus textos dinámicamente
    private Label lblValSaldo;
    private Label lblValIngresos;
    private Label lblValGastos;

    // Constructor: Recibe los datos del usuario logueado y monta toda la interfaz
    public VentanaPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        inicializarVentana();
        inicializarComponentes();
    }

    // Configura la ventana exterior (tamaño, título y cierre seguro)
    private void inicializarVentana() {
        setTitle("Dacarex Capital - " + usuarioActual.getNombre());
        setSize(900, 600); // Ventana espaciosa de 900x600 píxeles
        setLocationRelativeTo(null); // Centra la ventana en tu monitor
        setLayout(new BorderLayout()); // Divide la ventana en 5 regiones (Norte, Sur, Este, Oeste, Centro)

        // Escuchador para cuando el usuario cierra el programa con la "X"
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // Cierra la conexión al archivo .odb de ObjectDB para que no se corrompan los datos
                com.dacarex.capital.dao.ConexionDB.getInstance().cerrar();
                System.exit(0); // Apaga el proceso de Java por completo
            }
        });
    }

    // Crea la estructura principal: Menú a la izquierda y Pantallas en el centro
    private void inicializarComponentes() {
        // 1. Crea la barra lateral de botones y la coloca en la zona OESTE (izquierda)
        Panel sidebar = crearSidebar();
        add(sidebar, BorderLayout.WEST);

        // 2. Prepara el contenedor CENTRAL y le aplica el truco de la baraja de cartas (CardLayout)
        cardLayout     = new CardLayout();
        panelContenido = new Panel(cardLayout);

        // 3. Fabricamos las tres pantallas que albergará nuestra aplicación
        panelMovimientos = new PanelMovimientos();
        panelCategorias  = new PanelCategorias();
        Panel dashboard  = crearDashboard();

        // 4. Metemos las pantallas dentro de la baraja y les asignamos un "nombre clave" para llamarlas luego
        panelContenido.add(dashboard,        "dashboard");
        panelContenido.add(panelMovimientos, "movimientos");
        panelContenido.add(panelCategorias,  "categorias");

        // 5. Encajamos el contenedor de cartas en el CENTRO de la ventana
        add(panelContenido, BorderLayout.CENTER);
        
        // 6. Al abrir el programa, muestra la pantalla del Dashboard por defecto
        mostrarPanel("dashboard");
    }

    // Diseña el menú lateral izquierdo con un layout de rejilla (7 filas, 1 columna)
    private Panel crearSidebar() {
        Panel sb = new Panel(new GridLayout(7, 1, 5, 5)); // 7 huecos verticales, 5px de separación
        sb.setPreferredSize(new Dimension(180, 0)); // Forzamos a que mida exactamente 180 píxeles de ancho

        // Fila 1: Logo de la app
        Label lblLogo = new Label("DACAREX", Label.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 16));
        sb.add(lblLogo);

        // Fila 2: Nombre del usuario actual que está usando la app
        Label lblUser = new Label(usuarioActual.getNombre(), Label.CENTER);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 11));
        sb.add(lblUser);

        // Fila 3: Botón para ir al inicio (Dashboard)
        Button btnDash = new Button("Dashboard");
        btnDash.addActionListener(e -> mostrarPanel("dashboard"));
        sb.add(btnDash);

        // Fila 4: Botón para ir a las transacciones
        Button btnMov = new Button("Movimientos");
        btnMov.addActionListener(e -> mostrarPanel("movimientos"));
        sb.add(btnMov);

        // Fila 5: Botón para ir a las categorías
        Button btnCat = new Button("Categorias");
        btnCat.addActionListener(e -> mostrarPanel("categorias"));
        sb.add(btnCat);

        // Fila 6: Un hueco en blanco para separar estéticamente los botones del botón de salir
        Label espacio = new Label("");
        sb.add(espacio);

        // Fila 7: Botón para desconectarse
        Button btnSalir = new Button("Cerrar sesion");
        btnSalir.addActionListener(e -> {
            dispose(); // Destruye esta ventana principal de la pantalla
            new VentanaLogin().setVisible(true); // Te devuelve a la ventanita de Login
        });
        sb.add(btnSalir);

        return sb;
    }

    // Diseña la pantalla inicial del Dashboard (Resumen financiero de bienvenida)
    private Panel crearDashboard() {
        Panel panel = new Panel(new BorderLayout());

        // Título de la pantalla en la zona Norte
        Label lblTitulo = new Label("Dashboard", Label.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // Contenedor central para las tres tarjetas de datos alineadas horizontalmente (1 fila, 3 columnas)
        Panel tarjetas = new Panel(new GridLayout(1, 3, 15, 0)); // 15px de separación horizontal entre tarjetas

        // Creamos visualmente las tres tarjetas vacías
        tarjetas.add(crearTarjeta("Saldo"));
        tarjetas.add(crearTarjeta("Ingresos"));
        tarjetas.add(crearTarjeta("Gastos"));

        panel.add(tarjetas, BorderLayout.CENTER);
        return panel;
    }

    // Método auxiliar para construir una cajita visual (tarjeta) con su título y valor centralizado
    private Panel crearTarjeta(String tipoTarjeta) {
        Panel tarjeta = new Panel(new GridLayout(2, 1)); // 2 filas (arriba título, abajo el número)

        Label lTitulo = new Label(tipoTarjeta, Label.CENTER);
        lTitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        tarjeta.add(lTitulo);

        Label lValor = new Label("0.00 EUR", Label.CENTER);
        lValor.setFont(new Font("Arial", Font.BOLD, 22));
        tarjeta.add(lValor);

        // Asignamos las variables de control a las etiquetas correspondientes para poder cambiarlas más tarde
        if (tipoTarjeta.equals("Saldo"))    lblValSaldo = lValor;
        if (tipoTarjeta.equals("Ingresos")) lblValIngresos = lValor;
        if (tipoTarjeta.equals("Gastos"))   lblValGastos = lValor;

        return tarjeta;
    }

    // NUEVO MÉTODO: Consulta a la Base de Datos los totales financieros y los pinta en el Dashboard
    private void actualizarDashboard() {
        com.dacarex.capital.dao.MovimientoDAO dao = new com.dacarex.capital.dao.MovimientoDAO();

        double ingresos = dao.calcularTotalIngresos();
        double gastos   = dao.calcularTotalGastos();
        double saldo    = ingresos - gastos;

        // Inyecta los textos formateados a dos decimales en las etiquetas correspondientes
        lblValSaldo.setText(String.format("%.2f EUR", saldo));
        lblValIngresos.setText(String.format("%.2f EUR", ingresos));
        lblValGastos.setText(String.format("%.2f EUR", gastos));
    }

    // Este método mágico se encarga de cambiar los paneles centrales y obligar a que se actualicen sus datos
    public void mostrarPanel(String nombre) {
        // MODIFICADO: Si el usuario pulsa en el Dashboard, recalculamos el dinero en tiempo real
        if (nombre.equals("dashboard"))   actualizarDashboard();
        if (nombre.equals("movimientos")) panelMovimientos.recargar();
        if (nombre.equals("categorias"))  panelCategorias.recargar();
        
        // Le dice al layout de cartas: "Pon encima de la mesa el panel con este nombre clave"
        cardLayout.show(panelContenido, nombre);
    }
}