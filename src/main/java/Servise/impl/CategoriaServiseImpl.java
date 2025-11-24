package Servise.impl;

import Modelo.Categoria;
import Servise.CategoriaServise;
import java.util.List;
import Dao.CategoriaDao;

public class CategoriaServiseImpl implements CategoriaServise{
    private final CategoriaDao categoriaDao;

    public CategoriaServiseImpl(CategoriaDao categoriaDao) {
        this.categoriaDao = categoriaDao;
    }
    

    @Override
    public void registarCategoria(Categoria categoria) {
        categoriaDao.registrarCategoria(categoria);
    }

    @Override
    public List<Categoria> mostrarCategoria() {
        return categoriaDao.mostrarCategoria();
    }
    
}
