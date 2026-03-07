package es.mundohardware.beans;

import java.io.Serializable;

/**
 * Clase que representa una línea de detalle dentro de un Pedido. Relaciona un
 * producto específico con la cantidad solicitada en el carrito o compra.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public class LineaPedidos implements Serializable {

    private int idLineaPedido;
    private int idPedido;
    private int idProducto;
    private int cantidad;
    private double precioVenta;
    private Productos producto;

    /**
     * Constructor por defecto de LineaPedidos.
     */
    public LineaPedidos() {
    }

    /**
     * Obtiene la linea del pedido.
     *
     * @return El ID de la linea de pedido
     */
    public int getIdLineaPedido() {
        return idLineaPedido;
    }

    /**
     * Establece la linea de pedido.
     *
     * @param idLineaPedido El ID de linea de pedido a asignar
     */
    public void setIdLineaPedido(int idLineaPedido) {
        this.idLineaPedido = idLineaPedido;
    }

    /**
     * Obtiene el identificador del pediddo.
     *
     * @return El ID del pedido
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     * Establece el identificador del pedido.
     *
     * @param idPedido El ID del pedido
     */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * Obtiene el identificador del producto.
     *
     * @return El ID del producto
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el identificador del producto.
     *
     * @param idProducto El ID del producto a asignar
     */
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene la cantidad de unidades solicitadas de este producto.
     *
     * @return La cantidad de unidades
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de unidades solicitadas.
     *
     * @param cantidad La cantidad a asignar
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio de venta de un producto.
     *
     * @return El precio de venta
     */
    public double getPrecioVenta() {
        return precioVenta;
    }

    /**
     * Establece el precio de venta de un producto
     *
     * @param precioVenta El precio de venta
     */
    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    /**
     * Obtiene el producto asociado a esta línea de pedido.
     *
     * @return El objeto Productos correspondiente
     */
    public Productos getProducto() {
        return producto;
    }

    /**
     * Establece el producto para esta línea de pedido.
     *
     * @param producto El objeto Productos a asignar
     */
    public void setProducto(Productos producto) {
        this.producto = producto;
    }
}
