package Service.impl;

import Modelo.Autor;
import Service.AutorService;
import java.util.List;
import Dao.AutorDao;

public class AutorServiceImpl implements AutorService {
    private final AutorDao autorDao;

    // Constructor que recibe el DAO (bueno para pruebas)
    public AutorServiceImpl(AutorDao autorDao) {
        this.autorDao = autorDao;
    }

    // Constructor por defecto
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

    // Implementación del nuevo método de búsqueda
    @Override
    public Autor buscarAutorPorId(int id) {
        // Asegúrate de que tu AutorDao tenga este método (ver abajo)
        return autorDao.buscarAutorPorId(id); 
    }
}
