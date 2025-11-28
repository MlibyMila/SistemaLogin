package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLServer {

    private static final String URL = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=BD_BIBLIOTECA;"
            + "user=sa;password=root;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    public static Connection getConexion() throws SQLException {
        Connection con = DriverManager.getConnection(URL);
        System.out.println("¡Conexión exitosa a SQL Server!");
        return con;
    }
}
