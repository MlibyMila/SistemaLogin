package ControladorNew;

import ControladorNew.GestorUsuario.GesUsuarioPrincipalControlador;
import Modelo.Usuario;

import VistaNew.plantillaMenu;

public class MenuController {

    private plantillaMenu view;
    private Usuario usuario;

    public MenuController(Usuario usuario) {
        this.view = new plantillaMenu();
        this.usuario = usuario;
        this.cargarUsuario();
        this.configuracionListeners();
    }

    public void iniciarMenu() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }
    
    private void configuracionListeners() {
        view.btn_GestorUsuario.addActionListener((e)->{abrirGesUsuario(usuario);});
        view.btn_salir.addActionListener((e)->{iniciarLogin();});
    }

    public void iniciarLogin() {
        LogInControlador loginController = new LogInControlador();
        view.dispose();
        loginController.iniciarLogin();
    }
    
    public void abrirGesUsuario(Usuario usuario){
        GesUsuarioPrincipalControlador gesUsuarioRegisterController = new GesUsuarioPrincipalControlador(usuario);
        view.dispose();
        gesUsuarioRegisterController.iniciarUsuarioPrincipal();
    }

    public void cargarUsuario() {
        view.txtEmailUsuario.setText(usuario.getEmail());
        view.txtNombreUsuario.setText(usuario.getNombres() + " " + usuario.getApellidos());
    }


}
