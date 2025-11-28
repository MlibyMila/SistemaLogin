/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControladorNew.GestorUsuario;

import Service.UsuarioService;
import Service.impl.UsuarioServiceImpl;
import VistaNew.Usuario.UsuarioDes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Milagritos
 */
public class GesUsuarioDesControlador {

    private UsuarioDes view;
    private UsuarioService service;

    public GesUsuarioDesControlador() {
        this.view = new UsuarioDes();
        this.service = new UsuarioServiceImpl();
        this.configuracionListeners();
    }
    public void iniciarDeshabilitar() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    private void configuracionListeners() {
        view.btn_desabilitarUsuario.addActionListener(e -> desabilitar());
        view.btn_cancelarDesabilitarUsuario.addActionListener(e -> limpiarCampos());
        view.btn_salir.addActionListener(e -> abrirUsuarioPrincipal());
    }

    private void limpiarCampos() {
        view.txt_IdUsuarioDesabilitar.setText("");
        view.txt_nombreDesabilitar.setText("");
    }

    public void desabilitar() {

        String idTexto = view.txt_IdUsuarioDesabilitar.getText().trim();
        String nombreConfirmacion = view.txt_nombreDesabilitar.getText().trim(); 
        
        if (!validarID(idTexto)) {
            return;
        }
        if (!validar(nombreConfirmacion)){
            return;
        }

        int idUsuario = Integer.parseInt(idTexto);
        int confirmacion = JOptionPane.showConfirmDialog(view, 
                "¿Está seguro de deshabilitar al usuario con ID: " + idUsuario + "?", 
                "Confirmar Acción", 
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                
                service.desabilitarUsuario(idUsuario);
                mostrarMensaje("Operación finalizada. El usuario ha sido deshabilitado (si existía).", "Información", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                abrirUsuarioPrincipal();
                
            } catch (Exception e) {
                mostrarMensaje("Ocurrió un error al intentar deshabilitar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    
    }

    private void abrirUsuarioPrincipal() {
        GesUsuarioPrincipalControlador gesUsuarioPrincipal = new GesUsuarioPrincipalControlador();
        view.dispose();
        gesUsuarioPrincipal.iniciarUsuarioPrincipal();
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(view, mensaje, titulo, tipo);
    }

    private boolean validar(String campo) {
        if (campo == null || campo.trim().isEmpty()) {
            return false;
        }
        String formatName = "^[A-Za-z]{2,}$";
        Pattern patron = Pattern.compile(formatName);
        Matcher confirmar = patron.matcher(campo.trim());
        return confirmar.matches();
    }

    private boolean validarID(String telefono) {
        if (telefono.matches("\\d+")) {
            return true;
        } else {
            mostrarMensaje("Ingresar un ID válido", "Error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }
}
