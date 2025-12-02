package Controlador.GestorAutores;

import Modelo.Autor;
import Service.AutorService;
import Service.impl.AutorServiceImpl;
import VistaNew.Autores.EditarRegistrarAutor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

public class GesAutorEditControlador {

    private EditarRegistrarAutor view; // instanciar la vista 
    private AutorService service; // instanciar el servicio (interface de metodos)
    private GesAutorPrincipalControlador principalControlador; // instanciar al controlador de la vista principal
    private Autor autorEdicion; // instanciar un autor para editar 

    // inicalizarr los objetos instanciados 
    public GesAutorEditControlador(GesAutorPrincipalControlador principal) {
        this.view = new EditarRegistrarAutor(); // inicializa la vista 
        this.service = new AutorServiceImpl(); // inicia la implementacion 
        this.principalControlador = principal; // inicliza el controlaodr principal
        configurarListeners(); // inicia las asigna acciones de los btnes 
    }

    // muestra la ventana de editar Autor
    public void iniciarVista() {
        view.setVisible(true); // hace visible la vista 
        view.setLocationRelativeTo(null); // centra la ventana 
        view.setTitle("Editar Autor"); // asigna un titulo a la vemtana
        view.btn_guardarEditLibro.setText("Actualizar"); // actuliza el texto del btn
    }

    // obtener los datos del autor para llenar la tabla
    public void cargarDatos(Autor autor) {
        if (autor == null) {
            JOptionPane.showMessageDialog(view, "Error, no ha seleccionado un autor válido");
            view.dispose();
            return;
        }

        this.autorEdicion = autor; // incializa al autor 
        view.txt_editaNombreAutor.setText(autor.getNombres()); // obtine el nombre y le asigna al campo
        view.txt_apellidosEditAutor.setText(autor.getApellidos()); // obtine el apellido y le asigna al campo
        view.txt_nacionalidadEditAutor.setText(autor.getNacionalidad()); // obtine la nacionalidad y le asigna al campo

        if (autor.getFechaNacimiento() != null) { // hay fecha de nacimiento 
            // obtine la fecha le da formato y le asigna al campo
            view.txt_fechaNacEditAutor.setText(autor.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

    // asignarle acciones a los btnes 
    private void configurarListeners() {
        view.btn_guardarEditLibro.addActionListener(e -> actualizar()); // btn actualiza 
        view.btn_cancelarEditLibro.addActionListener(e -> view.dispose()); // btn cancelar 
    }

    // metodo que actuliza datos del autor 
    private void actualizar() {
        if (autorEdicion == null) {
            return; // no existe el autor 
        }
        String nombres = view.txt_editaNombreAutor.getText().trim(); // obtine el nombre acttulizado  
        String apellidos = view.txt_apellidosEditAutor.getText().trim(); // obtiene el apellido acttulizado

        if (nombres.isEmpty() || apellidos.isEmpty()) { // manda mensaje de llenar campos obligatorios 
            JOptionPane.showMessageDialog(view, "Nombre y Apellidos son obligatorios.");
            return;
        }

        autorEdicion.setNombres(nombres); // asigna al autor su nombre acttulizado
        autorEdicion.setApellidos(apellidos); // asigna al autor apellido acttulizado
        autorEdicion.setNacionalidad(view.txt_nacionalidadEditAutor.getText().trim()); // asigna al autor nacionalidad acttulizado
        String fechaTxt = view.txt_fechaNacEditAutor.getText().trim();
        if (!fechaTxt.isEmpty()) { // hay fecha ingresada
            try { // intenta dar formato a la fecha de nacimiento
                LocalDate fechaNueva = LocalDate.parse(fechaTxt, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                autorEdicion.setFechaNacimiento(fechaNueva);

                autorEdicion.setFechaNacimiento(LocalDate.parse(fechaTxt, DateTimeFormatter.ofPattern("dd/MM/yyyy"))); // le da formato
            } catch (Exception e) { // lanza un error 
                JOptionPane.showMessageDialog(view, "La fecha ingresada es inválida");
                return;
            }
        }else{
            autorEdicion.setFechaNacimiento(null);
        }

        try { // intenta actulizar el Autor
            service.actualizarAutor(autorEdicion); // llama al servicio
            JOptionPane.showMessageDialog(view, "Autor actualizado correctamente.");
            if (principalControlador != null) {
                principalControlador.refrescarTabla(); // actualiza la tabla 
            }
            view.dispose();  // cierra la ventana 
        } catch (Exception e) { // captura errores 
            JOptionPane.showMessageDialog(view, "Error al actualizar: " + e.getMessage());
        }
    }
}
