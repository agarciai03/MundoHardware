package es.mundohardware.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Fábrica de conexiones a la base de datos. Obtiene las conexiones a través de
 * un Pool configurado mediante JNDI en el servidor (Tomcat).
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public class ConnectionFactory {

    /**
     * Obtiene una conexión activa desde el Pool de conexiones JNDI.
     *
     * @return Connection Objeto de conexión a la base de datos MySQL, o null si
     * falla.
     */
    public static Connection getConnection() {
        Connection con = null;
        try {
            // buscamos el pool de conexiones en context.xml
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");

            DataSource ds = (DataSource) envContext.lookup("jdbc/mundohardware");

            // Pool connection
            con = ds.getConnection();
        } catch (Exception e) {
            System.err.println("Error obteniendo la conexión del Pool: " + e.getMessage());
            e.printStackTrace();
        }
        return con;
    }

    /**
     * Cierra la conexión de forma segura, devolviéndola al Pool.
     *
     * @param con La conexión a cerrar.
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close(); // close() devuelve la conexión al pool 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
