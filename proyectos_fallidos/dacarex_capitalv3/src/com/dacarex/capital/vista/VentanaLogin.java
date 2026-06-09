package com.dacarex.capital.vista;

import com.dacarex.capital.dao.ConexionBD;
import com.dacarex.capital.dao.UsuarioDAO;
import com.dacarex.capital.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class VentanaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistrar;
    private JLabel lblEstado;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public VentanaLogin() {
        inicializarVentana();
        inicializarComponentes();
        inicializarEventos();
    }

    private void inicializarVentana() {
        setTitle("Dacarex Capital — Login");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(30, 30, 45));
    }

    private void inicializarComponentes() {
        setLayout(null);

        // Título
        JLabel lblTitulo = new JLabel("DACAREX CAPITAL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(100, 180, 255));
        lblTitulo.setBounds(0, 20, 420, 35);
        add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Gestión Financiera", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(150, 150, 170));
        lblSubtitulo.setBounds(0, 55, 420, 20);
        add(lblSubtitulo);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        lblEmail.setBounds(60, 95, 80, 25);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(60, 120, 300, 32);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        txtEmail.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 255)));
        add(txtEmail);

        // Contraseña
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setFont(new Font("Arial", Font.PLAIN, 13));
        lblPass.setBounds(60, 162, 100, 25);
        add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(60, 187, 300, 32);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 255)));
        add(txtPassword);

        // Botones
        btnLogin = new JButton("Entrar");
        btnLogin.setBounds(60, 235, 140, 35);
        btnLogin.setBackground(new Color(100, 180, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(btnLogin);

        btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setBounds(220, 235, 140, 35);
        btnRegistrar.setBackground(new Color(60, 60, 80));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(btnRegistrar);

        // Estado
        lblEstado = new JLabel("", SwingConstants.CENTER);
        lblEstado.setForeground(new Color(255, 100, 100));
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 11));
        lblEstado.setBounds(0, 278, 420, 20);
        add(lblEstado);
    }

    private void inicializarEventos() {

        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass  = new String(txtPassword.getPassword());

            if (email.isEmpty() || pass.isEmpty()) {
                lblEstado.setText("Por favor rellena todos los campos.");
                return;
            }

            Optional<Usuario> usuario = usuarioDAO.buscarPorEmail(email);

            if (usuario.isEmpty() || !usuario.get().getContrasenia().equals(pass)) {
                lblEstado.setText("Email o contraseña incorrectos.");
                return;
            }

            dispose();
            new VentanaPrincipal(usuario.get()).setVisible(true);
        });

        btnRegistrar.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre completo:");
            if (nombre == null || nombre.isBlank()) return;

            String email = JOptionPane.showInputDialog(this, "Email:");
            if (email == null || !email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Email no válido.");
                return;
            }

            String pass = JOptionPane.showInputDialog(this, "Contraseña (mín. 8 caracteres):");
            if (pass == null || pass.length() < 8) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 8 caracteres.");
                return;
            }

            if (usuarioDAO.buscarPorEmail(email).isPresent()) {
                JOptionPane.showMessageDialog(this, "Ya existe una cuenta con ese email.");
                return;
            }

            com.dacarex.capital.enums.TipoCuenta tipo =
                JOptionPane.showConfirmDialog(this, "¿Es una cuenta de empresa?",
                    "Tipo de cuenta", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION
                ? com.dacarex.capital.enums.TipoCuenta.EMPRESA
                : com.dacarex.capital.enums.TipoCuenta.PERSONAL;

            Usuario nuevo = new Usuario(0, nombre, email, pass, tipo);
            usuarioDAO.guardar(nuevo);
            JOptionPane.showMessageDialog(this, "✔ Cuenta creada. Ya puedes iniciar sesión.");
        });

        // Enter en el campo contraseña lanza el login
        txtPassword.addActionListener(e -> btnLogin.doClick());
    }
}