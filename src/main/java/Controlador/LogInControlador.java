package Controlador;

import Modelo.Usuario;
import Service.UsuarioService;
import Service.impl.UsuarioServiceImpl;
import VistaNew.LogIn;

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
//        String email = "ab@gmail.com";
//        String password = "123456";

        ValidacionesLogeoRegistro validacionesLogeoRegistro = new ValidacionesLogeoRegistro();

        if (!validacionesLogeoRegistro.validarCampo(email, password)) {
            return;
        }

        Usuario usuario = usuarioService.validarLogin(email, password);

        if (usuario != null) {
            validacionesLogeoRegistro.mostrarMensaje("LogIn exitoso!", "Exito!", JOptionPane.INFORMATION_MESSAGE);
            abrirVentanaPrincipal(usuario);

        } else {
            validacionesLogeoRegistro.mostrarMensaje("Email o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirVentanaPrincipal(Usuario usuario) {
        vista.dispose();
        new MenuController(usuario).iniciarMenu();
    }

    private void abrirVentanaRegistro() {
        vista.dispose();
        new RegistroController().iniciarRegistro();
    }

}
