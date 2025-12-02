/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador.GestorCatalogo;

/**
 *
 * @author Milagritos
 */
// clase auxiliar
public class ComboItem {
    
    private int key; // guarda el ID 
    private String value; // guarda el nombre 
    
    // incializa los objetos intanciados 
    public ComboItem(int key, String value){
        this.key= key;
        this.value = value;
        
    }
    //  obtener los objetos intanciados (No se modifican por eso no hat Set)
    public int getKey(){
        return key;
    }
    public String getValue(){
        return value;
    }
    // JComboBox llama a este metodo para saber que mostrara en pantalla 
    @Override
    public String toString(){
        return value;
    }
}
