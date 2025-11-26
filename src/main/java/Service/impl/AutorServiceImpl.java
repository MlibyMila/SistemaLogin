package Service.impl;

import Modelo.Autor;
import Service.AutorService;
import java.util.List;
import Dao.AutorDao;

public class AutorServiceImpl implements AutorService {
    private final AutorDao autorDao;

    public AutorServiceImpl(AutorDao autorDao) {
        this.autorDao = autorDao;
    }

    public AutorServiceImpl() {
        this.autorDao = new AutorDao();
    }

    @Override
    public void registrarAutor(Autor autor) {
        autorDao.registrarAutor(autor);
    }

    @Override
    public List<Autor> mostrarAutores() {
        return autorDao.mostrarAutores();
    }

    @Override
    public void actualizarAutor(Autor autor) {
        autorDao.actualizarAutores(autor);
    }

    @Override
    public void desavilitarAutor(int id) {
        autorDao.desavilitarAutor(id);
    }

}
