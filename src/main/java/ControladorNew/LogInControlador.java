package ControladorNew;

import Dao.UsuarioDao;
import Modelo.Usuario;
import Servise.UsuarioServise;
import Servise.impl.UsuarioServiseImpl;
import VistaNew.LogIn;
import VistaNew.Menu;
import VistaNew.RegistroView;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class LogInControlador {

    private LogIn vista;
    private UsuarioServise usuarioServise;

    public LogInControlador(LogIn vista, UsuarioServise usuarioServise) {
        this.vista = vista;
        this.usuarioServise = usuarioServise;
        configuracionListeners();
    }
    
    public void inicializarIconos(){
        vista.ImgLogin.setIcon(new ImageIcon("../imagenes/loginIcon.jpg"));
//                setIconImage(new ImageIcon(getClass().getResource("/imagenes/loginIcon.jpg")).getImage());
    }

    private void configuracionListeners() {
        vista.btn_ingresar.addActionListener((e) -> {
            login();
        });
        vista.txt_registrar.addActionListener((e) -> {
            abrirVentanaRegistro();
        });
    }

    private void login() {
        String email = vista.txt_usuario.getText();
        String password = new String(vista.txt_password.getPassword());
        if (!validarCampo(email, password)) {
            return;
        }

        Usuario usuario = usuarioServise.validarLogin(email, password);

        if (usuario != null) {
            mostrarMensaje("LogIn exitoso!", "Exito!", JOptionPane.INFORMATION_MESSAGE);
            abrirVentanaPrincipal(usuario);

        } else {
            mostrarMensaje("Email o contraseña incorrectos", "Error", JOptionPane.ERROR);
        }
    }

    private void register() {
        String email = vista.txt_usuario.getText();
        String password = new String(vista.txt_password.getPassword());

        if (!validarCampo(email, password)) {
            return;
        }

        UsuarioDao usuarioDao = new UsuarioDao();
        if (usuarioDao.buscarUsuarioPorEmail(email) != null) {
            mostrarMensaje("Este Email ya esta registrado!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombres("Nuevo");
        nuevoUsuario.setApellidos("Usuario");
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPasswordHash(password);
        nuevoUsuario.setTelefono("");
        nuevoUsuario.setDireccion("");
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());
        nuevoUsuario.setEstado(true);

        usuarioServise.registrarUsuario(nuevoUsuario);
        mostrarMensaje("Registro exitoso!", "Exito!", JOptionPane.INFORMATION_MESSAGE);
        limpiarCampos();

    }

    private boolean validarCampo(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Por favor, completar los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validarEmail(email)) {
            mostrarMensaje("Por favor, ingresar un email valido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String formatoEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern patron = Pattern.compile(formatoEmail);
        Matcher confirmar = patron.matcher(email.trim());
        return confirmar.matches();
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this.vista, mensaje, titulo, tipo);
    }

    private void limpiarCampos() {
        vista.txt_usuario.setText("");
        vista.txt_password.setText("");
    }

    private void abrirVentanaPrincipal(Usuario usuario){
        Menu menu = new Menu();
        MenuController menuController = new MenuController(menu, usuario );
        menu.setVisible(true);
        vista.dispose();
    }

    private void abrirVentanaRegistro() {
        vista.dispose();
        RegistroView principalView = new RegistroView();
        principalView.setVisible(true);
    }

}
