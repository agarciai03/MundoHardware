package es.mundohardware.dao;

import es.mundohardware.beans.Pedidos;
import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a datos relacionadas con los
 * Pedidos de los usuarios y su gestión en BBDD.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public interface IPedidoDAO {

    /**
     * Obtiene todos los pedidos con estado finalizado ('f') de un usuario
     * específico.
     *
     * @param idUsuario El ID del usuario
     * @return Una lista de pedidos finalizados
     */
    public List<Pedidos> getPedidosFinalizados(int idUsuario);

    /**
     * Recupera el pedido en estado carrito ('c') guardado en la base de datos
     * para un usuario.
     *
     * @param idUsuario El ID del usuario
     * @return El objeto Pedido correspondiente al carrito, o null si no tiene.
     */
    public Pedidos getCarritoBaseDatos(int idUsuario);

    /**
     * Guarda el estado actual del carrito (pedido en estado 'c') en la base de
     * datos. Elimina el carrito anterior si existiera y guarda las líneas
     * actualizadas.
     *
     * @param carrito El objeto Pedido que representa el carrito actual
     */
    public void guardarCarritoBaseDatos(Pedidos carrito);

    /**
     * Procesa la compra de un pedido, cambiando su estado a finalizado ('f') y
     * actualizando los totales.
     *
     * @param pedido El pedido a finalizar
     */
    public void finalizarPedido(Pedidos pedido);

    /**
     * Recupera un pedido finalizado específico mediante su ID, validando que
     * pertenezca al usuario especificado.
     *
     * @param idPedido El ID del pedido a buscar
     * @param idUsuario El ID del usuario propietario
     * @return El objeto Pedido, o null si no se encuentra o no pertenece al
     * usuario.
     */
    public Pedidos getPedidoById(int idPedido, int idUsuario);
}
