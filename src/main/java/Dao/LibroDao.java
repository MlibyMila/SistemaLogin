package Dao;

import Conexion.ConexionSQLServer;
import Modelo.Autor;
import Modelo.Libro;
import Modelo.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibroDao {

    public List<Libro> obtenerTodosLosLibros() {
        List<Libro> libros = new ArrayList<>();
        // Traemos datos del libro y el nombre de la categoría
        String sql = "SELECT l.*, c.Nombre as NombreCategoria FROM Libros l "
                + "LEFT JOIN Categorias c ON l.IdCategoria = c.IdCategoria "
                + "WHERE l.Estado = 1";

        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                libros.add(mapearLibro(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los libros: " + e.getMessage());
        }
        return libros;
    }

    public Libro buscarLibroPorISBN(String isbn) {
        Libro libro = null;
        String sql = "SELECT l.*, c.Nombre as NombreCategoria FROM Libros l "
                + "LEFT JOIN Categorias c ON l.IdCategoria = c.IdCategoria "
                + "WHERE l.ISBN = ? AND l.Estado = 1";

        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    libro = mapearLibro(rs);
                    // IMPORTANTE: Cargar autores para que aparezcan al editar
                    libro.setAutores(obtenerAutoresPorLibro(libro.getIdLibro()));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar libro por ISBN: " + e.getMessage());
        }
        return libro;
    }

    private Libro mapearLibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setIdLibro(rs.getInt("IdLibro"));
        libro.setTitulo(rs.getString("Titulo"));
        libro.setIsbn(rs.getString("ISBN"));
        libro.setFechaPublicacion(rs.getObject("FechaPublicacion", java.time.LocalDate.class));
        libro.setIdioma(rs.getString("Idioma"));
        libro.setNumeroPaginas(rs.getInt("NumeroPaginas"));
        libro.setDescripcion(rs.getString("Descripcion"));

        Categoria cat = new Categoria();
        cat.setIdCategoria(rs.getInt("IdCategoria"));
        cat.setNombre(rs.getString("NombreCategoria"));
        libro.setCategoria(cat);
        return libro;
    }

    private List<Autor> obtenerAutoresPorLibro(int idLibro) {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT a.* FROM Autores a JOIN LibroAutor la ON a.IdAutor = la.IdAutor WHERE la.IdLibro = ?";
        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, idLibro);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Autor a = new Autor();
                    a.setIdAutor(rs.getInt("IdAutor"));
                    a.setNombres(rs.getString("Nombres"));
                    a.setApellidos(rs.getString("Apellidos"));
                    autores.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autores;
    }

    public List<Libro> buscarLibrosPorCriterio(String criterio) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT l.*, c.Nombre as NombreCategoria FROM Libros l "
                + "LEFT JOIN Categorias c ON l.IdCategoria = c.IdCategoria "
                + "WHERE (l.Titulo LIKE ? OR l.ISBN LIKE ?) AND l.Estado = 1";

        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            String criterioBusqueda = "%" + criterio + "%";
            pstmt.setString(1, criterioBusqueda);
            pstmt.setString(2, criterioBusqueda);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapearLibro(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar libros: " + e.getMessage());
        }
        return libros;
    }

     public void registrarLibro(Libro libro) {
        String sql = "INSERT INTO Libros (Titulo, ISBN, IdCategoria, FechaPublicacion, Idioma, NumeroPaginas, Descripcion, Estado) VALUES (?,?,?,?,?,?, ?, 1)";
        String sqlRelacion = "INSERT INTO LibroAutor (IdLibro,IdAutor) VALUES (?,?)";
        Connection con = null;
        try {
            con = ConexionSQLServer.getConexion();
            con.setAutoCommit(false); 
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getIsbn());
            if (libro.getCategoria() != null) pstmt.setInt(3, libro.getCategoria().getIdCategoria());
            else pstmt.setNull(3, java.sql.Types.INTEGER);
            pstmt.setObject(4, libro.getFechaPublicacion());
            pstmt.setString(5, libro.getIdioma());
            pstmt.setInt(6, libro.getNumeroPaginas());
            pstmt.setString(7, libro.getDescripcion());
            
            if (pstmt.executeUpdate() == 0) throw new SQLException("Fallo registro libro");
            
            int idLibroGenerado = -1;
            try (ResultSet generateKeys = pstmt.getGeneratedKeys()) {
                if (generateKeys.next()) idLibroGenerado = generateKeys.getInt(1);
            }
            
            if (libro.getAutores() != null && !libro.getAutores().isEmpty()) {
                PreparedStatement pstmtAutor = con.prepareStatement(sqlRelacion);
                for (Autor autor : libro.getAutores()) {
                    pstmtAutor.setInt(1, idLibroGenerado);
                    pstmtAutor.setInt(2, autor.getIdAutor());
                    pstmtAutor.addBatch(); 
                }
                pstmtAutor.executeBatch(); 
            }
            con.commit(); 
            System.out.println("Libro registrado ID: " + idLibroGenerado);
        } catch (SQLException e) {
            try { if(con!=null) con.rollback(); } catch(SQLException ex){}
            System.out.println("Error registro: " + e.getMessage());
        } finally {
            try { if(con!=null) {con.setAutoCommit(true); con.close();} } catch(SQLException ex){}
        }
    }
    

    public void desavilitarLibro(String isbn) {
        String sql = "UPDATE Libros SET Estado = ? WHERE ISBN = ?";
        try (Connection con = ConexionSQLServer.getConexion(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setBoolean(1, false);
            pstmt.setString(2, isbn);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Se ha desavilitado con exito el libro con ISBN: " + isbn);
            } else {
                System.out.println("No se ha encontrado el Libro con ISBN: " + isbn);
            }

        } catch (SQLException e) {
            System.out.println("Error al desavilitar Libro: " + e.getMessage());
        }

    }

    public void editarLibro(Libro libro) {
        String sql = "UPDATE Libros SET Titulo=?, IdCategoria=?, FechaPublicacion=?, Idioma=?, NumeroPaginas=?, Descripcion=? WHERE ISBN=?";
        String sqlDel = "DELETE FROM LibroAutor WHERE IdLibro=?";
        String sqlIns = "INSERT INTO LibroAutor (IdLibro, IdAutor) VALUES (?,?)";

        Connection con = null;
        try {
            con = ConexionSQLServer.getConexion();
            con.setAutoCommit(false);

            // 1. Actualizar Libro
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, libro.getTitulo());
            if (libro.getCategoria() != null) {
                pstmt.setInt(2, libro.getCategoria().getIdCategoria());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            pstmt.setObject(3, libro.getFechaPublicacion());
            pstmt.setString(4, libro.getIdioma());
            pstmt.setInt(5, libro.getNumeroPaginas());
            pstmt.setString(6, libro.getDescripcion());
            pstmt.setString(7, libro.getIsbn());
            pstmt.executeUpdate();

            // 2. Actualizar Autores (Solo si tenemos ID del libro)
            // Si el objeto libro no tiene ID, búscalo primero por ISBN. Asumimos que viene cargado.
            if (libro.getIdLibro() != null && libro.getIdLibro() > 0) {
                PreparedStatement psDel = con.prepareStatement(sqlDel);
                psDel.setInt(1, libro.getIdLibro());
                psDel.executeUpdate();

                if (libro.getAutores() != null && !libro.getAutores().isEmpty()) {
                    PreparedStatement psIns = con.prepareStatement(sqlIns);
                    for (Autor a : libro.getAutores()) {
                        psIns.setInt(1, libro.getIdLibro());
                        psIns.setInt(2, a.getIdAutor());
                        psIns.executeUpdate();
                    }
                }
            }
            con.commit();
        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
            }
            System.out.println("Error al editar: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException ex) {
            }
        }
    }
}
