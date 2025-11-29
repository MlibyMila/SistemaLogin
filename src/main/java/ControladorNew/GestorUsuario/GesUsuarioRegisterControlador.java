package ControladorNew.GestorUsuario;

import ControladorNew.LogInControlador;
import Modelo.Usuario;
import Service.UsuarioService;
import Service.impl.UsuarioServiceImpl;
import VistaNew.Usuario.UsuarioRegister;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;


public class GesUsuarioRegisterControlador {

    private UsuarioRegister view;
    private Usuario usuario;
    private UsuarioService service;

    public GesUsuarioRegisterControlador(Usuario usuario) {
        this.view = new UsuarioRegister();
        this.usuario = usuario;
        this.service = new UsuarioServiceImpl();
        this.limpiarCampos();
        this.cargarUsuario();
        this.configuracionListeners();
    }

    public void iniciarRegistro() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    public void cargarUsuario() {
        view.txtEmailUsuario.setText(usuario.getEmail());
        view.txtNombreUsuario.setText(usuario.getNombres() + " " + usuario.getApellidos());
    }

    private void configuracionListeners() {
        view.btn_guardarAddUsuario.addActionListener(e -> registrar());
        view.btn_cancelarAddUsuario.addActionListener(e -> limpiarCampos());
        view.btn_cancelarAddUsuario.addActionListener(e -> abrirMenuPrincipal());
        view.btn_salir.addActionListener(e -> iniciarLogin());
    }

    private void registrar() {
        String nombres = view.txt_nombreAddUsuario.getText();
        String apellidos = view.txt_apellidoAddUsuario.getText();
        String email = view.txt_emailAddUsuario.getText();
        String password = new String(view.txt_passwordAddUsuario.getPassword());
        String telefono = view.txt_telefonoAddUsuario.getText();
        String direccion = view.txt_direccionAddUsuario.getText();

        if (!validarCampos(nombres, apellidos, email, password)) {
            return;
        }
        if (!validarTelefono(telefono)) {
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
            abrirMenuPrincipal();
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

    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(view, mensaje, titulo, tipo);
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
        view.txt_nombreAddUsuario.setText("");
        view.txt_apellidoAddUsuario.setText("");
        view.txt_emailAddUsuario.setText("");
        view.txt_passwordAddUsuario.setText("");
        view.txt_telefonoAddUsuario.setText("");
        view.txt_direccionAddUsuario.setText("");
    }

    private void abrirMenuPrincipal() {
        GesUsuarioPrincipalControlador gesUsuarioPrincipal = new GesUsuarioPrincipalControlador(usuario);
        view.dispose();
        gesUsuarioPrincipal.iniciarUsuarioPrincipal();
    }

    private boolean validarTelefono(String telefono) {
        if (telefono.matches("\\d+")) {
            return true;
        } else {
            mostrarMensaje("Ingresar un numero telefonico valido", "Error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }
       public void iniciarLogin() {
        LogInControlador loginController = new LogInControlador();
        view.dispose();
        loginController.iniciarLogin();
    }

}
