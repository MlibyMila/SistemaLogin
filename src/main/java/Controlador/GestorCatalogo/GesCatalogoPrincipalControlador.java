package Controlador.GestorCatalogo;

import Modelo.Libro;
import Modelo.Usuario;
import Service.LibroService;
import Service.impl.LibroServiceImpl;
import VistaNew.Catalogo.MenuCatalogo;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import Controlador.LogInControlador;
import Modelo.Autor;
import javax.swing.JOptionPane;

public class GesCatalogoPrincipalControlador {

    private MenuCatalogo view;
    private Usuario usuario;
    private LibroService service;
    private DefaultTableModel model;

    public GesCatalogoPrincipalControlador(Usuario usuario) {
        this.view = new MenuCatalogo();
        this.usuario = usuario;
        this.service = new LibroServiceImpl(); // Conectar con el servicio

        this.cargarUsuario();
        this.configuracionListeners();
        this.initTableLibros(); // Cargar la tabla al inicio
    }

    public void cargarUsuario() {
        if (usuario != null) {
            view.txt_emailUsuario.setText(usuario.getEmail());
            view.txt_bienvenidaUsuario.setText("¡Bienvenido al Sistema!" + " " + usuario.getNombres());
        }
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

    // metodo que permite selecionar un libro de la tabla principal 
    public Libro obtenerLibroTabla() {
        int filaSeleccionada = view.table_mostrarLibros.getSelectedRow(); // selecciona una fila 
        if (filaSeleccionada == -1) { // no se ha seleccionado
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un libro de la tabla.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String isbn = view.table_mostrarLibros.getValueAt(filaSeleccionada, 2).toString();

        Libro libroSeleccionado = service.buscarLibroPorISBN(isbn);

        if (libroSeleccionado == null) {
            JOptionPane.showMessageDialog(view, "Error al recuperar los datos del libro.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return libroSeleccionado;
    }

    // metodo que permite desabilitar el libro de la fila seleccionada 
    public void deshabilitarLibro() {
        Libro libroSeleccionado = obtenerLibroTabla();
        if (libroSeleccionado != null) {
            int confirm = JOptionPane.showConfirmDialog(view, "Estas Seguro de Deshabilitarel libro: " + libroSeleccionado.getTitulo() + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                service.desavilitarLibro(libroSeleccionado.getIsbn());
                initTableLibros(); // refrescar la tabla 
                JOptionPane.showMessageDialog(view, "Libro deshabilitaado");
            }
        }
    }

    // metodo que permite editar libro seleccionado
    public void editarLibro() {
        Libro libroSeleccionado = obtenerLibroTabla();
        if (libroSeleccionado != null) {
            GesCatalogoEditControlador editControlador = new GesCatalogoEditControlador();
            editControlador.cargarDatosLibro(libroSeleccionado);
            editControlador.iniciarActulizacion();
        }
    }

    public void initTableLibros() {
        String[] header = {"ID", "Título", "ISBN", "Categoría", "Idioma", "Páginas", "Descripción", "Autores", "Estado"}; // nombramos los campos de la tabla 
        //  hace que la tabla solo sea visible pero NO editable 
        model = new DefaultTableModel(header, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // traer los datos 
        List<Libro> listaLibros = service.obtenerTodosLosLibros();
        for (Libro libro : listaLibros) {
            // formatear la categoria
            String nombreCategoria = (libro.getCategoria() != null) ? libro.getCategoria().getNombre() : "Sin categortia";
            // fromater Autores
            StringBuilder autoresStr = new StringBuilder();
            if (libro.getAutores() != null) {
                for (Autor a : libro.getAutores()) {
                    if (autoresStr.length() > 0) {
                        autoresStr.append(" , ");
                    }
                    autoresStr.append(a.getNombres()).append(" ").append(a.getApellidos());
                }
            }
            // agregar fila con los datos respetando el orden
            model.addRow(new Object[]{
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getIsbn(),
                nombreCategoria,
                libro.getIdioma(),
                libro.getNumeroPaginas(),
                libro.getDescripcion(),
                autoresStr.toString(),
                libro.isEstado() ? "Activo" : "Inactivo"
            });

        }
        view.table_mostrarLibros.setModel(model); // se agrega el obejeto a la tabla de la ventana principal
    }

    // muestra la ventana de registrar libro 
    public void agregarLibro() {
        GesCatalogoRegisterControlador registerControlador = new GesCatalogoRegisterControlador();
        registerControlador.inicializaRegistro();
   
    }

    // muestra la ventana de LogIn
    public void iniciarLogin() {
        LogInControlador loginController = new LogInControlador();
        view.dispose(); // cierra la ventana 
        loginController.iniciarLogin();
    }
}
