package es.mundohardware.dao;

import es.mundohardware.beans.Productos;
import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a datos permitidas para la
 * entidad Productos, incluyendo filtros y búsquedas avanzadas.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public interface IProductoDAO {

    /**
     * Obtiene la lista completa de productos del catálogo.
     *
     * @return Una lista de objetos Productos
     */
    public List<Productos> getProductos();

    /**
     * Busca un producto en concreto mediante su ID.
     *
     * @param id El identificador del producto
     * @return El objeto Producto, o null si no existe
     */
    public Productos getProductoById(int id);

    /**
     * Obtiene una lista aleatoria de productos para mostrar como destacados.
     *
     * @param limite La cantidad máxima de productos a devolver
     * @return Una lista de productos aleatorios
     */
    public List<Productos> getProductosAleatorios(int limite);

    /**
     * Busca productos cuyo nombre o descripción coincida con la cadena
     * indicada.
     *
     * @param query El texto a buscar
     * @return Una lista de productos que coinciden con la búsqueda
     */
    public List<Productos> buscarProductos(String query);

    /**
     * Obtiene los productos que pertenecen a una serie de categorías dadas.
     *
     * @param categorias Un array con los IDs de las categorías
     * @return Una lista de productos filtrados por categoría
     */
    public List<Productos> getProductosByCategorias(String[] categorias);

    /**
     * Obtiene todas las marcas únicas existentes en la base de datos.
     *
     * @return Una lista de cadenas con los nombres de las marcas
     */
    public List<String> getMarcas();

    /**
     * Obtiene los productos que pertenecen a una serie de marcas dadas.
     *
     * @param marcas Un array con los nombres de las marcas
     * @return Una lista de productos filtrados por marca
     */
    public List<Productos> getProductosByMarcas(String[] marcas);

    /**
     * Obtiene el precio más bajo de todo el catálogo.
     *
     * @return El precio mínimo como número decimal
     */
    public double getPrecioMinimo();

    /**
     * Obtiene el precio más alto de todo el catálogo.
     *
     * @return El precio máximo como número decimal
     */
    public double getPrecioMaximo();

    /**
     * Filtra los productos para devolver solo aquellos cuyo precio se encuentre
     * entre un valor mínimo y uno máximo.
     *
     * @param min El precio mínimo
     * @param max El precio máximo
     * @return Una lista de productos dentro del rango de precios
     */
    public List<Productos> filtrarPorPrecio(double min, double max);
}
