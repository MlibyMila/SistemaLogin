package Dao;

import Conexion.ConexionSQLServer;
import Modelo.Libro;
import Modelo.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDao {

    public List<Libro> obtenerTodosLosLibros() {
        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT l.*, c.Nombre as NombreCategoria FROM Libros l " +
                "LEFT JOIN Categorias c ON l.IdCategoria = c.IdCategoria " +
                "WHERE l.Estado = 1";

        try (Connection con = ConexionSQLServer.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setIdLibro(rs.getInt("IdLibro"));
                libro.setTitulo(rs.getString("Titulo"));
                libro.setIsbn(rs.getString("ISBN"));
                libro.setFechaPublicacion(rs.getObject("FechaPublicacion", java.time.LocalDate.class));

                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getInt("IdCategoria"));
                cat.setNombre(rs.getString("NombreCategoria"));
                libro.setCategoria(cat);

                libros.add(libro);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los libros: " + e.getMessage());
        }
        return libros;
    }

    public List<Libro> buscarLibrosPorCriterio(String criterio) {
        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT l.*, c.Nombre as NombreCategoria FROM Libros l " +
                "LEFT JOIN Categorias c ON l.IdCategoria = c.IdCategoria " +
                "WHERE (l.Titulo LIKE ? OR l.ISBN LIKE ?) AND l.Estado = 1";

        try (Connection con = ConexionSQLServer.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            String criterioBusqueda = "%" + criterio + "%";
            pstmt.setString(1, criterioBusqueda);
            pstmt.setString(2, criterioBusqueda);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Libro libro = new Libro();
                    libro.setIdLibro(rs.getInt("IdLibro"));
                    libro.setTitulo(rs.getString("Titulo"));
                    libro.setIsbn(rs.getString("ISBN"));
                    libro.setFechaPublicacion(rs.getObject("FechaPublicacion", java.time.LocalDate.class));

                    Categoria cat = new Categoria();
                    cat.setIdCategoria(rs.getInt("IdCategoria"));
                    cat.setNombre(rs.getString("NombreCategoria"));
                    libro.setCategoria(cat);

                    libros.add(libro);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar libros: " + e.getMessage());
        }
        return libros;
    }

}