package Controlador.GestorCatalogo;

import Modelo.Libro;
import Modelo.Usuario;
import Service.LibroService;
import Service.impl.LibroServiceImpl;
import VistaNew.Catalogo.MenuCatalogo;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import Controlador.LogInControlador;

public class GesCatalogoPrincipalControlador {

    private MenuCatalogo view;
    private Libro libro;
    private Usuario usuario;
    private LibroService service;
    private DefaultTableModel model;

    public GesCatalogoPrincipalControlador(Usuario usuario) {
        this.view = new MenuCatalogo();
        this.usuario = usuario;
        this.service = new LibroServiceImpl();
        this.cargarUsuario();
        this.configuracionListeners();
        this.initTableLibros();
    }

    public void cargarUsuario() {
        view.txt_emailUsuario.setText(usuario.getEmail());
        view.txt_bienvenidaUsuario.setText("¡Bienvenido al Sistema!" + " " + usuario.getNombres());
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

    public Libro obtenerLibroTabla() {
        int filaSeleccionada = view.table_mostrarLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(view, "Por favor, seleccione un libro de la tabla.",
                    "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String isbn = (String) view.table_mostrarLibros.getValueAt(filaSeleccionada, 0);

        Libro libroSeleccionado = service.buscarLibroPorISBN(isbn);

        if (libroSeleccionado == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Error al recuperar los datos del libro.", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return libroSeleccionado;
    }

    public void deshabilitarLibro() {
        Libro libroSeleccionado = obtenerLibroTabla();
        service.desavilitarLibro(libroSeleccionado.getIsbn());
        initTableLibros();
    }

    public void editarLibro() {
        Libro libroSeleccionado = obtenerLibroTabla();
        view.dispose();
        GesCatalogoEditControlador editControlador = new GesCatalogoEditControlador();
        editControlador.cargarDatosDeLibro(libroSeleccionado);
        editControlador.iniciarActulizacion();
    }

    public void initTableLibros() {
        String[] header = {"ID", "Título", "ISBN", "Categoria", "Idioma", "Número de Páginas", "Descripción", "Fecha Creacion", "Estado"};
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
        new GesCatalogoRegisterControlador().inicializaRegistro();
    }

    public void iniciarLogin() {
        LogInControlador loginController = new LogInControlador();
        view.dispose();
        loginController.iniciarLogin();
    }
}
