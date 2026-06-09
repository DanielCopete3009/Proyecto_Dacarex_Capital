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
    private boolean modoOscuro = false;

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
        Panel sb = new Panel(new GridLayout(8, 1, 5, 5));
        sb.setPreferredSize(new Dimension(180, 0));
        sb.setBackground(new Color(30, 30, 45));

        Label lblLogo = new Label("DACAREX", Label.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 16));
        lblLogo.setForeground(new Color(100, 180, 255));
        sb.add(lblLogo);

        Label lblUser = new Label(usuarioActual.getNombre(), Label.CENTER);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 11));
        lblUser.setForeground(new Color(150, 150, 170));
        sb.add(lblUser);

        Button btnDash = new Button("Dashboard");
        btnDash.setBackground(new Color(45, 45, 65));
        btnDash.setForeground(Color.WHITE);
        btnDash.addActionListener(e -> mostrarPanel("dashboard"));
        sb.add(btnDash);

        Button btnMov = new Button("Movimientos");
        btnMov.setBackground(new Color(45, 45, 65));
        btnMov.setForeground(Color.WHITE);
        btnMov.addActionListener(e -> mostrarPanel("movimientos"));
        sb.add(btnMov);

        Button btnCat = new Button("Categorias");
        btnCat.setBackground(new Color(45, 45, 65));
        btnCat.setForeground(Color.WHITE);
        btnCat.addActionListener(e -> mostrarPanel("categorias"));
        sb.add(btnCat);

        // Modo oscuro
        Button btnTema = new Button("Modo oscuro");
        btnTema.setBackground(new Color(70, 70, 100));
        btnTema.setForeground(Color.WHITE);
        btnTema.addActionListener(e -> {
            modoOscuro = !modoOscuro;
            btnTema.setLabel(modoOscuro ? "Modo claro" : "Modo oscuro");
            aplicarTema();
        });
        sb.add(btnTema);

        // Espacio
        Label espacio = new Label("");
        sb.add(espacio);

        Button btnSalir = new Button("Cerrar sesion");
        btnSalir.setBackground(new Color(200, 60, 60));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.addActionListener(e -> {
            dispose();
            new VentanaLogin().setVisible(true);
        });
        sb.add(btnSalir);

        return sb;
    }

    private Panel crearDashboard() {
        Panel panel = new Panel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        Label lblTitulo = new Label("Dashboard", Label.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(30, 30, 45));
        panel.add(lblTitulo, BorderLayout.NORTH);

        Panel tarjetas = new Panel(new GridLayout(1, 3, 15, 0));
        tarjetas.setBackground(new Color(245, 247, 250));

        com.dacarex.capital.dao.MovimientoDAO dao =
            new com.dacarex.capital.dao.MovimientoDAO();

        double ingresos = dao.calcularTotalIngresos();
        double gastos   = dao.calcularTotalGastos();
        double saldo    = ingresos - gastos;

        tarjetas.add(crearTarjeta("Saldo",
            String.format("%.2f EUR", saldo),
            saldo >= 0 ? new Color(40, 167, 69) : new Color(220, 53, 69)));

        tarjetas.add(crearTarjeta("Ingresos",
            String.format("%.2f EUR", ingresos),
            new Color(0, 123, 255)));

        tarjetas.add(crearTarjeta("Gastos",
            String.format("%.2f EUR", gastos),
            new Color(220, 53, 69)));

        panel.add(tarjetas, BorderLayout.CENTER);
        return panel;
    }

    private Panel crearTarjeta(String titulo, String valor, Color color) {
        Panel tarjeta = new Panel(new GridLayout(2, 1));
        tarjeta.setBackground(Color.WHITE);

        Label lTitulo = new Label(titulo, Label.CENTER);
        lTitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        lTitulo.setForeground(new Color(100, 100, 120));

        Label lValor = new Label(valor, Label.CENTER);
        lValor.setFont(new Font("Arial", Font.BOLD, 22));
        lValor.setForeground(color);

        tarjeta.add(lTitulo);
        tarjeta.add(lValor);
        return tarjeta;
    }

    private void aplicarTema() {
        Color fondo = modoOscuro ? new Color(18, 18, 30) : new Color(245, 247, 250);
        panelContenido.setBackground(fondo);
        panelMovimientos.setBackground(fondo);
        panelCategorias.setBackground(fondo);
        repaint();
    }

    public void mostrarPanel(String nombre) {
        if (nombre.equals("movimientos")) panelMovimientos.recargar();
        if (nombre.equals("categorias"))  panelCategorias.recargar();
        cardLayout.show(panelContenido, nombre);
    }
}