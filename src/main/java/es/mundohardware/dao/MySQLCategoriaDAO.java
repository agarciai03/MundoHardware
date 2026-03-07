package es.mundohardware.dao;

import es.mundohardware.beans.Categorias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz ICategoriaDAO para acceso a datos en MySQL.
 * Gestiona las consultas de categorías usando JDBC.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public class MySQLCategoriaDAO implements ICategoriaDAO {

    @Override
    public List<Categorias> getCategorias() {
        List<Categorias> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias";
        Connection con = ConnectionFactory.getConnection();

        if (con == null) {
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Categorias c = new Categorias();
                c.setIdCategoria((byte) rs.getInt("idcategoria"));
                c.setNombre(rs.getString("nombre"));
                c.setImagen(rs.getString("imagen"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }
}
