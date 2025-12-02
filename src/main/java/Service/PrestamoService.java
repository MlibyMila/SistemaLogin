package Service;

import Modelo.Prestamo;

import java.time.LocalDateTime;
import java.util.List;

public interface PrestamoService {

    boolean realizarPrestamo(int idUsuario, int idEjemplar, LocalDateTime fechaDevolucion);

    boolean registrarDevolucion(int idPrestamo, int idEjemplar); // Ojo: A veces solo con ID Prestamo basta si el DAO busca el ejemplar

    List<Prestamo> obtenerPrestamosActivos(int idUsuario);

    List<Prestamo> obtenerHistorialPrestamos(int idUsuario);

    List<Prestamo> obtenerTodosLosPrestamos();
    boolean desabilitarPrestamo(int idPrestamo);
}
