package Dao;

import Conexion.ConexionSQLServer;
import Modelo.Autor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AutorDao {

    public void registrarAutor(Autor autor) {
        String sql = "INSERT INTO Autores ("
                + "Nombres,"
                + "Apellidos,"
                + "FechaNacimiento,"
                + "Nacionalidad,"
                + "FechaCreacion,"
                + "Estado)"
                + "VALUES (?,?,?,?,?,?)";
        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, autor.getNombres());
            pstmt.setString(2, autor.getApellidos());
            pstmt.setObject(3, autor.getFechaNacimiento());
            pstmt.setString(4, autor.getNacionalidad());
            pstmt.setObject(5, autor.getFechaCreacion());
            pstmt.setBoolean(6, autor.isEstado());
            pstmt.executeUpdate();
            System.out.println(
                    "Se ha registrado de manera exitosa al autor " + autor.getNombres() + autor.getApellidos());

        } catch (SQLException e) {
            System.out.println("Error al registrar Autor : " + e.getMessage());
        }
    }

    public List<Autor> mostrarAutores() {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT * FROM Autores WHERE Estado = 1S ";
        try (Connection con = ConexionSQLServer.getConexion();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Autor autor = new Autor();
                autor.setIdAutor(rs.getInt("IdAutor"));
                autor.setNombres(rs.getString("Nombres"));
                autor.setApellidos(rs.getString("Apellidos"));
                autor.setFechaNacimiento(rs.getObject("FechaNacimiento", LocalDate.class));
                autor.setNacionalidad(rs.getString("Nacionalidad"));
                autor.setFechaCreacion(rs.getObject("FechaCreacion", LocalDateTime.class));
                autor.setEstado(rs.getBoolean("Estado"));
                autores.add(autor);

            }

        } catch (SQLException e) {
            System.out.println("Error al mostrar Autor: " + e.getMessage());
        }
        return autores;
    }

    public void actualizarAutores(Autor autor) {
        String sql = "UPDATE Autores "
                + "SET Nombres = ?,"
                + "Apellidos = ?,"
                + "FechaNacimiento = ?,"
                + "Nacionalidad = ?,"
                + "FechaCreacion = ? ,"
                + "Estado = ?"
                + "WHERE IdAutor = ?";
        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, autor.getNombres());
            pstmt.setString(2, autor.getApellidos());
            pstmt.setObject(3, autor.getFechaNacimiento());
            pstmt.setString(4, autor.getNacionalidad());
            pstmt.setObject(5, autor.getFechaCreacion());
            pstmt.setBoolean(6, autor.isEstado());
            pstmt.setInt(7, autor.getIdAutor());
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Se ha actualizado con exito el registro del autor con ID : " + autor.getIdAutor());
            } else {
                System.out.println("No se ha podido encontrar al autor");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar Autor ");
        }

    }

    public void desavilitarAutor(int idAutor) {
        String sql = "UPDATE Autores SET Estado = ? WHERE IdAutor = ?";
        try (Connection con = ConexionSQLServer.getConexion();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setBoolean(1, false);
            pstmt.setInt(2, idAutor);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Se ha desavilitado con exito al autor con IS: " + idAutor);
            } else {
                System.out.println("No se ha encontrado al Autor con ID: " + idAutor);
            }

        } catch (SQLException e) {
            System.out.println("Error al desavilitar Autor: " + e.getMessage());
        }

    }
}
