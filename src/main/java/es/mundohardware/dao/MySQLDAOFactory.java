package es.mundohardware.dao;

import es.mundohardware.daofactory.DAOFactory;

/**
 * Fábrica concreta (Concrete Factory) que instancia los DAOs específicos para
 * trabajar con una base de datos MySQL.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public class MySQLDAOFactory extends DAOFactory {

    @Override
    public IProductoDAO getProductoDAO() {
        return new MySQLProductoDAO();
    }

    @Override
    public ICategoriaDAO getCategoriaDAO() {
        return new MySQLCategoriaDAO();
    }

    @Override
    public IUsuarioDAO getUsuarioDAO() {
        return new MySQLUsuarioDAO();
    }

    @Override
    public IPedidoDAO getPedidoDAO() {
        return new MySQLPedidoDAO();
    }
}
