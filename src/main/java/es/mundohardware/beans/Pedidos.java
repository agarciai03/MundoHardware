package es.mundohardware.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa un Pedido (o carrito de la compra) de un usuario.
 * Almacena los datos totales de facturación y una lista con las líneas de
 * detalle.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public class Pedidos implements Serializable {

    private int idPedido;
    private Date fecha;
    private String estado;
    private int idUsuario;
    private double importe;
    private double iva;
    private List<LineaPedidos> lineas;

    /**
     * Constructor por defecto. Inicializa la lista de líneas de pedido.
     */
    public Pedidos() {
        lineas = new ArrayList<>();
    }

    /**
     * Obtiene el ID del pedido.
     *
     * @return El identificador único del pedido
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     * Establece el ID del pedido.
     *
     * @param idPedido El identificador a asignar
     */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * Obtiene la fecha en la que se realizó o actualizó el pedido.
     *
     * @return La fecha del pedido
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha del pedido.
     *
     * @param fecha La fecha a asignar
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el estado actual del pedido ('c' para carrito, 'f' para
     * finalizado).
     *
     * @return El estado del pedido
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del pedido.
     *
     * @param estado El estado a asignar ('c' o 'f')
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el ID del usuario al que pertenece este pedido.
     *
     * @return El ID del usuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el ID del usuario propietario del pedido.
     *
     * @param idUsuario El ID de usuario a asignar
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene la base imponible del pedido (importe sin IVA).
     *
     * @return El importe de la base imponible
     */
    public double getImporte() {
        return importe;
    }

    /**
     * Establece la base imponible del pedido.
     *
     * @param importe El importe base a asignar
     */
    public void setImporte(double importe) {
        this.importe = importe;
    }

    /**
     * Obtiene la cantidad de IVA aplicada al pedido.
     *
     * @return El importe correspondiente al IVA
     */
    public double getIva() {
        return iva;
    }

    /**
     * Establece la cantidad de IVA del pedido.
     *
     * @param iva El importe de IVA a asignar
     */
    public void setIva(double iva) {
        this.iva = iva;
    }

    /**
     * Obtiene la lista de líneas que componen el pedido.
     *
     * @return La lista de objetos LineaPedidos
     */
    public List<LineaPedidos> getLineas() {
        return lineas;
    }

    /**
     * Establece la lista de líneas del pedido.
     *
     * @param lineas La lista de líneas a asignar
     */
    public void setLineas(List<LineaPedidos> lineas) {
        this.lineas = lineas;
    }
}
