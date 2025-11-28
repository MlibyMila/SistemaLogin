package Service;

import Modelo.Libro;
import java.util.List;

public interface LibroService {
    List<Libro> obtenerTodosLosLibros();
    void registrarLibro();
    List<Libro> buscarLibrosPorCriterio(String criterio);
    void desavilitarLibro(String ISBN);
    Libro buscarLibroPorISBN(String isbn);
}