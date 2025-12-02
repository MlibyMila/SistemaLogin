package Service.impl;

import Modelo.Categoria;
import Service.CategoriaService;
import java.util.List;
import Dao.CategoriaDao;

public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaDao categoriaDao;

    public CategoriaServiceImpl(CategoriaDao categoriaDao) {
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
