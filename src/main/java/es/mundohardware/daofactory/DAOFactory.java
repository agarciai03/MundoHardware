package es.mundohardware.daofactory;

import es.mundohardware.dao.ICategoriaDAO;
import es.mundohardware.dao.IPedidoDAO;
import es.mundohardware.dao.IProductoDAO;
import es.mundohardware.dao.IUsuarioDAO;
import es.mundohardware.dao.MySQLDAOFactory;

/**
 * Fábrica abstracta (Abstract Factory) que define y provee los métodos para la
 * creación de DAOs independientemente de la base de datos utilizada.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public abstract class DAOFactory {

    /**
     * Constante que define el motor MySQL
     */
    public static final int MYSQL = 1;

    /**
     * Obtiene el DAO para manipular Productos.
     *
     * @return El objeto de acceso a datos IProductoDAO
     */
    public abstract IProductoDAO getProductoDAO();

    /**
     * Obtiene el DAO para manipular Categorías.
     *
     * @return El objeto de acceso a datos ICategoriaDAO
     */
    public abstract ICategoriaDAO getCategoriaDAO();

    /**
     * Obtiene el DAO para manipular Usuarios.
     *
     * @return El objeto de acceso a datos IUsuarioDAO
     */
    public abstract IUsuarioDAO getUsuarioDAO();

    /**
     * Obtiene el DAO para manipular Pedidos.
     *
     * @return El objeto de acceso a datos IPedidoDAO
     */
    public abstract IPedidoDAO getPedidoDAO();

    /**
     * Obtiene la fábrica concreta específica según el motor de base de datos
     * requerido.
     *
     * @param tipo Identificador del tipo de base de datos
     * @return La fábrica concreta, o null si el tipo no está soportado.
     */
    public static DAOFactory getDAOFactory(int tipo) {
        switch (tipo) {
            case MYSQL:
                return new MySQLDAOFactory();
            default:
                return null;
        }
    }
}
