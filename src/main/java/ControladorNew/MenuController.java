package ControladorNew;

import Modelo.Usuario;
import Servise.UsuarioServise;
import VistaNew.Menu;

public class MenuController {

    private Menu vista;
    private Usuario usuario;

    public MenuController(Menu vista, Usuario usuario) {
        this.vista = vista;
        this.usuario = usuario;
        this.cargarUsuario();
    }
    
    public void cargarUsuario(){
        vista.txtEmailUsuario.setText(usuario.getEmail());
        vista.txtNombreUsuario.setText(usuario.getNombres() + usuario.getApellidos());
    }
    
    
    
}
