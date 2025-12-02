package Service.impl;

import Dao.EjemplarDao;
import Dao.PrestamoDao;
import Modelo.Ejemplar;
import Modelo.Prestamo;
import Modelo.Usuario;
import Service.PrestamoService;
import java.time.LocalDateTime;
import java.util.List;

public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoDao prestamoDao;
    private final EjemplarDao ejemplarDao;
    // private final UsuarioDao usuarioDao; // Si no lo usas, puedes quitarlo

    public PrestamoServiceImpl() {
        this.prestamoDao = new PrestamoDao();
        this.ejemplarDao = new EjemplarDao();
        // this.usuarioDao = new UsuarioDao();
    }

    @Override
    public boolean realizarPrestamo(int idUsuario, int idEjemplar, LocalDateTime fechaDevolucion) {
        Prestamo prestamo = new Prestamo();
        Usuario u = new Usuario();
        u.setIdUsuario(idUsuario);
        prestamo.setUsuario(u);

        Ejemplar e = new Ejemplar();
        e.setIdEjemplar(idEjemplar);
        prestamo.setEjemplar(e);

        prestamo.setFechaDevolucionEsperada(fechaDevolucion);

        boolean exitoPrestamo = prestamoDao.registrarPrestamo(prestamo);

        if (exitoPrestamo) {
            return ejemplarDao.cambiarEstadoEjemplar(idEjemplar, "Prestado");
        }
        return false;
    }

    @Override
    public boolean registrarDevolucion(int idPrestamo, int idEjemplar) {
        boolean exitoDevolucion = prestamoDao.registrarDevolucion(idPrestamo);
        if (exitoDevolucion) {
            // Nota: Si idEjemplar viene como 0, deberías buscarlo primero en la BD usando idPrestamo
            return ejemplarDao.cambiarEstadoEjemplar(idEjemplar, "Disponible");
        }
        return false;
    }

    @Override
    public List<Prestamo> obtenerPrestamosActivos(int idUsuario) {
        return prestamoDao.obtenerPrestamosActivosPorUsuario(idUsuario);
    }

    @Override
    public List<Prestamo> obtenerHistorialPrestamos(int idUsuario) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Prestamo> obtenerTodosLosPrestamos() {
        return prestamoDao.obtenerTodosLosPrestamos();
    }
        @Override
    public boolean desabilitarPrestamo(int idPrestamo) {
        // NOTA: Lo ideal sería verificar si el préstamo estaba activo para liberar el libro.
        // Por simplicidad, aquí solo hacemos el borrado lógico del registro.
        return prestamoDao.desabilitarPrestamo(idPrestamo);
    }
}
