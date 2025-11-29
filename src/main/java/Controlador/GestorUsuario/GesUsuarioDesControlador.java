package Controlador.GestorUsuario;

import Modelo.Usuario;
import Service.UsuarioService;
import Service.impl.UsuarioServiceImpl;
import VistaNew.Usuario.DesactivarUsuario;

public class GesUsuarioDesControlador {

    private DesactivarUsuario view;
    private UsuarioService service;
    private Usuario usuarioEliminar;
    private GesUsuarioPrincipalControlador principalControlador;

    public GesUsuarioDesControlador(Usuario usuario, GesUsuarioPrincipalControlador principalControlador) {
        this.view = new DesactivarUsuario();
        this.service = new UsuarioServiceImpl();
        this.usuarioEliminar = usuario;
        this.principalControlador = principalControlador;
        this.configuracionListeners();
    }

    public void iniciarDeshabilitar() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
        this.cargarDatosDeUsuario();
    }

    private void configuracionListeners() {
        view.btn_desabilitarUsuario.addActionListener(e -> deshabilitar(usuarioEliminar));
        view.btn_cancelarDesabilitarUsuario.addActionListener(e -> abrirUsuarioPrincipal());
    }

    public void cargarDatosDeUsuario() {
        view.txt_IdUsuario.setText(String.valueOf(usuarioEliminar.getIdUsuario()));
        view.txt_nombre.setText(usuarioEliminar.getNombres());

    }

    public void deshabilitar(Usuario usuario) {
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(view,
                "¿Estás seguro de que deseas deshabilitar este usuario?", "Confirmar Deshabilitación",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            service.deshabilitarUsuario(usuario.getIdUsuario());
            javax.swing.JOptionPane.showMessageDialog(view, "Usuario deshabilitado correctamente.", "Éxito",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            abrirUsuarioPrincipal();
        }

        if (principalControlador != null) {
            principalControlador.initTablaUsuario();
        }
    }

    public void abrirUsuarioPrincipal() {
        view.dispose();
    }

}
