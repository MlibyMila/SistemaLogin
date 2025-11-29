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
import java.util.List;
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
        this.configuracionListeners();
        this.innitTableLibros();
    }

    public GesCatalogoPrincipalControlador(Usuario usuario) {
        this.view = new CatalogoPrincipal();
        this.usuario = usuario;
        this.service = new LibroServiceImpl();
        this.cargarUsuario();
        this.configuracionListeners();
        this.innitTableLibros();
    }

    public void iniciarCatalogoPrincipal() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    private void configuracionListeners() {
        view.btn_añadirLibro.addActionListener(e -> agregarLibro());
        view.btn_EditarLibro.addActionListener(e -> editarLibro());
        view.btn_desabilitarLibro.addActionListener(e -> deshabilitarLibro());
        view.btn_salir.addActionListener(e -> iniciarLogin());
    }

    public void iniciarLogin() {
        LogInControlador loginController = new LogInControlador();
        view.dispose();
        loginController.iniciarLogin();
    }

    public void deshabilitarLibro() {
        int filaSeleccionada = view.table_mostrarLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(view, "Por favor, seleccione un libro de la tabla.",
                    "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String isbn = (String) view.table_mostrarLibros.getValueAt(filaSeleccionada, 0);

        Libro libroSeleccionado = service.buscarLibroPorISBN(isbn);

        if (libroSeleccionado == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error al recuperar los datos del libro.", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        service.desavilitarLibro(libroSeleccionado.getIsbn());
        innitTableLibros();
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
        editControlador.cargarDatosDeLibro(libroSeleccionado);
        editControlador.iniciarActulizacion();
    }

    public void cargarUsuario() {
        view.txtEmailUsuario.setText(usuario.getEmail());
        view.txtNombreUsuario.setText(usuario.getNombres() + " " + usuario.getApellidos());
    }

    public void innitTableLibros() {
        String[] header = {"ID", "Título", "ISBN", "Idioma", "Número de Páginas", "Autor", "Descripción", "Estado"};
        model = new DefaultTableModel(header, 0);
        view.table_mostrarLibros.setModel(model);

        List<Libro> listaLibros = service.obtenerTodosLosLibros();
        for (Libro libro : listaLibros) {
            model.addRow(new Object[]{
                libro.getTitulo(),
                libro.getIsbn(),
                libro.getIdioma(),
                libro.getNumeroPaginas(),
                libro.getDescripcion(),
                libro.getAutores(),
                libro.isEstado() ? "Activo" : "Inactivo",});
        }
        view.table_mostrarLibros.setModel(model);
    }

    public void agregarLibro() {
        view.dispose();
        new GesCatalogoRegisterControlador().inicializaRegistro();
    }

}
