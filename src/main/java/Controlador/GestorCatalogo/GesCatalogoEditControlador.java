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
import javax.swing.JOptionPane;

import VistaNew.Catalogo.EditarRegistrarLibro;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class GesCatalogoEditControlador {

    private EditarRegistrarLibro view; // instanciar la vista  
    private LibroService libroService; // instanciar al servicio (interface de metodoos)
    private CategoriaService categoriaService; //  instancia servicio categoria (interface de netodos)
    private AutorService autorService; // intancia un servico Autor (interface de netodos)
    
    private Libro libroEdit; // libro que sera editado

    public GesCatalogoEditControlador() {
        this.view = new EditarRegistrarLibro();
        // inicializar los servicios 
        this.libroService = new LibroServiceImpl();
        this.categoriaService = new CategoriaServiceImpl();
        this.autorService = new AutorServiceImpl();
        
        this.configuracionListeners(); // inicializa las acciones de los btnes
    }

    // muestra la ventana de actulizar libro
    public void iniciarActulizacion() {
        view.setVisible(true); // hace visible la ventana 
        view.setLocationRelativeTo(null); // centra la ventana 
        view.setTitle("Editar Libro"); // asigna titulo a la ventana
        view.btn_guardarEditLibro.setText("Actualizar"); // actualiza el nombre del btn 
        // llenar campos antes de editar 
        llenarCombos();
        if (libroEdit!= null){ // selecciona un libro de la tabla para editar 
            seleccionarCombosActuales(); 
        }
    }

    // asigna acciones a los btnes 
    public void configuracionListeners() {
        view.btn_guardarEditLibro.addActionListener(e -> procesarCambios()); // btn para confirmar la edicion 
        view.btn_cancelarEditLibro.addActionListener(e -> abrirMenuPrincipal()); // btn para abrir el menu principal
    }
    
    // llenar el comboBox 
    private void llenarCombos(){
        // instanciar ComboBox 
        DefaultComboBoxModel modelCat = (DefaultComboBoxModel) view.listaCategorias.getModel(); // alamcena categorias
        DefaultComboBoxModel modelAut = (DefaultComboBoxModel) view.listaAutores.getModel(); // almacena autores
        
        // se vacia los campos del ComboBox por defecto 
        modelCat.removeAllElements(); 
        modelAut.removeAllElements();
        
        // intancia listas para mostrar y almacenar los objetos
        List<Categoria> listaCategorias = categoriaService.mostrarCategoria(); // almacena categorias 
        List<Autor> listaAutores = autorService.mostrarAutores(); // almacena autores
        
        // llena el comboBox de Categorias
        for (Categoria cat : listaCategorias){
            modelCat.addElement(new ComboItem(cat.getIdCategoria(), cat.getNombre()));
        }
        
        // llena el comboBox de Autores
        for (Autor aut : listaAutores){
            String nombreCompleto = aut.getNombres() + " " + aut.getApellidos(); // obtine el nombre completo
            modelAut.addElement(new ComboItem(aut.getIdAutor(), nombreCompleto));
        }
    }
    
    // 
    public void cargarDatosLibro(Libro libro){
        this.libroEdit = libro;
        
        // cargar datos a los campos
        view.txt_tituloEditLibro.setText(libro.getTitulo()); // carga titulo
        view.txt_ISBNeditLibro.setText(libro.getIsbn()); // carga isbn
        view.txt_ISBNeditLibro.setEditable(false); // no permitir editar este campo
        view.txt_idiomaEditLibro.setText(libro.getIdioma()); // carga idioma
        view.txt_numPaginasEditLibro.setText(String.valueOf(libro.getNumeroPaginas())); // carga num de paginas 
        view.txt_descripcionEditLibro.setText(libro.getDescripcion()); // carga titulo
        
        if (libro.getFechaPublicacion()!= null){ // hay fecha de publicacion
            view.txt_fechaEditLibro.setText(libro.getFechaPublicacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); // le da formato 
        }
        
    }
    
    // metodo que permite seleccionar un elemento del ComboBox
    private void seleccionarCombosActuales(){
        // Seleccionar categorias
        if (libroEdit.getCategoria() != null){
            int idCatBuscado = libroEdit.getCategoria().getIdCategoria();
            // recore el combo buscando la ID categoria
            for (int i = 0; i < view.listaCategorias.getItemCount(); i++){
                Object item = view.listaCategorias.getItemAt(i);
                if (item instanceof ComboItem){
                    if (((ComboItem)item).getKey()==idCatBuscado){
                        view.listaCategorias.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
        // seleccionar autor 
        if (libroEdit.getAutores() != null && !libroEdit.getAutores().isEmpty()){
            int idAutBuscado = libroEdit.getAutores().get(0).getIdAutor();
            // recorre la lista buscando el id del autor 
            for (int i = 0; i < view.listaAutores.getItemCount(); i++){
                Object item = view.listaAutores.getItemAt(i);
                if(item instanceof ComboItem){
                    if(((ComboItem)item).getKey() == idAutBuscado){
                        view.listaAutores.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    // metodo para editar los datos 
    private void procesarCambios() {
        if (view.txt_tituloEditLibro.getText().isEmpty()){ // se obliga a pasar un titulo
            JOptionPane.showMessageDialog(view, "El titulo es obligatorio");
            return;
        }
        
        // actualizar el objeto libroEdit
        libroEdit.setTitulo(view.txt_tituloEditLibro.getText()); // modifica el titulo
        libroEdit.setIsbn(view.txt_ISBNeditLibro.getText()); // modifica el isbn
        libroEdit.setDescripcion(view.txt_descripcionEditLibro.getText()); // modifica la descripcion
        try{ // intenta conventir las paginas en un entero 
            libroEdit.setNumeroPaginas(Integer.parseInt(view.txt_numPaginasEditLibro.getText()));
        }catch(NumberFormatException e ){ // lanza un error 
            libroEdit.setNumeroPaginas(0);
        }
        try{ // intenta dar formato a la fecha
            String fechaTxt = view.txt_fechaEditLibro.getText();
            if (!fechaTxt.isEmpty()){ // campo de fecha lleno
                libroEdit.setFechaPublicacion(LocalDate.parse(fechaTxt,  DateTimeFormatter.ofPattern("dd/MM/yyyy"))); // da formato a la fecha 
            }
        }catch(Exception e){  // captura errores
            System.out.println("No se puedde actualizar la fecha "  + e.getMessage());
        }
        
        // recuperar  categorias nueva
        Object itemCat = view.listaCategorias.getSelectedItem();
        if (itemCat instanceof ComboItem){
            Categoria c = new Categoria();
            c.setIdCategoria(((ComboItem)itemCat).getKey());
            libroEdit.setCategoria(c);
        }
        
        // recuperar  Autor nuevo
        Object itemAut = view.listaAutores.getSelectedItem();
        if (itemAut instanceof ComboItem){
            List<Autor> nuesvosAutores = new ArrayList<>();
            Autor a = new Autor();
            a.setIdAutor(((ComboItem)itemAut).getKey());
            nuesvosAutores.add(a);
            libroEdit.setAutores(nuesvosAutores);
        }
        
        // guardar en la BD los cambios 
        try { 
            libroService.editarLibro(libroEdit);
            JOptionPane.showMessageDialog(view, "Libro Actualizado correctamente");
            abrirMenuPrincipal();
        }catch(Exception e){ // captura un error 
            JOptionPane.showMessageDialog(view, "Error al actulizar "+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }           
    }

  
    // cierra la vista 
    private void abrirMenuPrincipal() {
        view.dispose(); // cierra la ventana 
    }

}

