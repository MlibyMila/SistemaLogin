package Controlador.GestorAutores;

import Modelo.Autor;
import Modelo.Usuario;
import Service.AutorService;
import Service.impl.AutorServiceImpl;
import VistaNew.Autores.AutorPrincipal;
import Controlador.LogInControlador;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GesAutorPrincipalControlador {

    private AutorPrincipal view; // instanciar la ventana principal
    private Usuario usuario; // instanciar a un Usuario
    private AutorService service; // intancia al servicio (interfaces de metodos)
    private DefaultTableModel model; // intancia una tabla 

    // inicaliza los objetos instanciados 
    public GesAutorPrincipalControlador(Usuario usuario) {
        this.view = new AutorPrincipal(); // crea una nueva vista
        this.usuario = usuario; // iniciamos el usuario
        this.service = new AutorServiceImpl(); // Inicializamos el servicio
        
        this.cargarUsuario(); // metodo para cargar los datos del usuario 
        this.configurarListeners(); // acciones para los btns
        this.inicializarTabla(); // inicializa la tabla
    }
    
    // muestra la ventana principal 
    public void iniciarVista() {
        view.setVisible(true); // hace que la vista sea visble 
        view.setLocationRelativeTo(null); // muestra la ventana en el centro de la pantalla
        view.setTitle("Gestión de Autores"); // asigna un titulo a la ventana
    }

    // metodo para obtener datos basicos del Usuario adm
    private void cargarUsuario() {
        // encuentra usuario res 
        if (usuario != null) {
            view.txtEmailUsuario.setText(usuario.getEmail()); // obtener email
            view.txtNombreUsuario.setText(usuario.getNombres()); // obtener nombre
        }
    }

    // metodo para asignarle acciones a los btns de la ventana principal
    private void configurarListeners() {
        view.btn_añadirAutor.addActionListener(e -> abrirRegistro()); // registra new usuario
        view.btn_EditarAutor.addActionListener(e -> abrirEdicion());    // edita a un usuario
        view.btn_desabilitarAutor.addActionListener(e -> abrirDesactivar());     // desactiva a un usuario 
        view.btn_salir.addActionListener(e -> cerrarSesion());   // regresa al LogIn 
    }

    // metodo para mostrar los datos en la tabla de la vemtana principal
    private void inicializarTabla() {
        String[] columnas = {"ID", "Nombres", "Apellidos", "Nacionalidad", "Fecha Nac.", "Estado"}; // asigna nombres a cada campo 
        // hace que la tabla solo sea vista No editada directamente 
        model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // intancia una lista de Autores 
        List<Autor> autores = service.mostrarAutores();
        // imprime a cada autor (llenando la tabla)
        for (Autor a : autores) {
            model.addRow(new Object[]{
                a.getIdAutor(), // obtine su id 
                a.getNombres(), // obtiene el nombre 
                a.getApellidos(), // obtiene los apellidos 
                a.getNacionalidad(), // obtiene la nacionalidad
                a.getFechaNacimiento(), // obtine la fecha de nacimiento
                a.isEstado() ? "Activo" : "Inactivo" // obtiene el estado
            });
        }
        view.table_mostrarAutores.setModel(model); // muestra al autor en la tabla de la ventana principal
    }

    // metodo para obtener al libro seleccionado de la tabla de la ventana principal
    public Autor obtenerAutorSeleccionado() {
        int fila = view.table_mostrarAutores.getSelectedRow(); // almacena fila seleccionada
        if (fila == -1) { // no hay fila selecionada
            JOptionPane.showMessageDialog(view, "Seleccione un autor de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        // se instancia un objeto de tipo autor
        Autor autor = new Autor();
        try { // intenta obtener todos los datos del autor para ser editados 
            autor.setIdAutor(Integer.valueOf(view.table_mostrarAutores.getValueAt(fila, 0).toString()));
            autor.setNombres(view.table_mostrarAutores.getValueAt(fila, 1).toString());
            autor.setApellidos(view.table_mostrarAutores.getValueAt(fila, 2).toString());
            autor.setNacionalidad(view.table_mostrarAutores.getValueAt(fila, 3).toString());
            if (view.table_mostrarAutores.getValueAt(fila, 4) instanceof LocalDate localDate) {
                autor.setFechaNacimiento(localDate);
            }
            autor.setEstado(view.table_mostrarAutores.getValueAt(fila, 5).toString().equals("Activo"));
        } catch (NumberFormatException e) { // muestra mensaje de error
            System.out.println("Error al recuperar datos de la tabla: " + e.getMessage());
        }
        // retorna un objeto Autor 
        return autor;
    }

    // metodo para mostrar la ventana de registrar un nuevo Autor 
    private void abrirRegistro() {
        GesAutorRegisterControlador ctrl = new GesAutorRegisterControlador(this);
        ctrl.iniciarVista();
    }

    // metodo para mostrar la ventana de editar un autor  
    private void abrirEdicion() {
        Autor autor = obtenerAutorSeleccionado();
        // verfica si hay fila seleccionada para recuperar los datos a ser editado
        if (autor != null) { 
            GesAutorEditControlador ctrl = new GesAutorEditControlador(this);
            ctrl.cargarDatos(autor);
            ctrl.iniciarVista();
        }
    }

    private void abrirDesactivar() {
        // Pasar el autor seleccionado automáticamente
        Autor autor = obtenerAutorSeleccionado();
        GesAutorDesControlador ctrl = new GesAutorDesControlador(this);
        ctrl.iniciarVista();
        if (autor != null) {
            ctrl.prepararAutor(autor);
        }
    }
    
    // Método para actulizar la tabla 
    public void refrescarTabla() {
        inicializarTabla();
    }

    // metodo para regresar al LogIn 
    private void cerrarSesion() {
        view.dispose(); // cierra la ventana principal
        new LogInControlador().iniciarLogin();
    }
}
