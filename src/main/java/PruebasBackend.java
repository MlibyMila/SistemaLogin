import Conexion.ConexionSQLServer;
import Dao.UsuarioDao;
import Modelo.Libro;
import Modelo.Prestamo;
import Modelo.Usuario;
import Service.LibroService;
import Service.PrestamoService;
import Service.UsuarioService;
import Service.impl.LibroServiceImpl;
import Service.impl.PrestamoServiceImpl;
import Service.impl.UsuarioServiceImpl;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class PruebasBackend {

    public static void main(String[] args) {

        System.out.println("--- 1. Prueba de Conexión BD ---");
        try (Connection con = ConexionSQLServer.getConexion()) {
            if (con == null) {
                System.out.println("FALLO LA CONEXIÓN. Revisa la BD y el string de conexión.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error al intentar conectar: " + e.getMessage());
            return;
        }
        System.out.println("---------------------------------");

        UsuarioService usuarioService = new UsuarioServiceImpl();
        LibroService libroService = new LibroServiceImpl();
        PrestamoService prestamoService = new PrestamoServiceImpl();

        System.out.println("--- 2. Prueba de Registro de Usuario ---");
        String nuevoEmail = "milagros@gmail.com";
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombres("Milagros");
        nuevoUsuario.setApellidos("Tafur");
        nuevoUsuario.setEmail(nuevoEmail);
        nuevoUsuario.setPasswordHash("test123");
        nuevoUsuario.setTelefono(987654321);
        nuevoUsuario.setDireccion("Av. Prueba 123");
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());
        nuevoUsuario.setEstado(true);

        try {
            if (new UsuarioDao().buscarUsuarioPorEmail(nuevoEmail) != null) {
                System.out.println("Error: El email " + nuevoEmail + " ya existe.");
            } else {
                usuarioService.registrarUsuario(nuevoUsuario);
                System.out.println("Usuario registrado exitosamente: " + nuevoEmail);
                System.out.println("Verifica la tabla [Usuario] en tu BD.");
            }
        } catch (Exception e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
        System.out.println("---------------------------------");

        System.out.println("--- 3. Prueba de Login (Validación) ---");

        String emailPrueba = "test@test.com";
        String passPrueba = "passDePrueba123";

        Usuario usuarioValidado = usuarioService.validarLogin(emailPrueba, passPrueba);

        if (usuarioValidado != null) {
            System.out.println("Login exitoso para: " + usuarioValidado.getNombres());
        } else {
            System.out.println("Login fallido para: " + emailPrueba);
        }
        System.out.println("---------------------------------");

        System.out.println("--- 4. Prueba de Búsqueda de Libros ---");
        String criterioBusqueda = "Java";

        List<Libro> librosEncontrados = libroService.buscarLibrosPorCriterio(criterioBusqueda);

        if (!librosEncontrados.isEmpty()) {
            System.out
                    .println("Se encontraron " + librosEncontrados.size() + " libros para '" + criterioBusqueda + "':");
            for (Libro libro : librosEncontrados) {
                System.out.println("  - ID: " + libro.getIdLibro() + ", Título: " + libro.getTitulo());
            }
        } else {
            System.out.println("No se encontraron libros para '" + criterioBusqueda + "'.");
        }
        System.out.println("---------------------------------");

        System.out.println("--- 5. Prueba de Préstamos Activos ---");
        int idUsuarioPrueba = 1;

        List<Prestamo> prestamosActivos = prestamoService.obtenerPrestamosActivos(idUsuarioPrueba);

        if (!prestamosActivos.isEmpty()) {
            System.out.println("Se encontraron " + prestamosActivos.size() + " préstamos activos para el usuario ID "
                    + idUsuarioPrueba);
            for (Prestamo p : prestamosActivos) {
                System.out.println(
                        "  - ID Préstamo: " + p.getIdPrestamo() + ", Vence: " + p.getFechaDevolucionEsperada());
            }
        } else {
            System.out.println("No se encontraron préstamos activos para el usuario ID " + idUsuarioPrueba);
        }
        System.out.println("---------------------------------");

        System.out.println("--- 5. Prueba de desactivación de usuario---");

        usuarioService.desabilitarUsuario(idUsuarioPrueba);
        System.out.println("Usuario desactivado exitosamente con ID -> " + idUsuarioPrueba);

        System.out.println("---------------------------------");

    }
}