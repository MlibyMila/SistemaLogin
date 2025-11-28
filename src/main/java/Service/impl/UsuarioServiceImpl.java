package Service.impl;

import Dao.UsuarioDao;

import Modelo.Usuario;
import Service.UsuarioService;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UsuarioServiceImpl implements UsuarioService {
    private UsuarioDao usuarioDao;

    public UsuarioServiceImpl(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public UsuarioServiceImpl() {
        this.usuarioDao = new UsuarioDao();
    }

    @Override
    public void registrarUsuario(Usuario usuario) {

        String passwordPlana = usuario.getPasswordHash();

        String passwordHash = BCrypt.hashpw(passwordPlana, BCrypt.gensalt());

        usuario.setPasswordHash(passwordHash);

        usuarioDao.registrarUsuario(usuario);
    }

    @Override
    public List<Usuario> mostrarUsuario() {
        return usuarioDao.mostrarUsuario();
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {
        usuarioDao.actualizarUsuario(usuario);
    }

    @Override
    public void deshabilitarUsuario(int idUsuario) {

        usuarioDao.deshabilitarUsuario(idUsuario);
    }

    @Override
    public Usuario validarLogin(String email, String passwordPlana) {
        Usuario usuario = usuarioDao.buscarUsuarioPorEmail(email);
        if (usuario == null) {
            return null;
        }

        if (BCrypt.checkpw(passwordPlana, usuario.getPasswordHash())) {
            return usuario;
        } else {
            return null;
        }
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioDao.buscarUsuarioPorEmail(email);
    }

    @Override
    public Usuario buscarUsuarioPorId(int id) {
        return usuarioDao.buscarUsuarioPorId(id);
    }

}
