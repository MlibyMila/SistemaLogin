package Controlador.GestorCatalogo;

import Modelo.Autor;
import Modelo.Categoria;
import Modelo.Libro;
import Service.AutorService;
import Service.CategoriaService;
import Service.LibroService;
import Service.impl.AutorServiceImpl;
import Service.impl.CategoriaServiceImpl;
import Service.impl.LibroServiceImpl;
import VistaNew.Catalogo.EditarRegistrarLibro;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class GesCatalogoRegisterControlador {

    private EditarRegistrarLibro view;
    private LibroService libroService;
    private CategoriaService categoriaService;
    private AutorService autorDaoService;

    public GesCatalogoRegisterControlador() {
        this.view = new EditarRegistrarLibro();
        // iniciamos las implementaciones de service
        this.libroService = new LibroServiceImpl();
        this.categoriaService = new CategoriaServiceImpl();
        this.autorDaoService = new AutorServiceImpl();

        this.configuracionListeners();
    }

    public void inicializaRegistro() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
        view.setTitle("Registrar Nuevo Libro");
        llenarCombo();

    }

    private void llenarCombo() {
        // limpiar modelos
        DefaultComboBoxModel modelCat = (DefaultComboBoxModel) view.listaCategorias.getModel();
        DefaultComboBoxModel modelAut = (DefaultComboBoxModel) view.listaAutores.getModel();

        modelCat.removeAllElements();
        modelAut.removeAllElements();
        // obtener los datos desde los servicios 
        List<Categoria> listaCategorias = categoriaService.mostrarCategoria();
        List<Autor> listaAutores = autorDaoService.mostrarAutores();
        // llenar catdegorias (ComboBox)
        for (Categoria cat : listaCategorias) {
            modelCat.addElement(new ComboItem(cat.getIdCategoria(), cat.getNombre()));
        }
        // llenar Autores
        for (Autor aut : listaAutores) {
            String nombreCompleto = aut.getNombres() + " " + aut.getApellidos();
            modelAut.addElement(new ComboItem(aut.getIdAutor(), nombreCompleto));
        }

    }

    private void registrar() {
        // validaciones 
        if (view.txt_tituloEditLibro.getText().isEmpty() || view.txt_ISBNeditLibro.getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "El titulo y el ISBN son obligatorios", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Libro libro = new Libro();
        libro.setTitulo(view.txt_tituloEditLibro.getText());
        libro.setIsbn(view.txt_ISBNeditLibro.getText());
        libro.setIdioma(view.txt_idiomaEditLibro.getText());
        libro.setDescripcion(view.txt_descripcionEditLibro.getText());
        try {
            libro.setNumeroPaginas(Integer.parseInt(view.txt_numPaginasEditLibro.getText()));
        } catch (NumberFormatException e) {
            libro.setNumeroPaginas(0);
        }
        try {
            String fechatxt = view.txt_fechaEditLibro.getText();
            if (fechatxt != null && !fechatxt.trim().isEmpty()) {
                libro.setFechaPublicacion(LocalDate.parse(fechatxt, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } else {
                libro.setFechaPublicacion(LocalDate.now());
            }
        } catch (Exception e) {
            libro.setFechaPublicacion(LocalDate.now());
        }
        // recuperar el id categoria 
        Object itemCat = view.listaCategorias.getSelectedItem();
        if (itemCat instanceof ComboItem) {
            ComboItem selection = (ComboItem) itemCat;
            libro.setIdCategoria(selection.getKey());
            Categoria c = new Categoria();
            c.setIdCategoria(selection.getKey());
            libro.setCategoria(c);
        }
        // recuperar ID autor
        Object itemAut = view.listaAutores.getSelectedItem();
        List<Autor> listaAutores = new ArrayList<>();
        if (itemAut instanceof ComboItem) {
            ComboItem selection = (ComboItem) itemAut;
            Autor a = new Autor();
            a.setIdAutor(selection.getKey());
            listaAutores.add(a);
        }
        libro.setAutores(listaAutores);
        // guardar cambios 
        try {
            libroService.registrarLibro(libro);
            JOptionPane.showMessageDialog(view, "Libro Registrado");
            limpiarCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error al registrar " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        view.txt_tituloEditLibro.setText("");
        view.txt_ISBNeditLibro.setText("");
        view.txt_idiomaEditLibro.setText("");
        view.txt_numPaginasEditLibro.setText("");
        view.txt_descripcionEditLibro.setText("");
        view.txt_fechaEditLibro.setText("");
        if (view.listaCategorias.getItemCount() > 0) {
            view.listaCategorias.setSelectedIndex(0);
        }
        if (view.listaAutores.getItemCount() > 0) {
            view.listaAutores.setSelectedIndex(0);
        }

    }

    private void configuracionListeners() {
        view.btn_guardarEditLibro.addActionListener(e -> registrar());
        view.btn_cancelarEditLibro.addActionListener(e -> abrirMenuPrincipal());

    }

    private void abrirMenuPrincipal() {
        view.dispose();
    }

}
