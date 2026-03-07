package es.mundohardware.dao;

import es.mundohardware.beans.Productos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Implementación de la interfaz IProductoDAO para acceso a datos en MySQL.
 * Facilita las consultas de catálogo, búsqueda por nombres y filtros complejos
 * por precio, marca y categorías con consultas dinámicas.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public class MySQLProductoDAO implements IProductoDAO {

    @Override
    public List<Productos> getProductos() {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        Connection con = ConnectionFactory.getConnection();

        if (con == null) {
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Productos p = new Productos();
                p.setIdProducto((short) rs.getInt("idproducto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setMarca(rs.getString("marca"));
                p.setImagen(rs.getString("imagen"));
                p.setIdCategoria((byte) rs.getInt("idcategoria"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    @Override
    public Productos getProductoById(int id) {
        Productos p = null;
        String sql = "SELECT * FROM productos WHERE idproducto = ?";
        Connection con = ConnectionFactory.getConnection();

        if (con == null) {
            return null;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Productos();
                    p.setIdProducto((short) rs.getInt("idproducto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setMarca(rs.getString("marca"));
                    p.setImagen(rs.getString("imagen"));
                    p.setIdCategoria((byte) rs.getInt("idcategoria"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return p;
    }

    @Override
    public List<Productos> buscarProductos(String query) {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ? OR descripcion LIKE ?";
        Connection con = ConnectionFactory.getConnection();

        if (con == null) {
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            String busqueda = "%" + query + "%";
            ps.setString(1, busqueda);
            ps.setString(2, busqueda);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Productos p = new Productos();
                    p.setIdProducto((short) rs.getInt("idproducto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setMarca(rs.getString("marca"));
                    p.setImagen(rs.getString("imagen"));
                    p.setIdCategoria((byte) rs.getInt("idcategoria"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    @Override
    public List<Productos> filtrarPorPrecio(double min, double max) {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE precio BETWEEN ? AND ? ORDER BY precio ASC";
        Connection con = ConnectionFactory.getConnection();

        if (con == null) {
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Productos p = new Productos();
                    p.setIdProducto((short) rs.getInt("idproducto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setMarca(rs.getString("marca"));
                    p.setImagen(rs.getString("imagen"));
                    p.setIdCategoria((byte) rs.getInt("idcategoria"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    @Override
    public List<String> getMarcas() {
        List<String> marcas = new ArrayList<>();
        String sql = "SELECT DISTINCT marca FROM productos ORDER BY marca";
        Connection con = ConnectionFactory.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                marcas.add(rs.getString("marca"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return marcas;
    }

    @Override
    public double getPrecioMaximo() {
        double precio = 1880; // Valor por defecto
        String sql = "SELECT MAX(precio) FROM productos";
        Connection con = ConnectionFactory.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                precio = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return precio;
    }

    @Override
    public double getPrecioMinimo() {
        double precio = 0;
        String sql = "SELECT MIN(precio) FROM productos";
        Connection con = ConnectionFactory.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                precio = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return precio;
    }

    @Override
    public List<Productos> getProductosAleatorios(int cantidad) {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY RAND() LIMIT ?";
        Connection con = ConnectionFactory.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Productos p = new Productos();
                    p.setIdProducto((short) rs.getInt("idproducto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setMarca(rs.getString("marca"));
                    p.setImagen(rs.getString("imagen"));
                    p.setIdCategoria((byte) rs.getInt("idcategoria"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    @Override
    public List<Productos> getProductosByMarcas(String[] marcas) {
        List<Productos> lista = new ArrayList<>();
        if (marcas == null || marcas.length == 0) {
            return lista;
        }

        // según las marcas que haya marcado el usuario
        StringBuilder interrogantes = new StringBuilder();
        for (int i = 0; i < marcas.length; i++) {
            interrogantes.append("?");
            if (i < marcas.length - 1) {
                interrogantes.append(",");
            }
        }

        // La consulta busca en cualquiera de esas marcas
        String sql = "SELECT * FROM productos WHERE marca IN (" + interrogantes.toString() + ")";
        Connection con = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            // rellenamos con nombres de las marcas
            for (int i = 0; i < marcas.length; i++) {
                ps.setString(i + 1, marcas[i]);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Productos p = new Productos();
                p.setIdProducto((short) rs.getInt("idproducto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setMarca(rs.getString("marca"));
                p.setImagen(rs.getString("imagen"));
                p.setIdCategoria((byte) rs.getInt("idcategoria"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }

        return lista;
    }

    @Override
    public List<es.mundohardware.beans.Productos> getProductosByCategorias(String[] idCategorias) {
        List<es.mundohardware.beans.Productos> lista = new java.util.ArrayList<>();
        if (idCategorias == null || idCategorias.length == 0) {
            return lista;
        }

        StringBuilder interrogantes = new StringBuilder();
        for (int i = 0; i < idCategorias.length; i++) {
            interrogantes.append("?");
            if (i < idCategorias.length - 1) {
                interrogantes.append(",");
            }
        }

        String sql = "SELECT * FROM productos WHERE idcategoria IN (" + interrogantes.toString() + ")";
        java.sql.Connection con = ConnectionFactory.getConnection();

        try {
            java.sql.PreparedStatement ps = con.prepareStatement(sql);

            for (int i = 0; i < idCategorias.length; i++) {
                ps.setInt(i + 1, Integer.parseInt(idCategorias[i]));
            }

            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Creamos el producto a mano con los campos básicos 
                es.mundohardware.beans.Productos p = new es.mundohardware.beans.Productos();
                p.setIdProducto((short) rs.getInt("idproducto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setImagen(rs.getString("imagen"));
                p.setMarca(rs.getString("marca"));

                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }

        return lista;
    }
}
