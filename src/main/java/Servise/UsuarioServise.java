package Servise;

import Modelo.Usuario;
import java.util.List;

public interface UsuarioServise {
    void registrarUsuario(Usuario usuario);
    List<Usuario> mostrarUsuario();
    void actualizarUsuario(Usuario usuario);
    void desabilitarUsuario(int idUsuario);
    Usuario validarLogin(String email, String passwordPlana);
}
