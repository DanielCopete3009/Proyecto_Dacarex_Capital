package com.dacarex.capital.vista;

import com.dacarex.capital.dao.ConexionDB;
import com.dacarex.capital.dao.UsuarioDAO;
import com.dacarex.capital.modelo.Usuario;

import java.awt.*;
import java.awt.event.*;
import java.util.Optional;

public class VentanaLogin extends Frame {

    private TextField txtEmail;
    private TextField txtPassword;
    private Button btnLogin;
    private Button btnRegistrar;
    private Label lblEstado;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public VentanaLogin() {
        inicializarVentana();
        inicializarComponentes();
        inicializarEventos();
    }

    private void inicializarVentana() {
        setTitle("Dacarex Capital - Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Cerramos la base de datos de manera limpia y ordenada
                ConexionDB.getInstance().cerrar();
                System.out.println("Base de datos cerrada limpiamente al salir.");
                
                // Terminamos con la ejecución del programa
                System.exit(0);
            }
        });
    }

    private void inicializarComponentes() {

        // Titulo
        Label lblTitulo = new Label("DACAREX CAPITAL", Label.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setBounds(0, 20, 400, 35);
        add(lblTitulo);

        Label lblSub = new Label("Gestion Financiera", Label.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setBounds(0, 55, 400, 20);
        add(lblSub);

        // Email
        Label lblEmail = new Label("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        lblEmail.setBounds(60, 90, 80, 25);
        add(lblEmail);

        txtEmail = new TextField();
        txtEmail.setBounds(60, 115, 280, 28);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        add(txtEmail);

        // Password
        Label lblPass = new Label("Contrasenia:");
        lblPass.setFont(new Font("Arial", Font.PLAIN, 13));
        lblPass.setBounds(60, 153, 100, 25);
        add(lblPass);

        txtPassword = new TextField();
        txtPassword.setEchoChar('*');
        txtPassword.setBounds(60, 178, 280, 28);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        add(txtPassword);

        // Botones
        btnLogin = new Button("Entrar");
        btnLogin.setBounds(60, 220, 120, 32);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        add(btnLogin);

        btnRegistrar = new Button("Registrarse");
        btnRegistrar.setBounds(220, 220, 120, 32);
        btnRegistrar.setFont(new Font("Arial", Font.PLAIN, 13));
        add(btnRegistrar);

        // Estado
        lblEstado = new Label("", Label.CENTER);
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 11));
        lblEstado.setBounds(0, 260, 400, 20);
        add(lblEstado);
    }

    private void inicializarEventos() {

        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass  = txtPassword.getText().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                lblEstado.setText("Rellena todos los campos.");
                return;
            }

            Optional<Usuario> usuario = usuarioDAO.buscarPorEmail(email);

            if (usuario.isEmpty() || !usuario.get().getContrasenia().equals(pass)) {
                lblEstado.setText("Email o contrasenia incorrectos.");
                return;
            }

            dispose();
            new VentanaPrincipal(usuario.get()).setVisible(true);
        });

        btnRegistrar.addActionListener(e -> {
            Dialog dialog = new Dialog(this, "Registro", true);
            dialog.setSize(320, 230);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(null);

            Label lNombre = new Label("Nombre:");
            lNombre.setBounds(20, 20, 80, 25);
            dialog.add(lNombre);

            TextField tNombre = new TextField();
            tNombre.setBounds(110, 20, 180, 25);
            dialog.add(tNombre);

            Label lEmail = new Label("Email:");
            lEmail.setBounds(20, 60, 80, 25);
            dialog.add(lEmail);

            TextField tEmail = new TextField();
            tEmail.setBounds(110, 60, 180, 25);
            dialog.add(tEmail);

            Label lPass = new Label("Contrasenia:");
            lPass.setBounds(20, 100, 100, 25);
            dialog.add(lPass);

            TextField tPass = new TextField();
            tPass.setEchoChar('*');
            tPass.setBounds(110, 100, 180, 25);
            dialog.add(tPass);

            Label lError = new Label("", Label.CENTER);
            lError.setBounds(0, 140, 320, 20);
            dialog.add(lError);

            Button btnOk = new Button("Registrar");
            btnOk.setBounds(80, 170, 160, 32);
            dialog.add(btnOk);

            btnOk.addActionListener(ev -> {
                String nombre = tNombre.getText().trim();
                String email2 = tEmail.getText().trim();
                String pass2  = tPass.getText().trim();

                if (nombre.isEmpty() || email2.isEmpty() || pass2.isEmpty()) {
                    lError.setText("Rellena todos los campos.");
                    return;
                }
                if (!email2.contains("@")) {
                    lError.setText("Email no valido.");
                    return;
                }
                if (pass2.length() < 6) {
                    lError.setText("Contrasenia minimo 6 caracteres.");
                    return;
                }
                if (usuarioDAO.buscarPorEmail(email2).isPresent()) {
                    lError.setText("Email ya registrado.");
                    return;
                }
                usuarioDAO.guardar(new Usuario(nombre, email2, pass2));
                dialog.dispose();
                lblEstado.setText("Cuenta creada. Ya puedes entrar.");
            });

            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    dialog.dispose();
                }
            });

            dialog.setVisible(true);
        });

        txtPassword.addActionListener(e -> btnLogin.getActionListeners()[0]
                .actionPerformed(null));
    }
}