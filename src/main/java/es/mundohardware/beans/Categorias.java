package es.mundohardware.beans;

import java.io.Serializable;

/**
 * Clase que representa la entidad Categoría de la base de datos. Contiene los
 * datos necesarios para organizar los productos.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public class Categorias implements Serializable {

    private short idCategoria;
    private String nombre;
    private String imagen;

    /**
     * Constructor por defecto.
     */
    public Categorias() {
    }

    /**
     * Obtiene el ID de la categoría.
     *
     * @return idCategoria El identificador único
     */
    public short getIdCategoria() {
        return idCategoria;
    }

    /**
     * Establece el ID de la categoría.
     *
     * @param idCategoria El identificador a asignar
     */
    public void setIdCategoria(short idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Obtiene el nombre de la categoría.
     *
     * @return nombre El nombre de la categoría
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la categoría.
     *
     * @param nombre El nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la ruta o nombre de la imagen de la categoría.
     *
     * @return imagen Nombre del archivo de imagen
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen de la categoría.
     *
     * @param imagen Nombre del archivo de imagen a asignar
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
