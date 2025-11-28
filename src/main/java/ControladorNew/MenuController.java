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

    public void iniciarLogin() {
        LogInControlador loginController = new LogInControlador();
        view.dispose();
        loginController.iniciarLogin();
    }
    public void abrirGesUsuario(){
        GesUsuarioPrincipalControlador gesUsuarioRegisterController = new GesUsuarioPrincipalControlador();
        view.dispose();
        gesUsuarioRegisterController.iniciarUsuarioPrincipal();
    }
    public void abrirGesCatalogo(){
        
    }
   

    public void cargarUsuario() {
        view.txtEmailUsuario.setText(usuario.getEmail());
        view.txtNombreUsuario.setText(usuario.getNombres() + " " + usuario.getApellidos());
    }

    private void configuracionListeners() {
        view.btn_salir.addActionListener((e) -> iniciarLogin());
         view.btn_GestorUsuario.addActionListener(e -> abrirGesUsuario());
          view.btn_gestorCatalogo.addActionListener(e -> abrirGesCatalogo() );
//        view.btn_gestorAutores.addActionListener(e ->);
//        view.btn_gestorPrestamos.addActionListener(e -> );
    }

}
