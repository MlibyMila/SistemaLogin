package Dao;

import Modelo.Usuario;
import java.sql.Connection;
import Conexion.ConexionSQLServer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    public void registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuario (Nombres, Apellidos, Email, PasswordHash, Telefono, Direccion, FechaCreacion, Estado)"
                + " VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = ConexionSQLServer.getConexion(); 
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombres());
            pstmt.setString(2, usuario.getApellidos());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getPasswordHash());
            pstmt.setInt(5, usuario.getTelefono());
            pstmt.setString(6, usuario.getDireccion());
            pstmt.setObject(7, usuario.getFechaCreacion());
            pstmt.setBoolean(8, usuario.isEstado());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al registrar Usuario: " + e.getMessage());
        }
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        String sql = "SELECT * FROM Usuario WHERE Email = ? AND Estado = 1";
        try (Connection con = ConexionSQLServer.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("IdUsuario"));
                    usuario.setNombres(rs.getString("Nombres"));
                    usuario.setApellidos(rs.getString("Apellidos"));
                    usuario.setEmail(rs.getString("Email"));
                    usuario.setPasswordHash(rs.getString("PasswordHash"));
                    usuario.setTelefono(rs.getInt("Telefono"));
                    usuario.setDireccion(rs.getString("Direccion"));
                    usuario.setFechaCreacion(rs.getObject("FechaCreacion", LocalDateTime.class));
                    usuario.setEstado(rs.getBoolean("Estado"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario por email: " + e.getMessage());
        }
        return null;
    }

    public List<Usuario> mostrarUsuario() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario ";
        try (Connection con = ConexionSQLServer.getConexion(); 
                Statement stmt = con.createStatement(); 
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("IdUsuario"));
                usuario.setNombres(rs.getString("Nombres"));
                usuario.setApellidos(rs.getString("Apellidos"));
                usuario.setEmail(rs.getString("Email"));
                usuario.setPasswordHash(rs.getString("PasswordHash"));
                usuario.setTelefono(rs.getInt("Telefono"));
                usuario.setDireccion(rs.getString("Direccion"));
                usuario.setFechaCreacion(rs.getObject("FechaCreacion", LocalDateTime.class));
                usuario.setEstado(rs.getBoolean("Estado"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar usuario" + e.getMessage());
        }
        return usuarios;
    }

    public void actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE Usuario SET "
                + "Nombres = ?, "
                + "Apellidos = ?, "
                + "Email = ?, "
                + "PasswordHash = ?, "
                + "Telefono = ?, "
                + "Direccion = ?, "
                + "Estado = ? "
                + "WHERE IdUsuario = ?";
        try (Connection con = ConexionSQLServer.getConexion();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombres());
            pstmt.setString(2, usuario.getApellidos());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getPasswordHash());
            pstmt.setInt(5, usuario.getTelefono());
            pstmt.setString(6, usuario.getDireccion());
            pstmt.setBoolean(7, usuario.isEstado());
            pstmt.setInt(8, usuario.getIdUsuario());
            int filaAfectada = pstmt.executeUpdate();
            if (filaAfectada > 0) {
                System.out.println("Usuario Actualizado exitoso con  ID-> " + usuario.getIdUsuario());
            } else {
                System.out.println("No se puede encontar al Usuario");
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar Usuario: " + e.getMessage());
        }
    }

    public void desabilitarUsuario(int idUsuario) {
        String sql = "UPDATE Usuario SET Estado = ? WHERE IdUsuario = ?";
        try (Connection con = ConexionSQLServer.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setBoolean(1, false);
            pstmt.setInt(2, idUsuario);

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Se ha deshabilitado con exito ID -> " + idUsuario);
            } else {
                System.out.println("No se ha encontrado al usuario con ID -> " + idUsuario);
            }
        } catch (SQLException e) {
            System.out.println("Error al deshabilitar el usuario : " + e.getMessage());
        }
    }
}
