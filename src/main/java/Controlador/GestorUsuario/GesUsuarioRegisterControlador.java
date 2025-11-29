package Controlador.GestorUsuario;

import Modelo.Usuario;
import Service.UsuarioService;
import Service.impl.UsuarioServiceImpl;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;

import VistaNew.Usuario.EditarRegistarUsuario;
import org.mindrot.jbcrypt.BCrypt;

public class GesUsuarioRegisterControlador {

    private EditarRegistarUsuario view;
    private UsuarioService service;
    private GesUsuarioPrincipalControlador principalControlador;

    public GesUsuarioRegisterControlador(GesUsuarioPrincipalControlador principalControlador) {
        this.view = new EditarRegistarUsuario();
        this.service = new UsuarioServiceImpl();
        this.principalControlador = principalControlador;
        this.configuracionListeners();
    }

    public void iniciarRegistro() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
        view.txtTitulo.setText("REGISTRAR INFORMACION DEL USUARIO");
        view.btn_guardarEditUsuario.setText("Registar");
    }

    private void configuracionListeners() {
        view.btn_guardarEditUsuario.addActionListener(e -> registrar());
        view.btn_cancelarEditUsuario.addActionListener(e -> abrirMenuPrincipal());
    }

    private void registrar() {
        String nombres = view.txt_nombreEditUsuario.getText();
        String apellidos = view.txt_apellidoEditUsuario.getText();
        String email = view.txt_emailEditUsuario.getText();
        String password = new String(view.txt_passwordEditUsuario.getPassword());
        String telefono = view.txt_telefonoEditUsuario.getText();
        String direccion = view.txt_direccionEditUsuario.getText();

        ValidacionesUsuario validacionesUsuario = new ValidacionesUsuario();

        if (!validacionesUsuario.validarCampos(nombres, apellidos, email, telefono)) {
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombres(nombres);
        nuevoUsuario.setApellidos(apellidos);
        nuevoUsuario.setEmail(email);
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        nuevoUsuario.setPasswordHash(passwordHash);
        nuevoUsuario.setTelefono(Integer.parseInt(telefono));
        nuevoUsuario.setDireccion(direccion);
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());
        nuevoUsuario.setEstado(true);

        try {
            service.registrarUsuario(nuevoUsuario);
            validacionesUsuario.mostrarMensaje("Registro exitoso!", "Exito!", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            if (principalControlador != null) {
                principalControlador.initTablaUsuario();
            }
        } catch (Exception e) {
            validacionesUsuario.mostrarMensaje("Error al registrar: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void limpiarCampos() {
        view.txt_nombreEditUsuario.setText("");
        view.txt_apellidoEditUsuario.setText("");
        view.txt_emailEditUsuario.setText("");
        view.txt_passwordEditUsuario.setText("");
        view.txt_telefonoEditUsuario.setText("");
        view.txt_direccionEditUsuario.setText("");
    }

    public void abrirMenuPrincipal() {
        view.dispose();
    }

}
