package Controlador.GestorPrestamo;

import Service.PrestamoService;
import Service.impl.PrestamoServiceImpl;
import VistaNew.PrestamosDevolucion.EditarRegistrarPrestamo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;

public class GesPrestamoRegisterControlador {

    private EditarRegistrarPrestamo view; // instanciar la ventana de registro 
    private PrestamoService service; // instancia el servicio (interfaz de metodos)
    private GesPrestamoPrincipalControlador principalControlador; // instanciar 

    // Constructor
    public GesPrestamoRegisterControlador(GesPrestamoPrincipalControlador principal) {
        this.view = new EditarRegistrarPrestamo();
        this.service = new PrestamoServiceImpl();
        this.principalControlador = principal; // Referencia para actualizar la tabla principal

        configurarListeners();
    }

    public void iniciarVista() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
        view.setTitle("Registrar Nuevo Préstamo");

        // Sugerir fecha de devolución (ej: 7 días desde hoy)
        LocalDate fechaSugerida = LocalDate.now().plusDays(7);
        view.txt_fechaDevolucion.setText(fechaSugerida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void configurarListeners() {
        view.btn_guardar.addActionListener(e -> registrarPrestamo());
        view.btn_cancelar.addActionListener(e -> cerrar());
    }

    private void registrarPrestamo() {
        // 1. Validar que no haya campos vacíos
        if (view.txt_idUsuario.getText().isEmpty()
                || view.txt_idEjemplar.getText().isEmpty()
                || view.txt_fechaDevolucion.getText().isEmpty()) {

            JOptionPane.showMessageDialog(view, "Todos los campos (Usuario, Ejemplar, Fecha) son obligatorios.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 2. Convertir los datos de la vista
            int idUsuario = Integer.parseInt(view.txt_idUsuario.getText().trim());
            int idEjemplar = Integer.parseInt(view.txt_idEjemplar.getText().trim());

            // Manejo de Fecha
            String fechaTexto = view.txt_fechaDevolucion.getText().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaLocal = LocalDate.parse(fechaTexto, formatter);

            // Convertimos a LocalDateTime (asumiendo final del día para la devolución)
            LocalDateTime fechaDevolucion = fechaLocal.atTime(23, 59, 59);

            // 3. Validar que la fecha no sea anterior a hoy
            if (fechaLocal.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(view, "La fecha de devolución no puede ser anterior a hoy.",
                        "Fecha Inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 4. Llamar al servicio para realizar el préstamo
            // El servicio se encarga de verificar si el libro está disponible y cambiar su estado
            boolean exito = service.realizarPrestamo(idUsuario, idEjemplar, fechaDevolucion);

            if (exito) {
                JOptionPane.showMessageDialog(view, "¡Préstamo registrado exitosamente!");

                // Actualizamos la tabla en la ventana principal si existe
                if (principalControlador != null) {
                    principalControlador.inicializarTabla();
                }
                cerrar(); // Cerramos la ventana de registro
            } else {
                JOptionPane.showMessageDialog(view,"No se pudo registrar el préstamo","Error en el Registro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Los IDs de Usuario y Ejemplar deben ser números enteros.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(view, "Formato de fecha incorrecto.",
                    "Error de Fecha", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Ocurrió un error inesperado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // cierra la ventana  especifica (registro) y regresa al menu principal 
    private void cerrar() {
        view.dispose();
    }
}
