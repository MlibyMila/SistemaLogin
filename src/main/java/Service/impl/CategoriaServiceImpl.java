package Service.impl;

import Dao.CategoriaDao;
import Modelo.Categoria;
import Service.CategoriaService;
import java.util.List;

public class CategoriaServiceImpl implements CategoriaService {

    private CategoriaDao categoriaDao;

    public CategoriaServiceImpl() {
        this.categoriaDao = new CategoriaDao();
    }

    @Override
    public List<Categoria> mostrarCategoria() {
        return categoriaDao.mostrarCategoria();
    }
}
