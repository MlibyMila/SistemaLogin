package Dao;

import Conexion.ConexionSQLServer;
import Modelo.Ejemplar;
import Modelo.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EjemplarDao {

    public List<Ejemplar> buscarEjemplaresDisponiblesPorLibro(int idLibro) {
        List<Ejemplar> ejemplares = new ArrayList<>();
        String sql = "SELECT * FROM Ejemplares WHERE IdLibro = ? AND EstadoCopia = 'Disponible' AND Estado = 1";

        try (Connection con = ConexionSQLServer.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, idLibro);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ejemplar ej = new Ejemplar();
                    ej.setIdEjemplar(rs.getInt("IdEjemplar"));
                    // Adaptamos el IdLibro al modelo actual creando la instancia Libro
                    Libro libro = new Libro();
                    libro.setIdLibro(rs.getInt("IdLibro"));
                    ej.setLibro(libro);
                    ej.setCodigoEjemplar(rs.getString("CodigoEjemplar"));
                    ej.setEstadoCopia(rs.getString("EstadoCopia"));
                    ejemplares.add(ej);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar ejemplares disponibles: " + e.getMessage());
        }
        return ejemplares;
    }

    public boolean cambiarEstadoEjemplar(int idEjemplar, String nuevoEstado) {
        String sql = "UPDATE Ejemplares SET EstadoCopia = ? WHERE IdEjemplar = ?";
        try (Connection con = ConexionSQLServer.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idEjemplar);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado de ejemplar: " + e.getMessage());
            return false;
        }
    }

}
