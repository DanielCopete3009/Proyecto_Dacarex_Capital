package com.dacarex.capital.vista;

import com.dacarex.capital.modelo.Usuario;

import java.awt.*;
import java.awt.event.*;

public class VentanaPrincipal extends Frame {

    private final Usuario usuarioActual;
    private Panel panelContenido;
    private PanelMovimientos panelMovimientos;
    private PanelCategorias panelCategorias;
    private CardLayout cardLayout;

    public VentanaPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        inicializarVentana();
        inicializarComponentes();
    }

    private void inicializarVentana() {
        setTitle("Dacarex Capital - " + usuarioActual.getNombre());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // Cerramos la base de datos de manera limpia al salir desde aquí también
                com.dacarex.capital.dao.ConexionDB.getInstance().cerrar();
                System.exit(0);
            }
        });
    }

    private void inicializarComponentes() {
        // Sidebar
        Panel sidebar = crearSidebar();
        add(sidebar, BorderLayout.WEST);

        // Contenido
        cardLayout     = new CardLayout();
        panelContenido = new Panel(cardLayout);

        panelMovimientos = new PanelMovimientos();
        panelCategorias  = new PanelCategorias();
        Panel dashboard  = crearDashboard();

        panelContenido.add(dashboard,        "dashboard");
        panelContenido.add(panelMovimientos, "movimientos");
        panelContenido.add(panelCategorias,  "categorias");

        add(panelContenido, BorderLayout.CENTER);
        mostrarPanel("dashboard");
    }

    private Panel crearSidebar() {
        Panel sb = new Panel(new GridLayout(7, 1, 5, 5));
        sb.setPreferredSize(new Dimension(180, 0));

        Label lblLogo = new Label("DACAREX", Label.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 16));
        sb.add(lblLogo);

        Label lblUser = new Label(usuarioActual.getNombre(), Label.CENTER);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 11));
        sb.add(lblUser);

        Button btnDash = new Button("Dashboard");
        btnDash.addActionListener(e -> mostrarPanel("dashboard"));
        sb.add(btnDash);

        Button btnMov = new Button("Movimientos");
        btnMov.addActionListener(e -> mostrarPanel("movimientos"));
        sb.add(btnMov);

        Button btnCat = new Button("Categorias");
        btnCat.addActionListener(e -> mostrarPanel("categorias"));
        sb.add(btnCat);

        // Espacio
        Label espacio = new Label("");
        sb.add(espacio);

        Button btnSalir = new Button("Cerrar sesion");
        btnSalir.addActionListener(e -> {
            dispose();
            new VentanaLogin().setVisible(true);
        });
        sb.add(btnSalir);

        return sb;
    }

    private Panel crearDashboard() {
        Panel panel = new Panel(new BorderLayout());

        Label lblTitulo = new Label("Dashboard", Label.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(lblTitulo, BorderLayout.NORTH);

        Panel tarjetas = new Panel(new GridLayout(1, 3, 15, 0));

        com.dacarex.capital.dao.MovimientoDAO dao =
            new com.dacarex.capital.dao.MovimientoDAO();

        double ingresos = dao.calcularTotalIngresos();
        double gastos   = dao.calcularTotalGastos();
        double saldo    = ingresos - gastos;

        tarjetas.add(crearTarjeta("Saldo", String.format("%.2f EUR", saldo)));
        tarjetas.add(crearTarjeta("Ingresos", String.format("%.2f EUR", ingresos)));
        tarjetas.add(crearTarjeta("Gastos", String.format("%.2f EUR", gastos)));

        panel.add(tarjetas, BorderLayout.CENTER);
        return panel;
    }

    private Panel crearTarjeta(String titulo, String valor) {
        Panel tarjeta = new Panel(new GridLayout(2, 1));

        Label lTitulo = new Label(titulo, Label.CENTER);
        lTitulo.setFont(new Font("Arial", Font.PLAIN, 13));

        Label lValor = new Label(valor, Label.CENTER);
        lValor.setFont(new Font("Arial", Font.BOLD, 22));

        tarjeta.add(lTitulo);
        tarjeta.add(lValor);
        return tarjeta;
    }

    public void mostrarPanel(String nombre) {
        if (nombre.equals("movimientos")) panelMovimientos.recargar();
        if (nombre.equals("categorias"))  panelCategorias.recargar();
        cardLayout.show(panelContenido, nombre);
    }
}