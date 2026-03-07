package es.mundohardware.dao;

import es.mundohardware.beans.LineaPedidos;
import es.mundohardware.beans.Pedidos;
import es.mundohardware.beans.Productos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz IPedidoDAO para acceso a datos en MySQL.
 * Gestiona de forma transaccional (commit/rollback) los carritos de la compra y
 * la finalización de los pedidos, trabajando simultáneamente con las tablas
 * pedidos y lineaspedidos.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public class MySQLPedidoDAO implements IPedidoDAO {

    @Override
    public List<Pedidos> getPedidosFinalizados(int idUsuario) {
        List<Pedidos> lista = new ArrayList<>();
        String sql = "SELECT idpedido, fecha, estado, importe, iva "
                + "FROM pedidos WHERE idusuario = ? AND estado = 'f' "
                + "ORDER BY fecha DESC";
        Connection con = ConnectionFactory.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedidos p = new Pedidos();
                    p.setIdPedido(rs.getInt("idpedido"));
                    p.setFecha(rs.getDate("fecha"));
                    p.setEstado(rs.getString("estado"));
                    p.setImporte(rs.getDouble("importe"));
                    p.setIva(rs.getDouble("iva"));
                    p.setIdUsuario(idUsuario);
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
    public Pedidos getCarritoBaseDatos(int idUsuario) {
        Pedidos carrito = null;
        String sql = "SELECT * FROM pedidos WHERE idusuario = ? AND estado = 'c'";
        Connection con = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                carrito = new Pedidos();
                carrito.setIdPedido(rs.getInt("idpedido"));
                carrito.setEstado("c");
                carrito.setIdUsuario(idUsuario);
                carrito.setImporte(rs.getDouble("importe"));
                carrito.setIva(rs.getDouble("iva"));

                String sqlLineas = "SELECT lp.*, p.* FROM lineaspedidos lp INNER JOIN productos p ON lp.idproducto = p.idproducto WHERE lp.idpedido = ?";
                PreparedStatement psLineas = con.prepareStatement(sqlLineas);
                psLineas.setInt(1, carrito.getIdPedido());
                ResultSet rsLineas = psLineas.executeQuery();
                while (rsLineas.next()) {
                    LineaPedidos linea = new LineaPedidos();
                    linea.setCantidad(rsLineas.getInt("cantidad"));
                    Productos prod = new Productos();
                    prod.setIdProducto((short) rsLineas.getInt("idproducto"));
                    prod.setNombre(rsLineas.getString("nombre"));
                    prod.setPrecio(rsLineas.getDouble("precio"));
                    prod.setImagen(rsLineas.getString("imagen"));
                    linea.setProducto(prod);
                    carrito.getLineas().add(linea);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return carrito;
    }

    @Override
    public void guardarCarritoBaseDatos(Pedidos carrito) {
        Connection con = ConnectionFactory.getConnection();
        try {
            con.setAutoCommit(false);
            PreparedStatement psDel = con.prepareStatement("DELETE FROM pedidos WHERE idusuario = ? AND estado = 'c'");
            psDel.setInt(1, carrito.getIdUsuario());
            psDel.executeUpdate();

            // Solo insertamos si el carrito tiene líneas, para no crear carritos vacíos
            if(!carrito.getLineas().isEmpty()) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO pedidos (fecha, estado, idusuario, importe, iva) VALUES (NOW(), 'c', ?, ?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, carrito.getIdUsuario());
                ps.setDouble(2, carrito.getImporte());
                ps.setDouble(3, carrito.getIva());
                ps.executeUpdate();
    
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int idPedidoGenerado = rs.getInt(1);
                    PreparedStatement psL = con.prepareStatement("INSERT INTO lineaspedidos (idpedido, idproducto, cantidad) VALUES (?, ?, ?)");
                    for (LineaPedidos lp : carrito.getLineas()) {
                        psL.setInt(1, idPedidoGenerado);
                        psL.setInt(2, lp.getProducto().getIdProducto());
                        psL.setInt(3, lp.getCantidad());
                        psL.executeUpdate();
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
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException ex) {
            }
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void finalizarPedido(Pedidos pedido) {
        Connection con = ConnectionFactory.getConnection();
        try {
            con.setAutoCommit(false);
            String sqlUpdate = "UPDATE pedidos SET estado = 'f', importe = ?, iva = ?, fecha = NOW() WHERE idusuario = ? AND estado = 'c'";
            PreparedStatement psUp = con.prepareStatement(sqlUpdate);
            psUp.setDouble(1, pedido.getImporte());
            psUp.setDouble(2, pedido.getIva());
            psUp.setInt(3, pedido.getIdUsuario());

            int filas = psUp.executeUpdate();
            if (filas == 0) {
                PreparedStatement psIn = con.prepareStatement("INSERT INTO pedidos (fecha, estado, idusuario, importe, iva) VALUES (NOW(), 'f', ?, ?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS);
                psIn.setInt(1, pedido.getIdUsuario());
                psIn.setDouble(2, pedido.getImporte());
                psIn.setDouble(3, pedido.getIva());
                psIn.executeUpdate();

                ResultSet rs = psIn.getGeneratedKeys();
                if (rs.next()) {
                    int idPed = rs.getInt(1);
                    PreparedStatement psL = con.prepareStatement("INSERT INTO lineaspedidos (idpedido, idproducto, cantidad) VALUES (?, ?, ?)");
                    for (LineaPedidos lp : pedido.getLineas()) {
                        psL.setInt(1, idPed);
                        psL.setInt(2, lp.getProducto().getIdProducto());
                        psL.setInt(3, lp.getCantidad());
                        psL.executeUpdate();
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
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException ex) {
            }
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public Pedidos getPedidoById(int idPedido, int idUsuario) {
        Pedidos pedido = null;
        String sql = "SELECT idpedido, fecha, estado, importe, iva FROM pedidos WHERE idpedido = ? AND idusuario = ? AND estado = 'f'";
        Connection con = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idPedido);
            ps.setInt(2, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pedido = new Pedidos();
                pedido.setIdPedido(rs.getInt("idpedido"));
                pedido.setFecha(rs.getDate("fecha"));
                pedido.setEstado(rs.getString("estado"));
                pedido.setImporte(rs.getDouble("importe"));
                pedido.setIva(rs.getDouble("iva"));
                pedido.setIdUsuario(idUsuario);

                String sqlLineas = "SELECT lp.*, p.* FROM lineaspedidos lp INNER JOIN productos p ON lp.idproducto = p.idproducto WHERE lp.idpedido = ?";
                PreparedStatement psLineas = con.prepareStatement(sqlLineas);
                psLineas.setInt(1, pedido.getIdPedido());
                ResultSet rsLineas = psLineas.executeQuery();
                while (rsLineas.next()) {
                    LineaPedidos linea = new LineaPedidos();
                    linea.setCantidad(rsLineas.getInt("cantidad"));
                    Productos prod = new Productos();
                    prod.setIdProducto((short) rsLineas.getInt("idproducto"));
                    prod.setNombre(rsLineas.getString("nombre"));
                    prod.setPrecio(rsLineas.getDouble("precio"));
                    prod.setImagen(rsLineas.getString("imagen"));
                    prod.setMarca(rsLineas.getString("marca"));
                    linea.setProducto(prod);
                    pedido.getLineas().add(linea);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return pedido;
    }
}