package Servise.impl;

import Modelo.Autor;
import Servise.AutorServise;
import java.util.List;
import Dao.AutorDao;

public class AutorServiseImpl implements AutorServise{
    private final AutorDao autorDao;

    public AutorServiseImpl(AutorDao autorDao) {
        this.autorDao = autorDao;
    }

    public AutorServiseImpl() {
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
