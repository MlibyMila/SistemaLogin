package ControladorNew.GestorUsuario;

import Modelo.Usuario;
import Service.UsuarioService;
import Service.impl.UsuarioServiceImpl;
import VistaNew.Usuario.UsuarioPrincipal;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import ControladorNew.LogInControlador;

public final class GesUsuarioPrincipalControlador {

    private UsuarioPrincipal view;
    private Usuario usuario;
    private UsuarioService service;
    private DefaultTableModel model;

    public GesUsuarioPrincipalControlador(Usuario usuario) {
        this.view = new UsuarioPrincipal();
        this.usuario = usuario;
        this.service = new UsuarioServiceImpl();
        this.cargarUsuario();
        this.configuracionListeners();
        this.initTablaUsuario();
    }

    public void iniciarUsuarioPrincipal() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    private void configuracionListeners() {
        view.btn_añadirUsuario.addActionListener(e -> agregarUsuario());
        view.btn_EditarUsuario.addActionListener(e -> editarUsuario());
        view.btn_salir.addActionListener(e -> iniciarLogin());
        view.btn_deshabilitarUsuario.addActionListener(e -> deshabilitarUsuario());
    }

    public void editarUsuario() {
        int filaSeleccionada = view.table_mostrarUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(view, "Por favor, seleccione un usuario de la tabla.",
                    "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) view.table_mostrarUsuarios.getValueAt(filaSeleccionada, 0);

        Usuario usuarioSeleccionado = service.buscarUsuarioPorId(id);

        if (usuarioSeleccionado == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error al recuperar los datos del usuario.", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        view.dispose();
        GesUsuarioEditControlador editControlador = new GesUsuarioEditControlador(usuario);
        editControlador.cargarDatosDeUsuario(usuarioSeleccionado);
        editControlador.iniciarActulizacion();
    }

    public void deshabilitarUsuario() {
        int filaSeleccionada = view.table_mostrarUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(view, "Por favor, seleccione un usuario de la tabla.",
                    "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) view.table_mostrarUsuarios.getValueAt(filaSeleccionada, 0);

        Usuario usuarioSeleccionado = service.buscarUsuarioPorId(id);

        if (usuarioSeleccionado == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error al recuperar los datos del usuario.", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        service.deshabilitarUsuario(usuarioSeleccionado.getIdUsuario());
        initTablaUsuario();
    }

    public void cargarUsuario() {
        view.txtEmailUsuario.setText(usuario.getEmail());
        view.txtNombreUsuario.setText(usuario.getNombres() + " " + usuario.getApellidos());
    }

    public void initTablaUsuario() {
        String[] header = { "ID", "Nombres", "Apellidos", "Email", "Telefono", "Direccion", "Estado" };
        model = new DefaultTableModel(header, 0);
        view.table_mostrarUsuarios.setModel(model);

        List<Usuario> listaUsuarios = service.mostrarUsuario();
        for (Usuario usuario : listaUsuarios) {
            model.addRow(new Object[] {
                    usuario.getIdUsuario(),
                    usuario.getNombres(),
                    usuario.getApellidos(),
                    usuario.getEmail(),
                    usuario.getTelefono(),
                    usuario.getDireccion(),
                    usuario.isEstado() ? "Activo" : "Inactivo",
            });
        }
        view.table_mostrarUsuarios.setModel(model);
    }

    public void agregarUsuario() {
        view.dispose();
        new GesUsuarioRegisterControlador(usuario).iniciarRegistro();
    }

    public void iniciarLogin() {
        LogInControlador loginController = new LogInControlador();
        view.dispose();
        loginController.iniciarLogin();
    }

}
