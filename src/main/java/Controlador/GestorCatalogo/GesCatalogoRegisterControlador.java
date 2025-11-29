package Controlador.GestorCatalogo;

import Controlador.LogInControlador;
import Modelo.Libro;
import Modelo.Usuario;
import Service.LibroService;
import Service.impl.LibroServiceImpl;
import VistaNew.Catalogo.EditarRegistrarLibro;

public class GesCatalogoRegisterControlador {

    private EditarRegistrarLibro view;
    private Usuario usuario;
    private Libro libro;
    private LibroService service;

    public GesCatalogoRegisterControlador() {
        this.view = new EditarRegistrarLibro();
        this.service = new LibroServiceImpl();
        this.configuracionListeners();
    }

    public void inicializaRegistro() {
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    private void configuracionListeners() {
//        view.btn_guardarAddLibro.addActionListener(e -> registrar());
//        view.btn_cancelarAddLibro.addActionListener(e -> limpiarCampos());
        view.btn_cancelarEditLibro.addActionListener(e -> abrirMenuPrincipal());
    }

    private void abrirMenuPrincipal() {
        view.dispose();
    }

}
