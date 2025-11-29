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
import VistaNew.Catalogo.CatalogoEdit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Milagritos
 */
public class GesCatalogoEditControlador {

    private CatalogoEdit view;
    private LibroService service;
    private Libro libroEdit = null;
    private Usuario usuario;
    
public GesCatalogoEditControlador() {
        this.view = new CatalogoEdit();
        this.service = new LibroServiceImpl();
        this.limpiarCampos();
        this.cargarUsuario();
        this.configuracionListeners();
    }
    public GesCatalogoEditControlador(Usuario usuario) {
        this.view = new CatalogoEdit();
        this.service = new LibroServiceImpl();
        this.usuario = usuario;
        this.limpiarCampos();
        this.cargarUsuario();
        this.configuracionListeners();
    }

    public void iniciarActulizacion() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    public void cargarUsuario() {
        view.txtEmailUsuario.setText(usuario.getEmail());
        view.txtNombreUsuario.setText(usuario.getNombres() + " " + usuario.getApellidos());
    }

    public void configuracionListeners() {
        view.btn_guardarEditLibro.addActionListener(e -> procesarCambios());
         view.btn_cancelarEditLibro.addActionListener(e -> limpiarCampos());
        view.btn_cancelarEditLibro.addActionListener(e -> abrirMenuPrincipal());
        view.btn_salir.addActionListener(e -> iniciarLogin());
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
            limpiarCampos();
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
        GesCatalogoPrincipalControlador gesUsuarioPrincipal = new GesCatalogoPrincipalControlador(usuario);
        view.dispose();
        gesUsuarioPrincipal.iniciarCatalogoPrincipal();
    }

    public void iniciarLogin() {
        LogInControlador loginController = new LogInControlador();
        view.dispose();
        loginController.iniciarLogin();
    }

    private void limpiarCampos() {
        view.txt_tituloEditLibro.setText("");
        view.txt_ISBNeditLibro.setText("");
        view.txt_idiomaEditLibro.setText("");
        view.txt_descripcionEditLibro.setText("");
        view.txt_numPaginasEditLibro.setText("");
        //view.listaAutores.setText("");
        //view.listaCategorias.setText();
    }
     private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(view, mensaje, titulo, tipo);
    }
}
