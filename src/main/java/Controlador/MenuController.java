package Controlador;

import Controlador.GestorCatalogo.GesCatalogoPrincipalControlador;
import Controlador.GestorUsuario.GesUsuarioPrincipalControlador;
import Modelo.Usuario;

import VistaNew.PlantillaMenu;

public class MenuController {

    private PlantillaMenu view;
    private Usuario usuario;

    public MenuController(Usuario usuario) {
        this.view = new PlantillaMenu();
        this.usuario = usuario;
        this.cargarUsuario();
        this.configuracionListeners();
    }

    public void cargarUsuario() {
        view.txt_emailUsuario.setText(usuario.getEmail());
        view.txt_bienvenidaUsuario.setText("¡Bienvenido al Sistema!" + " " + usuario.getNombres());
    }

    public void iniciarMenu() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    private void configuracionListeners() {
        view.btn_GestorUsuario.addActionListener(e -> abrirGesUsuario(usuario));
        view.btn_gestorCatalogo.addActionListener(e -> abrirGesCatalogo(usuario));
        view.btn_salir.addActionListener(e ->iniciarLogin());
    }

    public void iniciarLogin() {
        view.dispose();
        new LogInControlador().iniciarLogin();
    }

    public void abrirGesUsuario(Usuario usuario) {
        view.dispose();
        new GesUsuarioPrincipalControlador(usuario).iniciarMenuUsuario();
    }

    public void abrirGesCatalogo(Usuario usuario) {
        view.dispose();
        new GesCatalogoPrincipalControlador(usuario).iniciarCatalogoPrincipal();
    }

}
