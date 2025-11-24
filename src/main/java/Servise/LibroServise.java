package Servise;

import Modelo.Libro;
import java.util.List;

public interface LibroServise {
    List<Libro> obtenerTodosLosLibros();
    List<Libro> buscarLibrosPorCriterio(String criterio);
}