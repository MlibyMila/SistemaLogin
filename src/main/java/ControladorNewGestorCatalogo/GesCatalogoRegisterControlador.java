/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControladorNewGestorCatalogo;

import Modelo.Libro;
import Service.LibroService;
import Service.impl.LibroServiceImpl;
import VistaNew.Catalogo.CatalogoAdd;

/**
 *
 * @author Milagritos
 */
public class GesCatalogoRegisterControlador {
    private CatalogoAdd view;
      private Libro libro;
    private LibroService service;
    public GesCatalogoRegisterControlador() {
        this.view = new CatalogoAdd();
        this.service = new LibroServiceImpl();
//        this.configuracionListeners();
//        this.initTablaUsuario();
    }
    public void inicializaRegistro(){
        
    }
    
}
