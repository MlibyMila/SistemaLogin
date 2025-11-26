package ControladorNew;

import Modelo.Usuario;

import VistaNew.Menu;

public class MenuController {

    private Menu vista;
    private Usuario usuario;

    public MenuController(Usuario usuario) {
        this.vista = new Menu();
        this.usuario = usuario;
        this.cargarUsuario();
        this.configuracionListeners();
    }

    public void iniciarMenu() {
        vista.setVisible(true);
        vista.setLocationRelativeTo(null);
    }

    public void iniciarLogin() {
        LogInControlador loginController = new LogInControlador();
        vista.dispose();
        loginController.iniciarLogin();
    }

    public void cargarUsuario() {
        vista.txtEmailUsuario.setText(usuario.getEmail());
        vista.txtNombreUsuario.setText(usuario.getNombres() + " " + usuario.getApellidos());
    }

    private void configuracionListeners() {
        vista.btnSalir.addActionListener((e) -> iniciarLogin());
    }

}
