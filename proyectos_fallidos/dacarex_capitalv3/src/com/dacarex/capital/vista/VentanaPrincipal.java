package com.dacarex.capital.vista;

import com.dacarex.capital.modelo.Usuario;
import com.dacarex.capital.util.TemaManager;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private final Usuario usuarioActual;
    private JPanel panelContenido;
    private PanelMovimientos panelMovimientos;
    private PanelCategorias panelCategorias;
    private JPanel sidebar;
    private JButton btnTema;

    public VentanaPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        inicializarVentana();
        inicializarComponentes();
    }

    private void inicializarVentana() {
        setTitle("Dacarex Capital — " + usuarioActual.getNombre());
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        sidebar = crearSidebar();
        add(sidebar, BorderLayout.WEST);

        panelContenido = new JPanel(new CardLayout());
        panelContenido.setBackground(TemaManager.getFondoPrincipal());

        panelMovimientos = new PanelMovimientos();
        panelCategorias  = new PanelCategorias();
        JPanel panelDashboard = crearPanelDashboard();

        panelContenido.add(panelDashboard,   "dashboard");
        panelContenido.add(panelMovimientos, "movimientos");
        panelContenido.add(panelCategorias,  "categorias");

        add(panelContenido, BorderLayout.CENTER);

        mostrarPanel("dashboard");
    }

    private JPanel crearSidebar() {
        JPanel sb = new JPanel();
        sb.setPreferredSize(new Dimension(200, 0));
        sb.setBackground(TemaManager.getFondoSidebar());
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));

        JLabel lblLogo = new JLabel("💰 Dacarex", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 18));
        lblLogo.setForeground(TemaManager.getAcento());
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(25, 0, 5, 0));
        sb.add(lblLogo);

        JLabel lblUsuario = new JLabel(usuarioActual.getNombre(), SwingConstants.CENTER);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 11));
        lblUsuario.setForeground(TemaManager.getTextoSubtitulo());
        lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        sb.add(lblUsuario);

        sb.add(crearBotonNav("🏠  Dashboard",   "dashboard"));
        sb.add(crearBotonNav("💸  Movimientos", "movimientos"));
        sb.add(crearBotonNav("🗂️  Categorías",  "categorias"));

        sb.add(Box.createVerticalGlue());

        // Botón tema
        btnTema = new JButton(TemaManager.getIconoTema());
        btnTema.setMaximumSize(new Dimension(180, 38));
        btnTema.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTema.setBackground(new Color(70, 70, 100));
        btnTema.setForeground(Color.WHITE);
        btnTema.setFont(new Font("Arial", Font.PLAIN, 12));
        btnTema.setFocusPainted(false);
        btnTema.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTema.setBorderPainted(false);
        btnTema.addActionListener(e -> cambiarTema());
        sb.add(btnTema);
        sb.add(Box.createVerticalStrut(10));

        // Botón cerrar sesión
        JButton btnSalir = new JButton("⬅  Cerrar sesión");
        btnSalir.setMaximumSize(new Dimension(180, 38));
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setBackground(new Color(200, 60, 60));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 12));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalir.setBorderPainted(false);
        btnSalir.addActionListener(e -> {
            dispose();
            new VentanaLogin().setVisible(true);
        });
        sb.add(btnSalir);
        sb.add(Box.createVerticalStrut(20));

        return sb;
    }

    private JButton crearBotonNav(String texto, String panel) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(180, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(45, 45, 65));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(false);
        btn.addActionListener(e -> mostrarPanel(panel));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(TemaManager.getAcento());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(45, 45, 65));
            }
        });

        return btn;
    }

    private JPanel crearPanelDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(TemaManager.getFondoPrincipal());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("Dashboard");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(TemaManager.getTextoTitulo());
        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 20, 0));
        tarjetas.setBackground(TemaManager.getFondoPrincipal());
        tarjetas.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

        com.dacarex.capital.servicio.MovimientoServicio servicio =
            new com.dacarex.capital.servicio.MovimientoServicio();

        double ingresos = servicio.calcularTotalIngresos();
        double gastos   = servicio.calcularTotalGastos();
        double saldo    = servicio.calcularSaldo();

        tarjetas.add(crearTarjeta("💰 Saldo",
            String.format("%.2f €", saldo),
            saldo >= 0 ? new Color(40, 167, 69) : new Color(220, 53, 69)));

        tarjetas.add(crearTarjeta("📈 Ingresos",
            String.format("%.2f €", ingresos),
            new Color(0, 123, 255)));

        tarjetas.add(crearTarjeta("📉 Gastos",
            String.format("%.2f €", gastos),
            new Color(220, 53, 69)));

        panel.add(tarjetas, BorderLayout.CENTER);

        JLabel lblInfo = new JLabel("Categoría más activa: "
            + servicio.obtenerCategoriaMasActiva());
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 13));
        lblInfo.setForeground(TemaManager.getTextoSubtitulo());
        panel.add(lblInfo, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearTarjeta(String titulo, String valor, Color color) {
        JPanel tarjeta = new JPanel(new GridLayout(2, 1));
        tarjeta.setBackground(TemaManager.getFondoPanel());
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TemaManager.getBorde()),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitulo.setForeground(TemaManager.getTextoSubtitulo());

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 26));
        lblValor.setForeground(color);

        tarjeta.add(lblTitulo);
        tarjeta.add(lblValor);
        return tarjeta;
    }

    private void cambiarTema() {
        TemaManager.toggleTema();
        btnTema.setText(TemaManager.getIconoTema());

        // Reconstruir la ventana con el nuevo tema
        getContentPane().removeAll();
        inicializarComponentes();
        revalidate();
        repaint();
    }

    public void mostrarPanel(String nombre) {
        if (nombre.equals("movimientos")) panelMovimientos.recargar();
        if (nombre.equals("categorias"))  panelCategorias.recargar();
        CardLayout cl = (CardLayout) panelContenido.getLayout();
        cl.show(panelContenido, nombre);
    }
}