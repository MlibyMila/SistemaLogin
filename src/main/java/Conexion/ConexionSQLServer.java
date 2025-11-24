package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLServer {

    private static final String URL = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=BD_BIBLIOTECA;"
            + "user=sa;password=root;encrypt=true;trustServerCertificate=true;";

    public static Connection getConexion() {
        Connection con = null;
        try {

            con = DriverManager.getConnection(URL);
            System.out.println("¡Conexión exitosa a SQL Server!");

        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos:");
            e.printStackTrace();
        }
        return con;
    }
}
