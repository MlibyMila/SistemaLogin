package Service.impl;

import Dao.LibroDao;
import Modelo.Libro;
import Service.LibroService;
import java.util.List;

public class LibroServiceImpl implements LibroService {

    private final LibroDao libroDao;

    public LibroServiceImpl() {
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

    @Override
    public void registrarLibro(Libro libro) {
        libroDao.registrarLibro(libro);
    }

    @Override
    public void desavilitarLibro(String ISBN) {
        // CORREGIDO: Llamamos al DAO
        libroDao.desavilitarLibro(ISBN);
    }

    @Override
    public Libro buscarLibroPorISBN(String isbn) {
        // CORREGIDO: Llamamos al DAO
        return libroDao.buscarLibroPorISBN(isbn);
    }

    @Override
    public void editarLibro(Libro libro) {
        // CORREGIDO: Llamamos al DAO
        libroDao.editarLibro(libro);
    }
}
