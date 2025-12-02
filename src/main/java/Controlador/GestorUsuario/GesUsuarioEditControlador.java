package Controlador.GestorUsuario;

import org.mindrot.jbcrypt.BCrypt;
import Modelo.Usuario;
import Service.UsuarioService;
import Service.impl.UsuarioServiceImpl;
import javax.swing.JOptionPane;

import VistaNew.Usuario.EditarRegistarUsuario;

public class GesUsuarioEditControlador {

    private EditarRegistarUsuario view;
    private UsuarioService service;
    private Usuario usuarioEdicion;

    public GesUsuarioEditControlador(Usuario usuario) {
        this.view = new EditarRegistarUsuario();
        this.service = new UsuarioServiceImpl();
        this.usuarioEdicion = usuario;
        this.configuracionListeners();
    }

    public void iniciarActulizacion() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    public void configuracionListeners() {
        view.btn_guardarEditUsuario.addActionListener(e -> procesarCambios());
        view.btn_cancelarEditUsuario.addActionListener(e -> abrirMenuPrincipal());
    }

    public void cargarDatosDeUsuario() {
        view.txt_nombreEditUsuario.setText(usuarioEdicion.getNombres());
        view.txt_apellidoEditUsuario.setText(usuarioEdicion.getApellidos());
        view.txt_emailEditUsuario.setText(usuarioEdicion.getEmail());
        view.txt_telefonoEditUsuario.setText(String.valueOf(usuarioEdicion.getTelefono()));
        view.txt_direccionEditUsuario.setText(usuarioEdicion.getDireccion());
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
        usuarioEdicion.setNombres(nombres);
        usuarioEdicion.setApellidos(apellidos);
        usuarioEdicion.setEmail(email);
        try {
            usuarioEdicion.setTelefono(Integer.parseInt(telefono));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "El teléfono debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        usuarioEdicion.setDireccion(direccion);

        ValidacionesUsuario validacionesUsuario = new ValidacionesUsuario();

        if (!validacionesUsuario.validarCampos(nombres, apellidos, email, telefono)) {
            return;
        }

        if (!password.isEmpty()) {
            String passwordHashed = BCrypt.hashpw(password, BCrypt.gensalt());
            usuarioEdicion.setPasswordHash(passwordHashed);
        }

        try {
            service.actualizarUsuario(usuarioEdicion);
            validacionesUsuario.mostrarMensaje("Usuario actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } catch (Exception e) {
            validacionesUsuario.mostrarMensaje("Error al actualizar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private void abrirMenuPrincipal() {
        view.dispose();
    }

}
