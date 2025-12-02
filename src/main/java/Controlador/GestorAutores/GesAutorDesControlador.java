package Controlador.GestorAutores;

import Modelo.Autor;
import Service.AutorService;
import Service.impl.AutorServiceImpl;
import VistaNew.Autores.DesactivarAutor;
import javax.swing.JOptionPane;

public class GesAutorDesControlador {

    private DesactivarAutor view; // instanciar a la vista 
    private AutorService service; // instanciar al servicio (interface de metodos)
    private GesAutorPrincipalControlador principalControlador; // instanciar al controlador principal

    // Inicializar cada uno de los objetos instanciados 
    public GesAutorDesControlador(GesAutorPrincipalControlador principal) {
        this.view = new DesactivarAutor();
        this.service = new AutorServiceImpl(); // impl. de los metodos def. en la inteface
        this.principalControlador = principal;
        configurarListeners(); // acciones de cada btn
    }

    // metodo para mostrar la ventana 
    public void iniciarVista() {
        view.setVisible(true); // hace visible la ventana
        view.setLocationRelativeTo(null); // centra la ventana
        view.setTitle("Desactivar Autor"); // asigna un titulo a la ventana 
        view.txt_nombreAutorDes.setEditable(false); // Solo lectura No editar
    }

    // metodo para pre-cargar datos 
    public void prepararAutor(Autor autor) {
        if (autor != null) {
            view.txt_IDdesabilitar.setText(String.valueOf(autor.getIdAutor())); // Obtine el Id del autor
            view.txt_nombreAutorDes.setText(autor.getNombres() + " " + autor.getApellidos()); // obtine el nombre completo del autor
        }
    }

    // metodo asigna acciones a los botones
    private void configurarListeners() {
        view.btn_desabilitar.addActionListener(e -> desactivar()); // desabilita al autor 
        view.btn_cancelarDesabilitar.addActionListener(e -> view.dispose()); // cancela la accion 
        view.txt_IDdesabilitar.addActionListener(e -> buscarNombre()); // busca al utor por nombre al dar enter 
    }

    // metodo para buscar al autor por su nombre
    private void buscarNombre() {
        String idTxt = view.txt_IDdesabilitar.getText().trim(); // obtenemos el id Autor
        if (idTxt.isEmpty()) {
            return; // retornamos si el campo esta vacio
        }
        try {
            int id = Integer.parseInt(idTxt); // intenta convertir id a entero 
            Autor autor = service.buscarAutorPorId(id); // busca al autor por id 
            if (autor != null) {   // encontro al autor 
                view.txt_nombreAutorDes.setText(autor.getNombres() + " " + autor.getApellidos()); // obtiene el nombre completo 
                view.btn_desabilitar.setEnabled(true); // autoriza btn desabilitar
            } else { // no encontro al autor
                view.txt_nombreAutorDes.setText("--- Autor no encontrado ---");
                view.btn_desabilitar.setEnabled(false); // no autoriza btn desactivar
            }
        } catch (NumberFormatException e) { // muestra un mensaje de error 
            JOptionPane.showMessageDialog(view, "ID inválido");
        }
    }

    // metodo para desactivar al Autor
    private void desactivar() {
        String idTxt = view.txt_IDdesabilitar.getText().trim(); // obtiene el Id autor 
        if (idTxt.isEmpty()) { // obliga a llenar los campos 
            JOptionPane.showMessageDialog(view, "Ingrese el ID del autor.");
            return;
        }

        // envia un mensaje de confirmacion
        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Está seguro de desactivar al autor con ID " + idTxt + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        // desactiva el aurtor 
        if (confirm == JOptionPane.YES_NO_OPTION) {
            try {
                int id = Integer.parseInt(idTxt); // intenta ´convertir a entero al id 
                service.desavilitarAutor(id); // se desactiva al autor 

                JOptionPane.showMessageDialog(view, "Autor desactivado.");
                if (principalControlador != null) {
                    principalControlador.refrescarTabla(); // se actualiza la tabla de la ventana principal 
                }
                view.dispose(); // muestra ventana principal 
            } catch (Exception e) { // muestra un mensaje de error 
                JOptionPane.showMessageDialog(view, "Error: " + e.getMessage());
            }
        }
    }
}
