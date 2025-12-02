package Controlador.GestorCatalogo;

import Modelo.Libro;
import Service.LibroService;
import Service.impl.LibroServiceImpl;
import VistaNew.Catalogo.DesactivarLibros;
import javax.swing.JOptionPane;

public class GesCatalogoDesControlador {

    private DesactivarLibros view; // instanciar la vista 
    private LibroService service; // instanciar servicio (inteface de metodos)

    // inicalizar los objetos instanciados
    public GesCatalogoDesControlador() {
        this.view = new DesactivarLibros(); // inicializa la vista 
        this.service = new LibroServiceImpl(); // Inicializamos la implementación
        this.configurarListeners(); // incializa las acciones de los btns 
    }

    // muestra la ventana de Desactivar libro
    public void iniciarVista() {
        view.setVisible(true); // hace que la vista sea visible
        view.setLocationRelativeTo(null); // centra la ventana
        view.setTitle("Deshabilitar Libro"); // asigna un titulo a la ventana
        view.txt_tituloDesabilitar.setEditable(false);   // Deshabilitar edición del título para que sea solo lectura
    }

    // asigna acciones a los btns de la ventana 
    private void configurarListeners() {
        view.btn_desabilitar.addActionListener(e -> desactivarLibro());   // btn desactivar
        view.btn_cancelarDesabilitar.addActionListener(e -> cerrar());    // btn cancelar desavitacion
    }
    
    // metodo para desactivar libro
    private void desactivarLibro() {
        String isbn = view.txt_ISBNdesabilitar.getText().trim(); // obtine el isbn 

        if (isbn.isEmpty()) { // obliga a llenar el campo 
            JOptionPane.showMessageDialog(view, "El campo ISBN es obligatorio.");
            return;
        }

        //  buscar para obtener el título si el campo está vacío 
        String tituloConfirmacion = view.txt_tituloDesabilitar.getText(); // obtener el titulo
        if (tituloConfirmacion.isEmpty() || tituloConfirmacion.equals("---")) { // np hay titulo
            Libro l = service.buscarLibroPorISBN(isbn); // busca por isbn 
            if (l != null) { // encontro el libro
                tituloConfirmacion = l.getTitulo(); // optiene el titulo
            }
        }

        // Confirmación de seguridad
        int confirmacion = JOptionPane.showConfirmDialog(view,
                "¿Estás seguro de que deseas deshabilitar el libro:\n" + tituloConfirmacion + "?",
                "Confirmar Acción",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Llamamos al servicio
                service.desavilitarLibro(isbn);

                JOptionPane.showMessageDialog(view, "Libro deshabilitado correctamente.");
                limpiarCampos();
            } catch (Exception e) { // muestra el mensaje de error
                JOptionPane.showMessageDialog(view, "Error al deshabilitar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // merodo para limpiar los campos 
    private void limpiarCampos() {
        view.txt_ISBNdesabilitar.setText(""); // limpia el isbn 
        if (view.txt_tituloDesabilitar != null) { // si ingreso titulo limpia el campo
            view.txt_tituloDesabilitar.setText("");
        }
    }

    // metodo para regresar al LogIn
    private void cerrar() {
        view.dispose();
    }
}
