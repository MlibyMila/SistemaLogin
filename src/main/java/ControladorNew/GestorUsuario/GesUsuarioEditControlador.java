/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControladorNew.GestorUsuario;

import Modelo.Usuario;
import Service.UsuarioService;
import Service.impl.UsuarioServiceImpl;
import VistaNew.Usuario.UsuarioEdit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Milagritos
 */
public class GesUsuarioEditControlador {

    private UsuarioEdit view;
    private UsuarioService service;
    private Usuario usuarioEdicion = null;

    public GesUsuarioEditControlador() {
        this.view = new UsuarioEdit();
        this.service = new UsuarioServiceImpl();
        this.configuracionListeners();
    }

    public void iniciarActulizacion() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    public void configuracionListeners() {
        view.btn_guardarEditUsuario.addActionListener(e -> procesarCambios());
        view.btn_cancelarEditUsuario.addActionListener(e -> limpiarCampos());
        view.btn_salir.addActionListener(e -> abrirUsuarioPrincipal());
    }

    private void actualizar(Usuario usuario) {
        this.usuarioEdicion = usuario;
        view.txt_nombreEditUsuario.setText(usuario.getNombres());
        view.txt_apellidoEditUsuario.setText(usuario.getApellidos());
        view.txt_emailEditUsuario.setText(usuario.getEmail());
        view.txt_telefonoEditUsuario.setText(String.valueOf(usuario.getTelefono()));
        view.txt_direccionEditUsuario.setText(usuario.getDireccion());

        view.txt_passwordEditUsuario.setText("");
        view.btn_guardarEditUsuario.setText("Actualizar");
    }

    private void procesarCambios() {
        String nombres = view.txt_nombreEditUsuario.getText().trim();
        String apellidos = view.txt_apellidoEditUsuario.getText().trim();
        String email = view.txt_emailEditUsuario.getText().trim();
        String password = new String(view.txt_passwordEditUsuario.getPassword());
        String telefono = view.txt_telefonoEditUsuario.getText().trim();
        String direccion = view.txt_direccionEditUsuario.getText().trim();

        if (!validarCampos(nombres, apellidos, email)) {
            return;
        }

        if (!validarTelefono(telefono)) {
            return;
        }

        if (usuarioEdicion == null && password.isEmpty()) {
            mostrarMensaje("La contraseña es obligatoria para nuevos usuarios.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

    }

    private boolean validarCampos(String nombres, String apellidos, String email) {
        if (nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty()) {
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

    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(view, mensaje, titulo, tipo);
    }

    private boolean validarTelefono(String telefono) {
        if (telefono.matches("\\d+")) {
            return true;
        } else {
            mostrarMensaje("Ingresar un numero telefonico valido", "Error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }

    private void limpiarCampos() {
        view.txt_nombreEditUsuario.setText("");
        view.txt_apellidoEditUsuario.setText("");
        view.txt_emailEditUsuario.setText("");
        view.txt_passwordEditUsuario.setText("");
        view.txt_telefonoEditUsuario.setText("");
        view.txt_direccionEditUsuario.setText("");
    }

    private void abrirUsuarioPrincipal() {
        GesUsuarioPrincipalControlador gesUsuarioPrincipal = new GesUsuarioPrincipalControlador();
        view.dispose();
        gesUsuarioPrincipal.iniciarUsuarioPrincipal();
    }

}
