package Service;

import Modelo.Libro;
import java.util.List;

public interface LibroService {
    List<Libro> obtenerTodosLosLibros();

    List<Libro> buscarLibrosPorCriterio(String criterio);
}