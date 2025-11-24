package Servise;

import Modelo.Autor;
import java.util.List;

public interface AutorServise {
    void registrarAutor (Autor autor);
    List<Autor> mostrarAutores();
    void actualizarAutor(Autor autor);
    void desavilitarAutor(int id);
}
