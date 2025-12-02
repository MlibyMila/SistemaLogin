package Controlador.GestorUsuario;

import Modelo.Usuario;
import Service.UsuarioService;
import Service.impl.UsuarioServiceImpl;
import VistaNew.Usuario.MenuPrestamo;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import Controlador.LogInControlador;
import javax.swing.JOptionPane;

public final class GesUsuarioPrincipalControlador {

    private MenuPrestamo view;
    private UsuarioService service;
    private Usuario usuario;
    private DefaultTableModel model;

    public GesUsuarioPrincipalControlador(Usuario usuario) {
        this.view = new MenuPrestamo();
        this.service = new UsuarioServiceImpl();
        this.usuario = usuario;
        this.cargarUsuario();
        this.configuracionListeners();
        this.initTablaUsuario();
    }

    public void cargarUsuario() {
        view.txt_emailUsuario.setText(usuario.getEmail());
        view.txt_bienvenidaUsuario.setText("¡Bienvenido al Sistema!" + " " + usuario.getNombres());
    }

    public void iniciarMenuUsuario() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    private void configuracionListeners() {
        view.btn_añadirUsuario.addActionListener(e -> agregarUsuario());
        view.btn_EditarUsuario.addActionListener(e -> editarUsuario());
        view.btn_salir.addActionListener(e -> iniciarLogin());
        view.btn_desabilitarUsuario.addActionListener(e -> desactivarUsuario());
    }

    public void initTablaUsuario() {
        String[] header = {"ID", "Nombres", "Apellidos", "Email", "Telefono", "Direccion", "Estado"};
        model = new DefaultTableModel(header, 0);
        view.table_mostrarUsuarios.setModel(model);

        List<Usuario> listaUsuarios = service.mostrarUsuario();
        for (Usuario usuario : listaUsuarios) {
            model.addRow(new Object[]{
                usuario.getIdUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getDireccion(),
                usuario.isEstado() ? "Activo" : "Inactivo",});
        }
        view.table_mostrarUsuarios.setModel(model);
    }

    public Usuario obtenerUsuarioTabla() {
        int filaSeleccionada = view.table_mostrarUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(view, "Por favor, seleccione un usuario de la tabla.",
                    "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = (int) view.table_mostrarUsuarios.getValueAt(filaSeleccionada, 0);
        Usuario usuarioSeleccionado = service.buscarUsuarioPorId(id);
        if (usuarioSeleccionado == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error al recuperar los datos del usuario.", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return usuarioSeleccionado;
    }

    public void editarUsuario() {
        int filaSeleccionada = view.table_mostrarUsuarios.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar un usuario de la tabla para editar.");
            return;
        }
        int idUsuario = (int) view.table_mostrarUsuarios.getValueAt(filaSeleccionada, 0);
        Usuario usuarioSeleccionado = service.buscarUsuarioPorId(idUsuario);
        if (usuarioSeleccionado!= null){
            GesUsuarioEditControlador editControlador = new GesUsuarioEditControlador(usuarioSeleccionado);
             editControlador.cargarDatosDeUsuario();
        editControlador.iniciarActulizacion();
        }else{
            JOptionPane.showMessageDialog(view, "Error no se puede recuperar la información");
        }
    }

    public void desactivarUsuario() {
        Usuario usuarioSeleccionado = obtenerUsuarioTabla();
        GesUsuarioDesControlador desControlador = new GesUsuarioDesControlador(usuarioSeleccionado, this);
        desControlador.cargarDatosDeUsuario();
        desControlador.iniciarDeshabilitar();
    }

    public void agregarUsuario() {
        new GesUsuarioRegisterControlador(this).iniciarRegistro();
    }

    public void iniciarLogin() {
        view.dispose();
        new LogInControlador().iniciarLogin();
    }

}
