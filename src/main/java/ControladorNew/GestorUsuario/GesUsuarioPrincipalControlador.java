/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControladorNew.GestorUsuario;

import Service.UsuarioService;
import Service.impl.UsuarioServiceImpl;
import VistaNew.Usuario.UsuarioPrincipal;

/**
 *
 * @author Milagritos
 */
public final class GesUsuarioPrincipalControlador {

    private UsuarioPrincipal view;
    private UsuarioService service;

    public GesUsuarioPrincipalControlador() {
        this.view = new UsuarioPrincipal();
        this.service = new UsuarioServiceImpl();

        this.configuracionListeners();
    }

    public void iniciarUsuarioPrincipal() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    private void configuracionListeners() {
        view.btn_añadirAutor.addActionListener(e -> abrirUsuarioRegister());
        view.btn_EditarAutor.addActionListener(e -> abrirEditUsuario());
        view.btn_EditarAutor.addActionListener(e -> abrirDesUsuario());
    }

    private void abrirUsuarioRegister() {
        GesUsuarioRegisterControlador registerUsuarioController = new GesUsuarioRegisterControlador();
        view.dispose();
        registerUsuarioController.iniciarRegistro();
    }

    private void abrirEditUsuario() {
        GesUsuarioEditControlador gesUsuarioEdit = new GesUsuarioEditControlador();
        view.dispose();
        gesUsuarioEdit.iniciarActulizacion();
    }

    private void abrirDesUsuario() {

    }
}
