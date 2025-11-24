package Servise.impl;

import Dao.LibroDao;
import Modelo.Libro;
import Servise.LibroServise;
import java.util.List;

public class LibroServiseImpl implements LibroServise {

    private final LibroDao libroDao;

    public LibroServiseImpl() {
        this.libroDao = new LibroDao();
    }

    @Override
    public List<Libro> obtenerTodosLosLibros() {
        return libroDao.obtenerTodosLosLibros();
    }

    @Override
    public List<Libro> buscarLibrosPorCriterio(String criterio) {
        return libroDao.buscarLibrosPorCriterio(criterio);
    }
}
