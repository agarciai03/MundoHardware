package es.mundohardware.controllers;

import es.mundohardware.beans.Categorias;
import es.mundohardware.dao.ICategoriaDAO;
import es.mundohardware.dao.IProductoDAO;
import es.mundohardware.daofactory.DAOFactory;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Controlador de Inicialización de la aplicación (MundoHardware). Este Servlet
 * está configurado para ejecutarse automáticamente al arrancar el servidor
 * y no atiende peticiones directas del usuario. Su objetivo
 * principal es cargar en la memoria global de la aplicación  
 * aquellos datos estáticos y de consulta masiva que se
 * utilizarán repetidamente, mejorando así el rendimiento general.
 *
 * @author Alberto García Izquierdo
 * @version 1.0
 */
@WebServlet(name = "InitController", urlPatterns = {"/InitController"}, loadOnStartup = 1)
public class InitController extends HttpServlet {

    /**
     * Ciclo de vida inicial del Servlet. Se ejecuta una única vez al levantar
     * el servidor. Realiza consultas a la base de datos mediante los DAO
     * pertinentes para extraer las categorías, las marcas registradas y los
     * rangos de precios (máximo y mínimo). Estos datos se guardan como
     * atributos de contexto para que estén disponibles permanentemente para
     * todas las sesiones y vistas.
     *
     * @throws ServletException Si ocurre un error crítico durante la carga
     * inicial.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IProductoDAO prodDAO = daof.getProductoDAO();
        ICategoriaDAO catDAO = daof.getCategoriaDAO();

        ServletContext context = getServletContext();

        List<Categorias> categorias = catDAO.getCategorias();
        context.setAttribute("appCategorias", categorias);

        List<String> marcas = prodDAO.getMarcas();
        context.setAttribute("appMarcas", marcas);

        context.setAttribute("appPrecioMax", prodDAO.getPrecioMaximo());
        context.setAttribute("appPrecioMin", prodDAO.getPrecioMinimo());

        System.out.println(" APLICACIÓN MUNDO HARDWARE INICIADA ");
    }
}
