package Service;

import Modelo.Usuario;
import java.util.List;

public interface UsuarioService {
    void registrarUsuario(Usuario usuario);

    List<Usuario> mostrarUsuario();

    void actualizarUsuario(Usuario usuario);

    void desabilitarUsuario(int idUsuario);

    Usuario validarLogin(String email, String passwordPlana);
}
