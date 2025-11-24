
import ControladorNew.LogInControlador;
import Servise.UsuarioServise;
import Servise.impl.UsuarioServiseImpl;
import VistaNew.LogIn;

public class Main {

    public static void main(String[] args) {
        LogIn view = new LogIn();
        UsuarioServise servise = new UsuarioServiseImpl();
        LogInControlador controlador = new LogInControlador(view, servise);
        view.setVisible(true);
    }

}
