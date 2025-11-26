package Service;

import Modelo.Categoria;
import java.util.List;

public interface CategoriaService {

    void registarCategoria(Categoria categoria);

    List<Categoria> mostrarCategoria();

}
