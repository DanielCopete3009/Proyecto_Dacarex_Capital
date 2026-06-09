package com.dacarex.capital.vista;

import com.dacarex.capital.dao.ConexionDB;
import com.dacarex.capital.dao.UsuarioDAO;
import com.dacarex.capital.modelo.Usuario;

import java.awt.*;
import java.awt.event.*;
import java.util.Optional;

// VentanaLogin es la pantalla de acceso inicial que hereda de Frame (Ventana nativa)
public class VentanaLogin extends Frame {

    private TextField txtEmail;
    private TextField txtPassword;
    private Button btnLogin;
    private Button btnRegistrar;
    private Label lblEstado;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO(); // Conector a la tabla de Usuarios

    // Constructor: Inicializa la configuración, los componentes visuales y las acciones
    public VentanaLogin() {
        inicializarVentana();
        inicializarComponentes();
        inicializarEventos();
    }

    // Configura los parámetros generales de la ventana de Login
    private void inicializarVentana() {
        setTitle("Dacarex Capital - Login");
        setSize(400, 320); // Incrementado ligeramente de 300 a 320 para dar más aire abajo
        setLocationRelativeTo(null); // Centra la ventana en mitad de la pantalla
        setResizable(false);         // Bloquea el botón de maximizar
        setLayout(null);             // Desactiva el layout automático para usar posiciones fijas (X, Y)

        // Evento para gestionar el cierre de la ventana desde la "X"
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

    // Crea y posiciona cada texto, caja de entrada y botón en la pantalla
    private void inicializarComponentes() {

        // ── TITULO PRINCIPAL (Corregido: bajado de Y=20 a Y=40 para que no lo corte la barra de Windows/Mac) ──
        Label lblTitulo = new Label("DACAREX CAPITAL", Label.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setBounds(0, 40, 400, 35); // (X, Y, Ancho, Alto)
        add(lblTitulo);

        // Subtítulo (Bajado de Y=55 a Y=75 de forma proporcional)
        Label lblSub = new Label("Gestion Financiera", Label.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setBounds(0, 75, 400, 20);
        add(lblSub);

        // ── CAMPO EMAIL ──
        Label lblEmail = new Label("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        lblEmail.setBounds(60, 105, 80, 25);
        add(lblEmail);

        txtEmail = new TextField();
        txtEmail.setBounds(60, 130, 280, 28);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        add(txtEmail);

        // ── CAMPO CONTRASEÑA ──
        Label lblPass = new Label("Contrasenia:");
        lblPass.setFont(new Font("Arial", Font.PLAIN, 13));
        lblPass.setBounds(60, 165, 100, 25);
        add(lblPass);

        txtPassword = new TextField();
        txtPassword.setEchoChar('*'); // Oculta el texto escribiendo asteriscos por privacidad
        txtPassword.setBounds(60, 190, 280, 28);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        add(txtPassword);

        // ── BOTONES DE ACCIÓN ──
        btnLogin = new Button("Entrar");
        btnLogin.setBounds(60, 235, 120, 32);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        add(btnLogin);

        btnRegistrar = new Button("Registrarse");
        btnRegistrar.setBounds(220, 235, 120, 32);
        btnRegistrar.setFont(new Font("Arial", Font.PLAIN, 13));
        add(btnRegistrar);

        // ── MENSANJES DE ERROR O ESTADO ──
        lblEstado = new Label("", Label.CENTER);
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 11));
        lblEstado.setBounds(0, 275, 400, 20);
        add(lblEstado);
    }

    // Define qué hace el programa cuando interactúas con los componentes
    private void inicializarEventos() {

        // ACCIÓN DEL BOTÓN "ENTRAR"
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass  = txtPassword.getText().trim();

            // Validación de campos vacíos
            if (email.isEmpty() || pass.isEmpty()) {
                lblEstado.setText("Rellena todos los campos.");
                return;
            }

            // Busca el usuario en ObjectDB a través de su correo
            Optional<Usuario> usuario = usuarioDAO.buscarPorEmail(email);

            // Comprueba si el usuario no existe o si la contraseña grabada no coincide
            if (usuario.isEmpty() || !usuario.get().getContrasenia().equals(pass)) {
                lblEstado.setText("Email o contrasenia incorrectos.");
                return;
            }

            // Si las credenciales son correctas: cierra el Login y abre la Ventana Principal pasándole el usuario
            dispose();
            new VentanaPrincipal(usuario.get()).setVisible(true);
        });

        // ACCIÓN DEL BOTÓN "REGISTRARSE" (Abre un cuadro de diálogo emergente)
        btnRegistrar.addActionListener(e -> {
            Dialog dialog = new Dialog(this, "Registro", true); // true = Modal (Bloquea el login detrás)
            dialog.setSize(320, 230);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(null); // Posicionamiento manual para el formulario de registro

            // Formulario interno: Nombre
            Label lNombre = new Label("Nombre:");
            lNombre.setBounds(20, 40, 80, 25);
            dialog.add(lNombre);

            TextField tNombre = new TextField();
            tNombre.setBounds(110, 40, 180, 25);
            dialog.add(tNombre);

            // Formulario interno: Email
            Label lEmail = new Label("Email:");
            lEmail.setBounds(20, 75, 80, 25);
            dialog.add(lEmail);

            TextField tEmail = new TextField();
            tEmail.setBounds(110, 75, 180, 25);
            dialog.add(tEmail);

            // Formulario interno: Contraseña
            Label lPass = new Label("Contrasenia:");
            lPass.setBounds(20, 110, 100, 25);
            dialog.add(lPass);

            TextField tPass = new TextField();
            tPass.setEchoChar('*');
            tPass.setBounds(110, 110, 180, 25);
            dialog.add(tPass);

            // Alerta de error interna del formulario
            Label lError = new Label("", Label.CENTER);
            lError.setBounds(0, 145, 320, 20);
            dialog.add(lError);

            // Botón de confirmación de registro
            Button btnOk = new Button("Registrar");
            btnOk.setBounds(80, 175, 160, 32);
            dialog.add(btnOk);

            // Lógica para guardar el nuevo usuario
            btnOk.addActionListener(ev -> {
                String nombre = tNombre.getText().trim();
                String email2 = tEmail.getText().trim();
                String pass2  = tPass.getText().trim();

                // Validaciones de seguridad básicas
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
                // Comprueba que no exista otra cuenta con el mismo correo electrónico
                if (usuarioDAO.buscarPorEmail(email2).isPresent()) {
                    lError.setText("Email ya registrado.");
                    return;
                }
                
                // Guarda el usuario en la base de datos a través del DAO
                usuarioDAO.guardar(new Usuario(nombre, email2, pass2));
                dialog.dispose(); // Cierra el cuadro de registro
                lblEstado.setText("Cuenta creada. Ya puedes entrar.");
            });

            // Cierre manual del cuadro de diálogo con la "X"
            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    dialog.dispose();
                }
            });

            dialog.setVisible(true); // Muestra el formulario
        });

        // COMODIDAD: Si pulsas "Enter" dentro de la caja de contraseña, simula hacer clic en "Entrar"
        txtPassword.addActionListener(e -> btnLogin.getActionListeners()[0]
                .actionPerformed(null));
    }
}