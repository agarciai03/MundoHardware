package es.mundohardware.beans;

import java.io.Serializable;

/**
 * Clase que representa un Producto disponible en el catálogo de la tienda.
 * Almacena los atributos necesarios para su venta y visualización.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public class Productos implements Serializable {

    private short idProducto;
    private short idCategoria;
    private String nombre;
    private String descripcion;
    private double precio;
    private String marca;
    private String imagen;

    /**
     * Constructor por defecto de Productos.
     */
    public Productos() {
    }

    /**
     * Obtiene el identificador del producto.
     *
     * @return El ID del producto
     */
    public short getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el identificador del producto.
     *
     * @param idProducto El ID a asignar
     */
    public void setIdProducto(short idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene el identificador de la categoría a la que pertenece el producto.
     *
     * @return El ID de la categoría
     */
    public short getIdCategoria() {
        return idCategoria;
    }

    /**
     * Establece el identificador de la categoría.
     *
     * @param idCategoria El ID de categoría a asignar
     */
    public void setIdCategoria(short idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return El nombre del producto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre El nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción detallada del producto.
     *
     * @return La descripción del producto
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción detallada del producto.
     *
     * @param descripcion La descripción a asignar
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el precio de venta del producto.
     *
     * @return El precio del producto
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio de venta del producto.
     *
     * @param precio El precio a asignar
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la marca o fabricante del producto.
     *
     * @return La marca del producto
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Establece la marca o fabricante del producto.
     *
     * @param marca La marca a asignar
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Obtiene el nombre del archivo de imagen asociado al producto.
     *
     * @return El nombre del archivo de imagen
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece el nombre del archivo de imagen del producto.
     *
     * @param imagen El nombre de archivo a asignar
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
