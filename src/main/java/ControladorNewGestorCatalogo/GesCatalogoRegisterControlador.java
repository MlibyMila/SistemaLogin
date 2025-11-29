/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControladorNewGestorCatalogo;

import ControladorNew.LogInControlador;
import Modelo.Libro;
import Modelo.Usuario;
import Service.LibroService;
import Service.impl.LibroServiceImpl;
import VistaNew.Catalogo.CatalogoAdd;

/**
 *
 * @author Milagritos
 */
public class GesCatalogoRegisterControlador {

    private CatalogoAdd view;
    private Usuario usuario;
    private Libro libro;
    private LibroService service;

    public GesCatalogoRegisterControlador() {
        this.view = new CatalogoAdd();
        this.service = new LibroServiceImpl();
//        this.configuracionListeners();
//        this.initTableLibros();
    }

    public void inicializaRegistro() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }
    public void cargarUsuario() {
        view.txtEmailUsuario.setText(usuario.getEmail());
        view.txtNombreUsuario.setText(usuario.getNombres() + " " + usuario.getApellidos());
    }
      private void configuracionListeners() {
//        view.btn_guardarAddLibro.addActionListener(e -> registrar());
//        view.btn_cancelarAddLibro.addActionListener(e -> limpiarCampos());
        view.btn_cancelarAddLibro.addActionListener(e -> abrirMenuPrincipal());
        view.btn_salir.addActionListener(e -> iniciarLogin());
    }
      private void abrirMenuPrincipal() {
        GesCatalogoPrincipalControlador gesCatalogoPrincipal = new GesCatalogoPrincipalControlador();
        view.dispose();
        gesCatalogoPrincipal.iniciarCatalogoPrincipal();
    }
          public void iniciarLogin() {
        LogInControlador loginController = new LogInControlador();
        view.dispose();
        loginController.iniciarLogin();
    }
          

}
