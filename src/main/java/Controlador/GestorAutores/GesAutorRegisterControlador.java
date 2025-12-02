package Controlador.GestorAutores;

import Modelo.Autor;
import Service.AutorService;
import Service.impl.AutorServiceImpl;
import VistaNew.Autores.EditarRegistrarAutor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

public class GesAutorRegisterControlador {

    private EditarRegistrarAutor view; // instancia una vista 
    private AutorService service; // instancia al servicio (interface de metodos)
    private GesAutorPrincipalControlador principalControlador; // instancia al controlador de la vista principal
 
    // se incializa los objetos instamciados
    public GesAutorRegisterControlador(GesAutorPrincipalControlador principal) {
        this.view = new EditarRegistrarAutor(); // inicia la vista 
        this.service = new AutorServiceImpl(); // inicia la impl de los metodos
        this.principalControlador = principal; // inicia el controlador principal
        configurarListeners(); // inicializa las acciones de los btnes 
    }

    // muestra ola ventana de registro
    public void iniciarVista() {
        view.setVisible(true); // hace la ventana visible 
        view.setLocationRelativeTo(null); // centra la ventana 
        view.setTitle("Registrar Nuevo Autor"); // asigna un titulo a la ventana
    }

    // asignar las acciones a cada btn 
    private void configurarListeners() {
        view.btn_guardarEditLibro.addActionListener(e -> registrar()); // btn para registrar
        view.btn_cancelarEditLibro.addActionListener(e -> view.dispose()); // btn para cancelar edicion 
    }

    // metodo para obtener y asigna datos del autor
    private void registrar() {
        String nombres = view.txt_editaNombreAutor.getText().trim(); // obtiene el nombre 
        String apellidos = view.txt_apellidosEditAutor.getText().trim(); // obtiene el apellido 
        String nacionalidad = view.txt_nacionalidadEditAutor.getText().trim(); // obtiene el nacionalidad 
        String fechaTxt = view.txt_fechaNacEditAutor.getText().trim(); // obtiene la fecha de nacimiento 

        if (nombres.isEmpty() || apellidos.isEmpty()) { // obliga a llenar campos escenciales 
            JOptionPane.showMessageDialog(view, "Nombre y Apellidos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // modificar los datos
        Autor autor = new Autor(); // intancia un autor 
        autor.setNombres(nombres); // pasa el nombre
        autor.setApellidos(apellidos); // pasa el apellido
        autor.setNacionalidad(nacionalidad); // pasa la nacionalidad
        autor.setFechaCreacion(LocalDateTime.now()); // obtiene la fecha actual 
        autor.setEstado(true); // estado activo por default 

        try {// intenta dar formato a la fecha
            if (!fechaTxt.isEmpty()) {
                autor.setFechaNacimiento(LocalDate.parse(fechaTxt, DateTimeFormatter.ofPattern("dd/MM/yyyy"))); // dar formato a la fecha
            }
        } catch (Exception e) { // captura errores 
            JOptionPane.showMessageDialog(view, "Formato de fecha incorrecto. Use dd/MM/yyyy", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try { // intenta registrar el autor
            service.registrarAutor(autor);
            JOptionPane.showMessageDialog(view, "Autor registrado exitosamente.");
            if (principalControlador != null) {
                principalControlador.refrescarTabla(); // actualiza la tabla
            }
            view.dispose(); // cierra la ventana 
        } catch (Exception e) { // captura errores
            JOptionPane.showMessageDialog(view, "Error al guardar: " + e.getMessage());
        }
    }
}
