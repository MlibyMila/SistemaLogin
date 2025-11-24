package Servise.impl;

import Dao.EjemplarDao;
import Dao.PrestamoDao;
import Dao.UsuarioDao;
import Modelo.Ejemplar;
import Modelo.Prestamo;
import Modelo.Usuario;
import Servise.PrestamoServise;
import java.time.LocalDateTime;
import java.util.List;

public class PrestamoServiseImpl implements PrestamoServise {

    private final PrestamoDao prestamoDao;
    private final EjemplarDao ejemplarDao;
    private final UsuarioDao usuarioDao;

    public PrestamoServiseImpl() {
        this.prestamoDao = new PrestamoDao();
        this.ejemplarDao = new EjemplarDao();
        this.usuarioDao = new UsuarioDao();
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
        return new java.util.ArrayList<Prestamo>();
    }
}
