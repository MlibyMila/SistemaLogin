package Service;

import Modelo.Prestamo;

import java.time.LocalDateTime;
import java.util.List;

public interface PrestamoService {
    boolean realizarPrestamo(int idUsuario, int idEjemplar, LocalDateTime fechaDevolucion);

    boolean registrarDevolucion(int idPrestamo, int idEjemplar);

    List<Prestamo> obtenerPrestamosActivos(int idUsuario);

    List<Prestamo> obtenerHistorialPrestamos(int idUsuario);
}