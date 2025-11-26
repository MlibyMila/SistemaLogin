package Service;

import Modelo.Autor;
import java.util.List;

public interface AutorService {
    void registrarAutor(Autor autor);

    List<Autor> mostrarAutores();

    void actualizarAutor(Autor autor);

    void desavilitarAutor(int id);
}
