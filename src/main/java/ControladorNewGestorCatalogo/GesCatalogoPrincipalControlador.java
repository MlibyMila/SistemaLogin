/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControladorNewGestorCatalogo;

import ControladorNew.LogInControlador;
import Modelo.Libro;
import Modelo.Usuario;
import Service.LibroService;
import Service.impl.LibroServiceImpl;
import VistaNew.Catalogo.CatalogoPrincipal;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Milagritos
 */
public class GesCatalogoPrincipalControlador {

    private CatalogoPrincipal view;
    private Libro libro;
    private Usuario usuario;
    private LibroService service;
    private DefaultTableModel model;

    public GesCatalogoPrincipalControlador() {
        this.view = new CatalogoPrincipal();
        this.service = new LibroServiceImpl();
    }

    public GesCatalogoPrincipalControlador(Usuario usuario) {
        this.view = new CatalogoPrincipal();
        this.usuario = usuario;
        this.service = new LibroServiceImpl();
        this.cargarUsuario();
        this.configuracionListeners();
       // this.innitTableLibros();
    }

    public void iniciarCatalogoPrincipal() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    private void configuracionListeners() {
      //  view.btn_añadirLibro.addActionListener(e -> agregarUsuario());
        view.btn_EditarLibro.addActionListener(e -> editarLibro());
        view.btn_desabilitarLibro.addActionListener(e -> deshabilitarUsuario());
        view.btn_salir.addActionListener(e -> iniciarLogin());
    }

    public void iniciarLogin() {
        LogInControlador loginController = new LogInControlador();
        view.dispose();
        loginController.iniciarLogin();
    }

    public void deshabilitarUsuario() {
        int filaSeleccionada = view.table_mostrarLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(view, "Por favor, seleccione un usuario de la tabla.",
                    "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String isbn = (String) view.table_mostrarLibros.getValueAt(filaSeleccionada, 0);

        Libro libroSeleccionado = service.buscarLibroPorISBN(isbn);

        if (libroSeleccionado == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error al recuperar los datos del usuario.", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        service.desavilitarLibro(libroSeleccionado.getIsbn());
        //initTableLibro();
    }
    public void editarLibro() {
        int filaSeleccionada = view.table_mostrarLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(view, "Por favor, seleccione un usuario de la tabla.",
                    "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String isbn = (String) view.table_mostrarLibros.getValueAt(filaSeleccionada, 0);

        Libro libroSeleccionado = service.buscarLibroPorISBN(isbn);

        if (libroSeleccionado == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error al recuperar los datos del usuario.", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        view.dispose();
        GesCatalogoEditControlador editControlador = new GesCatalogoEditControlador();
        //editControlador.cargarDatosDeUsuario(usuarioSeleccionado);
        editControlador.inciarActualizaciom();
    }
    

    public void cargarUsuario() {
        view.txtEmailUsuario.setText(usuario.getEmail());
        view.txtNombreUsuario.setText(usuario.getNombres() + " " + usuario.getApellidos());
    }

}
