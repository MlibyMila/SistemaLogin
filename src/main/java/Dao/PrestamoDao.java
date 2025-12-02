package Dao;

import Conexion.ConexionSQLServer;
import Modelo.Ejemplar;
import Modelo.Libro;
import Modelo.Prestamo;
import Modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDao {
  
    // metodo para registrar un  nuevo prestamo 
    public boolean registrarPrestamo(Prestamo prestamo) {
        String sql = "INSERT INTO Prestamos (IdEjemplar, IdUsuario, FechaDevolucionEsperada, EstadoPrestamo, Estado) "
                + "VALUES (?, ?, ?, 'Activo', 1)";  // crear el stript de insertar un nuevo registro  

        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

    public List<Prestamo> obtenerTodosLosPrestamos() {
        List<Prestamo> prestamos = new ArrayList<>();
        // Unimos tablas para traer Nombre de Usuario y Título de Libro
        String sql = "SELECT p.IdPrestamo, p.FechaPrestamo, p.FechaDevolucionEsperada, p.EstadoPrestamo, "
                + "u.IdUsuario, u.Nombres, u.Apellidos, "
                + "e.IdEjemplar, l.Titulo "
                + "FROM Prestamos p "
                + "INNER JOIN Usuario u ON p.IdUsuario = u.IdUsuario "
                + "INNER JOIN Ejemplares e ON p.IdEjemplar = e.IdEjemplar "
                + "INNER JOIN Libros l ON e.IdLibro = l.IdLibro "
                + "WHERE p.Estado = 1"; // Solo préstamos que no hayan sido borrados lógicamente

        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setIdPrestamo(rs.getInt("IdPrestamo"));
                p.setFechaPrestamo(rs.getObject("FechaPrestamo", LocalDateTime.class));
                p.setFechaDevolucionEsperada(rs.getObject("FechaDevolucionEsperada", LocalDateTime.class));
                p.setEstadoPrestamo(rs.getString("EstadoPrestamo"));

                // Mapear Usuario (Solo lo necesario para mostrar)
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("IdUsuario"));
                u.setNombres(rs.getString("Nombres"));
                u.setApellidos(rs.getString("Apellidos"));
                p.setUsuario(u);

                // Mapear Ejemplar y Libro
                Ejemplar e = new Ejemplar();
                e.setIdEjemplar(rs.getInt("IdEjemplar"));
                Libro l = new Libro();
                l.setTitulo(rs.getString("Titulo"));
                e.setLibro(l); // Asignamos el libro al ejemplar
                p.setEjemplar(e);

                prestamos.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar préstamos: " + e.getMessage());
        }
        return prestamos;
    }

    public boolean registrarDevolucion(int idPrestamo) {
        String sql = "UPDATE Prestamos SET EstadoPrestamo = 'Devuelto', FechaDevolucionReal = ? WHERE IdPrestamo = ?";

        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setObject(1, LocalDateTime.now());
            pstmt.setInt(2, idPrestamo);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar devolución: " + e.getMessage());
            return false;
        }
    }
        public List<Prestamo> obtenerPrestamosActivosPorUsuario(int idUsuario) {
        List<Prestamo> prestamos = new ArrayList<>();
        // Unimos tablas para traer datos legibles
        String sql = "SELECT p.*, e.IdEjemplar, l.Titulo, u.Nombres, u.Apellidos "
                   + "FROM Prestamos p "
                   + "INNER JOIN Ejemplares e ON p.IdEjemplar = e.IdEjemplar "
                   + "INNER JOIN Libros l ON e.IdLibro = l.IdLibro "
                   + "INNER JOIN Usuario u ON p.IdUsuario = u.IdUsuario "
                   + "WHERE p.IdUsuario = ? AND p.EstadoPrestamo = 'Activo' AND p.Estado = 1";

        try (Connection con = ConexionSQLServer.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    prestamos.add(mapearPrestamo(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener préstamos por usuario: " + e.getMessage());
        }
        return prestamos;
    }
        
        
            private Prestamo mapearPrestamo(ResultSet rs) throws SQLException {
        Prestamo p = new Prestamo();
        p.setIdPrestamo(rs.getInt("IdPrestamo"));
        p.setFechaPrestamo(rs.getObject("FechaPrestamo", LocalDateTime.class));
        p.setFechaDevolucionEsperada(rs.getObject("FechaDevolucionEsperada", LocalDateTime.class));
        // Manejo de fecha real que puede ser null
        if(rs.getObject("FechaDevolucionReal") != null)
            p.setFechaDevolucionReal(rs.getObject("FechaDevolucionReal", LocalDateTime.class));
            
        p.setEstadoPrestamo(rs.getString("EstadoPrestamo"));

        // Usuario
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("IdUsuario"));
        u.setNombres(rs.getString("Nombres"));
        u.setApellidos(rs.getString("Apellidos"));
        p.setUsuario(u);

        // Ejemplar y Libro
        Ejemplar e = new Ejemplar();
        e.setIdEjemplar(rs.getInt("IdEjemplar"));
        Libro l = new Libro();
        l.setTitulo(rs.getString("Titulo"));
        e.setLibro(l);
        p.setEjemplar(e);
        
        return p;
    }
                public boolean desabilitarPrestamo(int idPrestamo) {
        String sql = "UPDATE Prestamos SET Estado = 0 WHERE IdPrestamo = ?";
        try (Connection con = ConexionSQLServer.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPrestamo);
            int filas = pstmt.executeUpdate();
            return filas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al deshabilitar préstamo: " + e.getMessage());
            return false;
        }
    }
}
