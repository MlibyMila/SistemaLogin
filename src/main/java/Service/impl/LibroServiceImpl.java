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
    public void registrarLibro() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void desavilitarLibro(String ISBN) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Libro buscarLibroPorISBN(String isbn) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void editarLibro(Libro libro) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
