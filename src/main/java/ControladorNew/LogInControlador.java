package ControladorNew;

import Modelo.Usuario;
import Service.UsuarioService;
import Service.impl.UsuarioServiceImpl;
import VistaNew.LogIn;
import VistaNew.Menu;
import VistaNew.RegistroView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class LogInControlador {

    private LogIn vista;
    private UsuarioService usuarioService;

    public LogInControlador() {
        this.vista = new LogIn();
        this.usuarioService = new UsuarioServiceImpl();
        configuracionListeners();
    }

    public void iniciarLogin() {
        vista.setVisible(true);
        vista.setLocationRelativeTo(null);
    }

    private void configuracionListeners() {
        vista.btn_ingresar.addActionListener((e) -> {
            login();
        });
        vista.txt_registrar.addActionListener((e) -> {
            abrirVentanaRegistro();
        });
    }

    private void login() {
        String email = vista.txt_usuario.getText();
        String password = new String(vista.txt_password.getPassword());
        if (!validarCampo(email, password)) {
            return;
        }

        Usuario usuario = usuarioService.validarLogin(email, password);

        if (usuario != null) {
            mostrarMensaje("LogIn exitoso!", "Exito!", JOptionPane.INFORMATION_MESSAGE);
            abrirVentanaPrincipal(usuario);

        } else {
            mostrarMensaje("Email o contraseña incorrectos", "Error", JOptionPane.ERROR);
        }
    }

    private boolean validarCampo(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Por favor, completar los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validarEmail(email)) {
            mostrarMensaje("Por favor, ingresar un email valido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String formatoEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern patron = Pattern.compile(formatoEmail);
        Matcher confirmar = patron.matcher(email.trim());
        return confirmar.matches();
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this.vista, mensaje, titulo, tipo);
    }

    private void abrirVentanaPrincipal(Usuario usuario) {
        MenuController menuController = new MenuController(usuario);
        vista.dispose();
        menuController.iniciarMenu();
    }

    private void abrirVentanaRegistro() {
        RegistroController registroController = new RegistroController();
        vista.dispose();
        registroController.iniciarRegistro();
    }

}
