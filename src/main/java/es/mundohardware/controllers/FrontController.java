package es.mundohardware.controllers;

import es.mundohardware.beans.LineaPedidos;
import es.mundohardware.beans.Pedidos;
import es.mundohardware.beans.Productos;
import es.mundohardware.dao.IProductoDAO;
import es.mundohardware.daofactory.DAOFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controlador del Catálogo y Escaparate Principal. Gestiona todas las
 * peticiones relacionadas con la visualización pública de la tienda. Se encarga
 * de mostrar la página de inicio, resolver las búsquedas de productos (por
 * texto, categoría o marca), aplicar filtros de precios y renderizar los
 * detalles específicos de cada artículo.
 *
 * @author Alberto García Izquierdo
 * @version 1.0
 */
@WebServlet(name = "FrontController", urlPatterns = {"/FrontController"})
public class FrontController extends HttpServlet {

    /**
     * Procesamiento para las peticiones HTTP (GET y POST). Intercepta
     * la acción solicitada por el usuario en la interfaz y coordina la lógica
     * de negocio consultando el inventario a través de la capa DAO. Una vez
     * procesados los datos, despacha la respuesta hacia la vista JSP
     * correspondiente. También asegura que los usuarios sin sesión tengan un
     * carrito temporal asignado desde sus cookies.
     *
     * @param request La petición HTTP enviada por el cliente.
     * @param response La respuesta HTTP generada por el servidor.
     * @throws ServletException Si ocurre un problema a nivel del contenedor de
     * Servlets.
     * @throws IOException Si existe un problema de Entrada/Salida en el flujo
     * de datos.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "inicio";
        }

        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IProductoDAO prodDAO = daof.getProductoDAO();

        HttpSession session = request.getSession();
        if (session.getAttribute("carrito") == null) {
            Pedidos carritoCookie = recuperarCarritoDeCookie(request, prodDAO);
            if (carritoCookie != null && !carritoCookie.getLineas().isEmpty()) {
                session.setAttribute("carrito", carritoCookie);
            }
        }

        String url = "index.jsp";

        switch (accion) {
            case "inicio":
                request.setAttribute("listaProductos", prodDAO.getProductosAleatorios(6));
                url = "JSP/tienda/catalogo.jsp";
                break;

            case "buscar":
                String[] categoriasArray = request.getParameterValues("categoria");
                String query = request.getParameter("query");
                String[] marcasArray = request.getParameterValues("marca");

                if (categoriasArray != null && categoriasArray.length > 0) {
                    request.setAttribute("listaProductos", prodDAO.getProductosByCategorias(categoriasArray));
                } else if (marcasArray != null && marcasArray.length > 0) {
                    request.setAttribute("listaProductos", prodDAO.getProductosByMarcas(marcasArray));
                } else if (query != null && !query.trim().isEmpty()) {
                    request.setAttribute("listaProductos", prodDAO.buscarProductos(query));
                } else {
                    request.setAttribute("listaProductos", prodDAO.getProductos());
                }
                url = "JSP/tienda/catalogo.jsp";
                break;

            case "filtrarPrecio":
                double minPrecio = 0;
                double maxPrecio = 1880;

                try {
                    String minStr = request.getParameter("minPrecio");
                    String maxStr = request.getParameter("maxPrecio");

                    if (minStr != null && !minStr.isEmpty()) {
                        minPrecio = Double.parseDouble(minStr);
                    }
                    if (maxStr != null && !maxStr.isEmpty()) {
                        maxPrecio = Double.parseDouble(maxStr);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                request.setAttribute("listaProductos", prodDAO.filtrarPorPrecio(minPrecio, maxPrecio));

                request.setAttribute("minPrecioSel", minPrecio);
                request.setAttribute("maxPrecioSel", maxPrecio);

                url = "JSP/tienda/catalogo.jsp";
                break;

            case "verDetalle":
                int id = Integer.parseInt(request.getParameter("id"));
                Productos p = prodDAO.getProductoById(id);
                request.setAttribute("producto", p);
                url = "JSP/tienda/detalle.jsp";
                break;
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * Extrae y reconstruye el carrito de compras a partir de las cookies
     * almacenadas en el navegador del cliente. Este método es vital para
     * mantener la persistencia de los productos seleccionados por usuarios
     * anónimos que aún no han iniciado sesión en la aplicación.
     *
     * @param request La petición HTTP que contiene el array de cookies del
     * cliente.
     * @param prodDAO Instancia de acceso a datos para recuperar la información
     * íntegra del producto.
     * @return Un objeto de tipo Pedidos que representa el carrito de la compra
     * recuperado.
     */
    private Pedidos recuperarCarritoDeCookie(HttpServletRequest request, IProductoDAO prodDAO) {
        Pedidos carrito = new Pedidos();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("carritoAnonimo".equals(c.getName())) {
                    String valor = c.getValue();
                    String[] items = valor.split("\\|");
                    for (String item : items) {
                        try {
                            String[] data = item.split(":");
                            int id = Integer.parseInt(data[0]);
                            int cant = Integer.parseInt(data[1]);
                            Productos p = prodDAO.getProductoById(id);
                            if (p != null) {
                                LineaPedidos linea = new LineaPedidos();
                                linea.setProducto(p);
                                linea.setIdProducto(id);
                                linea.setCantidad(cant);
                                carrito.getLineas().add(linea);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        return carrito;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
