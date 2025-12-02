package Service;

import Modelo.Autor;
import java.util.List;

public interface AutorService {
    
    void registrarAutor(Autor autor);

    List<Autor> mostrarAutores();

    void actualizarAutor(Autor autor);

    void desavilitarAutor(int id);
    
    // Método necesario para buscar un autor antes de editarlo o desactivarlo
    Autor buscarAutorPorId(int id);
}
