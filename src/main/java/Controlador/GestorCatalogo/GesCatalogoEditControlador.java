package Controlador.GestorCatalogo;

import Modelo.Libro;
import Modelo.Usuario;
import Service.LibroService;
import Service.impl.LibroServiceImpl;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

import VistaNew.Catalogo.EditarRegistrarLibro;

public class GesCatalogoEditControlador {

    private EditarRegistrarLibro view;
    private LibroService service;
    private Libro libroEdit = null;
    private Usuario usuario;

    public GesCatalogoEditControlador() {
        this.view = new EditarRegistrarLibro();
        this.service = new LibroServiceImpl();
        this.configuracionListeners();
    }

    public void iniciarActulizacion() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    public void configuracionListeners() {
        view.btn_guardarEditLibro.addActionListener(e -> procesarCambios());
        view.btn_cancelarEditLibro.addActionListener(e -> abrirMenuPrincipal());
    }

    private void procesarCambios() {
        String titulo = view.txt_tituloEditLibro.getText().trim();
        String ISBN = view.txt_ISBNeditLibro.getText().trim();
        String idioma = view.txt_idiomaEditLibro.getText().trim();
        String descripcion = view.txt_descripcionEditLibro.getText().trim();
        String fechaPublicacion = view.txt_fechaEditLibro.getText().trim();
        libroEdit.setTitulo(titulo);
        libroEdit.setIsbn(ISBN);
        libroEdit.setIdioma(idioma);
        libroEdit.setDescripcion(descripcion);
        //  libroEdit.setFechaPublicacion(fechaPublicacion);

        if (!validarCampos(titulo, ISBN, idioma)) {
            return;
        }

        try {
            service.editarLibro(libroEdit); // Asumiendo que este método existe en tu Service
            mostrarMensaje("Usuario actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            // abrirUsuarioPrincipal();
        } catch (Exception e) {
            mostrarMensaje("Error al actualizar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private boolean validarCampos(String titulo, String ISBN, String idioma) {
        if (titulo.isEmpty() || ISBN.isEmpty() || idioma.isEmpty()) {
            mostrarMensaje("Por favor, completar los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!validar(titulo)) {
            mostrarMensaje("Por favor, ingresar un titulo valido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validar(ISBN)) {
            mostrarMensaje("Por favor, ingresar un ISBN valido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validar(idioma)) {
            mostrarMensaje("Por favor, ingresar un idioma valido", "Error", JOptionPane.ERROR_MESSAGE);

        }
        return true;
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

    public void cargarDatosDeLibro(Libro libro) {
        this.libroEdit = libro;
        view.txt_tituloEditLibro.setText(libro.getTitulo());
        view.txt_ISBNeditLibro.setText(libro.getIsbn());
        view.txt_idiomaEditLibro.setText(libro.getIdioma());
        view.txt_descripcionEditLibro.setText(libro.getDescripcion());
        // view.listaAutores.setText(String.valueOf(libro.getAutores()));
        //  view.txt_numPaginasEditLibro.setText(libro.getNumeroPaginas());
        //  view.listaCategorias.setText(libro.getCategoria());
    }

    private void abrirMenuPrincipal() {
        view.dispose();
    }


    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(view, mensaje, titulo, tipo);
    }
}
