package Dao;

import Conexion.ConexionSQLServer;
import Modelo.Categoria;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDao {


    public List<Categoria> mostrarCategoria() {
        List<Categoria> categorias = new ArrayList<>();
        // consultamos solo las categorias activas
        String sql = "SELECT idCategoria, Nombre "
                + "FROM Categorias "
                + " WHERE Estado = 1";
        try(Connection con = ConexionSQLServer.getConexion();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getInt("IdCategoria"));
                cat.setNombre(rs.getString("Nombre"));
                categorias.add(cat);
            }
            
        }catch(SQLException e){
            System.out.println("Error al listar Categorias: " + e.getMessage());
        }
        return categorias;
    }   
}