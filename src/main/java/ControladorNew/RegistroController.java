package ControladorNew;

import Modelo.Usuario;
import Service.UsuarioService;
import Service.impl.UsuarioServiceImpl;
import VistaNew.RegisterView;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class RegistroController {
    
    private RegisterView view;
    private UsuarioService service;
    
    public RegistroController() {
        this.view = new RegisterView();
        this.service = new UsuarioServiceImpl();
        this.configuracionListeners();
    }
    
    public void iniciarRegistro() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }
    
    private void configuracionListeners() {
        view.btn_registrar.addActionListener(e -> registrar());
        view.btn_login.addActionListener(e -> abrirLogin());
    }
    
    private void registrar() {
        String nombres = view.txt_nombres.getText();
        String apellidos = view.txt_apellidos.getText();
        String email = view.txt_email.getText();
        String password = new String(view.txt_password.getPassword());
        String telefono = view.txt_telefono.getText();
        
        String direccion = view.txt_direccion.getText();
        
        if (!validarCampos(nombres, apellidos, email, password)) {
            return;
        }
        if (!parciarTelefono(telefono)) {
            return;
        }
        
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombres(nombres);
        nuevoUsuario.setApellidos(apellidos);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPasswordHash(password);
        nuevoUsuario.setTelefono(Integer.parseInt(telefono));
        nuevoUsuario.setDireccion(direccion);
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());
        nuevoUsuario.setEstado(true);
        
        try {
            service.registrarUsuario(nuevoUsuario);
            mostrarMensaje("Registro exitoso!", "Exito!", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            abrirLogin();
        } catch (Exception e) {
            mostrarMensaje("Error al registrar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private boolean validarCampos(String nombres, String apellidos, String email, String password) {
        if (nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Por favor, completar los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validarEmail(email)) {
            mostrarMensaje("Por favor, ingresar un email valido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validar(nombres)) {
            mostrarMensaje("Por favor, ingresar un nombre valido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validar(apellidos)) {
            mostrarMensaje("Por favor, ingresar un apellido valido", "Error", JOptionPane.ERROR_MESSAGE);
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
    
    private boolean validar(String campo) {
        if (campo == null || campo.trim().isEmpty()) {
            return false;
        }
        String formatName = "^[A-Za-z]{2,}$";
        Pattern patron = Pattern.compile(formatName);
        Matcher confirmar = patron.matcher(campo.trim());
        return confirmar.matches();
    }
    
    private void limpiarCampos() {
        view.txt_nombres.setText("");
        view.txt_apellidos.setText("");
        view.txt_email.setText("");
        view.txt_password.setText("");
        view.txt_telefono.setText("");
        view.txt_direccion.setText("");
    }
    
    private void abrirLogin() {
        LogInControlador loginController = new LogInControlador();
        view.dispose();
        loginController.iniciarLogin();
    }
    
    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(view, mensaje, titulo, tipo);
    }

    private boolean parciarTelefono(String telefono) {
        if (telefono.matches("\\d+")) {
            return true;
        } else {
            mostrarMensaje("Ingresar un numero telefonico valido", "Error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }
}
