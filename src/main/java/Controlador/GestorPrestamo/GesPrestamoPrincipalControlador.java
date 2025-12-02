package Controlador.GestorPrestamo;

import Modelo.Prestamo;
import Modelo.Usuario;
import Service.PrestamoService;
import Service.impl.PrestamoServiceImpl;
import VistaNew.PrestamosDevolucion.MenuPrestamo;
import Controlador.LogInControlador;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GesPrestamoPrincipalControlador {

    private MenuPrestamo view; // instancuar la vista 
    private Usuario usuarioAdmin; // instnaciar a Usuario 
    private PrestamoService service; // instanciar al servicio (interface de metodos)
    private DefaultTableModel model; // instanciar una tabla
    
    // inicalizar los objetos instanciados 
    public GesPrestamoPrincipalControlador(Usuario usuario) {
        this.view = new MenuPrestamo(); // inicializa la vista como el menu principal 
        this.usuarioAdmin = usuario; // inicaliza al usuario
        this.service = new PrestamoServiceImpl(); // inicializa la implementacion 
        
        cargarDatosUsuario(); // cargar los datos del usuario
        inicializarTabla();  // uncializar la tabla con datos 
        configurarListeners(); // inicaliza las acciones de los btnes 
    }

    // metodo para mostrar el menu principal
    public void iniciarVista() {
        view.setVisible(true); // hace que el menu sea visible
        view.setLocationRelativeTo(null); // centra la ventana 
        view.setTitle("Gestión de Préstamos"); // asigna un titulo a la ventana 
    }

    // metodo que obtine los datos del usuario adm
    private void cargarDatosUsuario() { 
        if (usuarioAdmin != null) { // existe - obtine el email y nombre 
            view.txt_emailUsuario.setText(usuarioAdmin.getEmail()); // pasa el email a la ventana 
            view.txt_bienvenidaUsuario.setText(usuarioAdmin.getNombres()); // pasa el nombre a la ventana 
        }
    }

    // Metodo para asignar acciones a los btnes
    private void configurarListeners() {
        view.btn_añadirPrestamo.addActionListener(e -> abrirNuevoPrestamo()); // btn para registrar prestamo
        view.btn_DesabilitarPrestamo.setText("Registrar Devolución"); // 
        view.btn_DesabilitarPrestamo.addActionListener(e -> deshabilitarPrestamo()); //btn para desabilitar prestamo (devolver libro) 
        view.btn_salir.addActionListener(e -> cerrarSesion()); // btn abre ele LogIn
    }

    // metodo opara cargar los datos en la tabla
    public void inicializarTabla() {
        String[] columnas = {"ID", "Usuario", "Libro", "F. Préstamo", "F. Devolución", "Estado"}; // asignar nombres a los campos 
       
        // hace que la tabla solo sea visible NO editable 
        model = new DefaultTableModel(columnas, 0) { 
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // llanma al servicio de optener los datps de prestamo 
        List<Prestamo> lista = service.obtenerTodosLosPrestamos(); // intancia una lista para almacenarlos 
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // asigna un formato a las fechas 

        // obtener los datos e ir llenando la tabla 
        for (Prestamo p : lista) {
            String fechaPrestamo = (p.getFechaPrestamo() != null) ? p.getFechaPrestamo().format(fmt) : "-"; // obtiene la fechaPrestamo si existe 
            String fechaDev = (p.getFechaDevolucionEsperada() != null) ? p.getFechaDevolucionEsperada().format(fmt) : "-";  // obtiene la fecha de debolucion si existe 
            String nombreUsuario = (p.getUsuario() != null) ? p.getUsuario().getNombres() + " " + p.getUsuario().getApellidos() : "Desc."; // obtiene el nombre de usuario 
            String tituloLibro = (p.getEjemplar() != null && p.getEjemplar().getLibro() != null) ? p.getEjemplar().getLibro().getTitulo() : "Desc."; // obtiene el nombre del autor

            // llena la tabla con el obejeto prestmo
            model.addRow(new Object[]{
                p.getIdPrestamo(),
                nombreUsuario,
                tituloLibro,
                fechaPrestamo,
                fechaDev,
                p.getEstadoPrestamo()
            });
        }
        // agrega  a la tabla de la vista el obeto obtenido
        view.table_mostrarPrestamos.setModel(model);
    }

    // metodo que nos permite devolver libro 
    private void deshabilitarPrestamo() {
        int fila = view.table_mostrarPrestamos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Seleccione un préstamo de la tabla para eliminarlo.");
            return;
        }

        int idPrestamo = Integer.parseInt(view.table_mostrarPrestamos.getValueAt(fila, 0).toString());
        
        int confirm = JOptionPane.showConfirmDialog(view, 
                "¿Está seguro de eliminar este registro de préstamo?\nEsto lo ocultará del sistema.",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (service.desabilitarPrestamo(idPrestamo)) {
                JOptionPane.showMessageDialog(view, "Préstamo eliminado/deshabilitado correctamente.");
                inicializarTabla(); // Refrescar la tabla
            } else {
                JOptionPane.showMessageDialog(view, "Error al eliminar el préstamo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Metodo para mostrar la ventana de nuevo registro 
    private void abrirNuevoPrestamo() {
        GesPrestamoRegisterControlador ctldr = new GesPrestamoRegisterControlador(this);
        ctldr.iniciarVista();
        
    }

    // metodo para abrir LogIn 
    private void cerrarSesion() {
        view.dispose();
        new LogInControlador().iniciarLogin();
    }
}
