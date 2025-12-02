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
        String sql = "INSERT INTO Autores (Nombres, Apellidos, FechaNacimiento, Nacionalidad, FechaCreacion, Estado) VALUES (?,?,?,?,?,?)";
        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, autor.getNombres());
            pstmt.setString(2, autor.getApellidos());
            pstmt.setObject(3, autor.getFechaNacimiento());
            pstmt.setString(4, autor.getNacionalidad());
            pstmt.setObject(5, autor.getFechaCreacion());
            pstmt.setBoolean(6, autor.isEstado());
            pstmt.executeUpdate();
            System.out.println("Autor registrado: " + autor.getNombres());
        } catch (SQLException e) {
            System.out.println("Error al registrar Autor: " + e.getMessage());
        }
    }

    public List<Autor> mostrarAutores() {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT * FROM Autores WHERE Estado = 1";
        try (Connection con = ConexionSQLServer.getConexion(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                autores.add(mapearAutor(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar Autores: " + e.getMessage());
        }
        return autores;
    }

    // --- NUEVO: Buscar Autor por ID ---
    public Autor buscarAutorPorId(int idAutor) {
        Autor autor = null;
        String sql = "SELECT * FROM Autores WHERE IdAutor = ? AND Estado = 1";
        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, idAutor);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    autor = mapearAutor(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar autor por ID: " + e.getMessage());
        }
        return autor;
    }

    public void actualizarAutores(Autor autor) {
        String sql = "UPDATE Autores SET Nombres=?, Apellidos=?, FechaNacimiento=?, Nacionalidad=? WHERE IdAutor=?";
        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, autor.getNombres());
            pstmt.setString(2, autor.getApellidos());
            pstmt.setObject(3, autor.getFechaNacimiento());
            pstmt.setString(4, autor.getNacionalidad());
            pstmt.setInt(5, autor.getIdAutor());
            
            int filas = pstmt.executeUpdate();
            if (filas > 0) System.out.println("Autor actualizado ID: " + autor.getIdAutor());
            else System.out.println("No se encontró autor para actualizar.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar Autor: " + e.getMessage());
        }
    }

    public void desavilitarAutor(int idAutor) {
        String sql = "UPDATE Autores SET Estado = 0 WHERE IdAutor = ?";
        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, idAutor);
            int filas = pstmt.executeUpdate();
            if (filas > 0) System.out.println("Autor deshabilitado ID: " + idAutor);
            else System.out.println("No se encontró autor ID: " + idAutor);
        } catch (SQLException e) {
            System.out.println("Error al deshabilitar Autor: " + e.getMessage());
        }
    }

    private Autor mapearAutor(ResultSet rs) throws SQLException {
        Autor autor = new Autor();
        autor.setIdAutor(rs.getInt("IdAutor"));
        autor.setNombres(rs.getString("Nombres"));
        autor.setApellidos(rs.getString("Apellidos"));
        autor.setFechaNacimiento(rs.getObject("FechaNacimiento", LocalDate.class));
        autor.setNacionalidad(rs.getString("Nacionalidad"));
        autor.setFechaCreacion(rs.getObject("FechaCreacion", LocalDateTime.class));
        autor.setEstado(rs.getBoolean("Estado"));
        return autor;
    }
}
