package Servise;

import Modelo.Categoria;
import java.util.List;

public interface CategoriaServise {

    void registarCategoria(Categoria categoria);

    List<Categoria> mostrarCategoria();

}
