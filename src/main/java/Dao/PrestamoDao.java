package Dao;

import Conexion.ConexionSQLServer;
import Modelo.Prestamo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDao {

    public boolean registrarPrestamo(Prestamo prestamo) {
        String sql = "INSERT INTO Prestamos (IdEjemplar, IdUsuario, FechaDevolucionEsperada, EstadoPrestamo, Estado) " +
                "VALUES (?, ?, ?, 'Activo', 1)";

        try (Connection con = ConexionSQLServer.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, prestamo.getEjemplar().getIdEjemplar());
            pstmt.setInt(2, prestamo.getUsuario().getIdUsuario());
            pstmt.setObject(3, prestamo.getFechaDevolucionEsperada());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar préstamo: " + e.getMessage());
            return false;
        }
    }

    public List<Prestamo> obtenerPrestamosActivosPorUsuario(int idUsuario) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM Prestamos WHERE IdUsuario = ? AND EstadoPrestamo = 'Activo' AND Estado = 1";

        try (Connection con = ConexionSQLServer.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Prestamo p = new Prestamo();
                    p.setIdPrestamo(rs.getInt("IdPrestamo"));
                    p.setFechaPrestamo(rs.getObject("FechaPrestamo", LocalDateTime.class));
                    p.setFechaDevolucionEsperada(rs.getObject("FechaDevolucionEsperada", LocalDateTime.class));
                    // (Necesitaríamos cargar el Ejemplar y el Libro asociado)
                    prestamos.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener préstamos activos: " + e.getMessage());
        }
        return prestamos;
    }

    public boolean registrarDevolucion(int idPrestamo) {
        String sql = "UPDATE Prestamos SET EstadoPrestamo = 'Devuelto', FechaDevolucionReal = ? WHERE IdPrestamo = ?";

        try (Connection con = ConexionSQLServer.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setObject(1, LocalDateTime.now());
            pstmt.setInt(2, idPrestamo);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar devolución: " + e.getMessage());
            return false;
        }
    }

}
