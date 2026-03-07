package es.mundohardware.dao;

import es.mundohardware.beans.Categorias;
import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a datos permitidas para la
 * entidad Categorias.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public interface ICategoriaDAO {

    /**
     * Obtiene una lista completa con todas las categorías disponibles en la
     * base de datos.
     *
     * @return Una lista de objetos Categorias
     */
    public List<Categorias> getCategorias();
}
