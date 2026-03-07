package es.mundohardware.controllers;

import es.mundohardware.beans.LineaPedidos;
import es.mundohardware.beans.Pedidos;
import es.mundohardware.beans.Productos;
import es.mundohardware.beans.Usuarios;
import es.mundohardware.dao.IPedidoDAO;
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
 * Controlador de Gestión de Pedidos y Carrito de la Compra. Aísla la lógica
 * comercial referida a las transacciones económicas. Se encarga de añadir y
 * eliminar productos del carrito virtual, finalizar el proceso de "Checkout"
 * (compra), calcular el IVA, y consultar el historial de pedidos y facturas
 * emitidas para un usuario determinado. Coordina la persistencia híbrida del
 * carrito: Cookies para anónimos y Base de Datos para usuarios registrados.
 *
 * @author Alberto García Izquierdo
 * @version 1.0
 */
@WebServlet(name = "PedidoController", urlPatterns = {"/PedidoController"})
public class PedidoController extends HttpServlet {

    /**
     * Núcleo de enrutamiento para transacciones. Analiza el parámetro "accion"
     * para decidir qué operación de carrito o pedido se debe ejecutar. Protege
     * la ruta de compra exigiendo que exista un usuario autenticado en sesión,
     * y coordina la persistencia en base de datos una vez que un pedido se
     * formaliza y se emite la factura.
     *
     * @param request La petición HTTP enviada por el cliente.
     * @param response La respuesta HTTP del servidor.
     * @throws ServletException En caso de un fallo interno en el manejo del
     * Servlet.
     * @throws IOException En caso de un error de lectura/escritura HTTP.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "verCarrito";
        }

        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IProductoDAO prodDAO = daof.getProductoDAO();
        IPedidoDAO pedDAO = daof.getPedidoDAO();

        HttpSession session = request.getSession();
        if (session.getAttribute("carrito") == null) {
            Pedidos carritoCookie = recuperarCarritoDeCookie(request, prodDAO);
            if (carritoCookie != null && !carritoCookie.getLineas().isEmpty()) {
                session.setAttribute("carrito", carritoCookie);
            }
        }

        String url = "index.jsp";

        switch (accion) {
            case "anadirCarrito":
                addToCart(request, response, prodDAO, pedDAO);
                request.setAttribute("listaProductos", prodDAO.getProductosAleatorios(6));
                request.setAttribute("mensaje", "Producto añadido correctamente");
                url = "JSP/tienda/catalogo.jsp";
                break;

            case "verCarrito":
                url = "JSP/tienda/carrito.jsp";
                break;

            case "eliminarLinea":
                removeFromCart(request, response, pedDAO);
                url = "JSP/tienda/carrito.jsp";
                break;

            case "vaciarCarrito":
                vaciarCarritoLogico(request, response, pedDAO);
                url = "JSP/tienda/carrito.jsp";
                break;

            case "comprar":
                Usuarios userLog = (Usuarios) session.getAttribute("usuario");
                if (userLog == null) {
                    request.setAttribute("error", "Debes iniciar sesión para comprar.");
                    url = "JSP/usuarios/login.jsp";
                } else {
                    Pedidos carritoComprar = (Pedidos) session.getAttribute("carrito");
                    if (carritoComprar != null && !carritoComprar.getLineas().isEmpty()) {

                        double total = 0;
                        for (LineaPedidos lp : carritoComprar.getLineas()) {
                            total += (lp.getProducto().getPrecio() * lp.getCantidad());
                        }

                        double baseImponible = total / 1.21;
                        double importeIva = total - baseImponible;

                        carritoComprar.setImporte(baseImponible);
                        carritoComprar.setIva(importeIva);
                        carritoComprar.setIdUsuario(userLog.getIdUsuario());

                        daof.getPedidoDAO().guardarCarritoBaseDatos(carritoComprar);
                        daof.getPedidoDAO().finalizarPedido(carritoComprar);

                        request.setAttribute("factura", carritoComprar);

                        session.setAttribute("carrito", new Pedidos());
                        Cookie c = new Cookie("carritoAnonimo", "");
                        c.setMaxAge(0);
                        c.setPath("/");
                        response.addCookie(c);

                        url = "JSP/tienda/factura.jsp";

                    } else {
                        request.setAttribute("listaProductos", prodDAO.getProductosAleatorios(6));
                        url = "JSP/tienda/catalogo.jsp";
                    }
                }
                break;

            case "verPedidos":
                Usuarios uPedidos = (Usuarios) session.getAttribute("usuario");
                if (uPedidos != null) {
                    request.setAttribute("misPedidos", daof.getPedidoDAO().getPedidosFinalizados(uPedidos.getIdUsuario()));
                    url = "JSP/usuarios/pedidos.jsp";
                } else {
                    url = "JSP/usuarios/login.jsp";
                }
                break;

            case "verFactura":
                Usuarios uFactura = (Usuarios) session.getAttribute("usuario");
                if (uFactura != null) {
                    int idPedidoFactura = Integer.parseInt(request.getParameter("idPedido"));
                    Pedidos factura = daof.getPedidoDAO().getPedidoById(idPedidoFactura, uFactura.getIdUsuario());
                    if (factura != null) {
                        request.setAttribute("factura", factura);
                        url = "JSP/tienda/factura.jsp";
                    } else {
                        url = "JSP/usuarios/perfil.jsp";
                    }
                } else {
                    url = "JSP/usuarios/login.jsp";
                }
                break;
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * Agrega de forma segura un producto al carrito virtual del usuario. Si el
     * producto ya se encontraba previamente en el carrito, se incrementa su
     * cantidad en lugar de duplicar la línea de pedido. Tras la operación,
     * sincroniza el estado del carrito guardándolo en una Cookie o en BD.
     *
     * @param request Objeto de petición para acceder a la sesión actual y
     * parámetros.
     * @param response Objeto de respuesta para insertar/actualizar la cookie en
     * el navegador.
     * @param prodDAO Data Access Object utilizado para validar la existencia
     * del producto.
     * @param pedDAO Data Access Object para guardar el carrito si el usuario
     * está registrado.
     */
    private void addToCart(HttpServletRequest request, HttpServletResponse response, IProductoDAO prodDAO, IPedidoDAO pedDAO) {
        HttpSession session = request.getSession();
        Pedidos carrito = (Pedidos) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new Pedidos();
        }
        int idProd = Integer.parseInt(request.getParameter("idProducto"));
        boolean existe = false;
        for (LineaPedidos lp : carrito.getLineas()) {
            if (lp.getProducto().getIdProducto() == idProd) {
                lp.setCantidad(lp.getCantidad() + 1);
                existe = true;
                break;
            }
        }
        if (!existe) {
            Productos p = prodDAO.getProductoById(idProd);
            if (p != null) {
                LineaPedidos linea = new LineaPedidos();
                linea.setProducto(p);
                linea.setCantidad(1);
                linea.setIdProducto(p.getIdProducto());
                carrito.getLineas().add(linea);
            }
        }

        session.setAttribute("carrito", carrito);
        Usuarios usuario = (Usuarios) session.getAttribute("usuario");

        if (usuario != null) {
            carrito.setIdUsuario(usuario.getIdUsuario());
            pedDAO.guardarCarritoBaseDatos(carrito);
        } else {
            guardarCarritoEnCookie(carrito, response);
        }
    }

    /**
     * Elimina una línea completa del carrito de la compra basada en el ID del
     * producto seleccionado, recalculando de manera implícita los totales y
     * actualizando la sesión y la cookie o BD.
     *
     * @param request Petición HTTP que contiene el ID del producto a eliminar.
     * @param response Respuesta HTTP para reflejar los cambios en la cookie.
     * @param pedDAO Data Access Object para borrar línea en BD si corresponde.
     */
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response, IPedidoDAO pedDAO) {
        HttpSession session = request.getSession();
        Pedidos carrito = (Pedidos) session.getAttribute("carrito");
        if (carrito != null) {
            int idProd = Integer.parseInt(request.getParameter("id"));
            for (int i = 0; i < carrito.getLineas().size(); i++) {
                if (carrito.getLineas().get(i).getProducto().getIdProducto() == idProd) {
                    carrito.getLineas().remove(i);
                    break;
                }
            }
            session.setAttribute("carrito", carrito);

            Usuarios usuario = (Usuarios) session.getAttribute("usuario");
            if (usuario != null) {
                carrito.setIdUsuario(usuario.getIdUsuario());
                pedDAO.guardarCarritoBaseDatos(carrito);
            } else {
                guardarCarritoEnCookie(carrito, response);
            }
        }
    }

    private void vaciarCarritoLogico(HttpServletRequest request, HttpServletResponse response, IPedidoDAO pedDAO) {
        HttpSession session = request.getSession();
        Usuarios usuario = (Usuarios) session.getAttribute("usuario");
        Pedidos carritoVacio = new Pedidos();

        session.setAttribute("carrito", carritoVacio);

        if (usuario != null) {
            carritoVacio.setIdUsuario(usuario.getIdUsuario());
            pedDAO.guardarCarritoBaseDatos(carritoVacio);
        } else {
            borrarCookieCarrito(response);
        }
    }

    /**
     * Realiza la serialización manual del objeto carrito convirtiéndolo en una
     * cadena de texto estructurada para poder inyectarlo de forma persistente
     * en una cookie del navegador.
     *
     * @param carrito El objeto Pedidos actual con las líneas que posee el
     * cliente.
     * @param response El objeto de respuesta donde se adjuntará la cookie
     * configurada con expiración.
     */
    private void guardarCarritoEnCookie(Pedidos carrito, HttpServletResponse response) {
        if (carrito == null || carrito.getLineas().isEmpty()) {
            borrarCookieCarrito(response);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (LineaPedidos lp : carrito.getLineas()) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append(lp.getProducto().getIdProducto()).append(":").append(lp.getCantidad());
        }
        Cookie c = new Cookie("carritoAnonimo", sb.toString());
        c.setMaxAge(2 * 24 * 60 * 60);
        c.setPath("/");
        response.addCookie(c);
    }

    /**
     * Analiza las cookies que viajan en la petición HTTP del usuario en busca
     * de la cookie especial "carritoAnonimo". Si la encuentra, deserializa su
     * contenido y reconstruye el objeto Pedidos original restaurando los
     * productos desde la BBDD.
     *
     * @param request Petición del cliente para leer las cookies.
     * @param prodDAO Objeto DAO para hidratar los detalles de los productos.
     * @return Objeto Pedidos con el historial no formalizado, o uno vacío si no
     * existe la cookie.
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

    /**
     * Fuerza la eliminación lógica de la cookie de persistencia de compra
     * estableciendo su tiempo máximo a 0 segundos.
     *
     * @param response Respuesta donde se añade la instrucción de borrado para
     * el navegador.
     */
    private void borrarCookieCarrito(HttpServletResponse response) {
        Cookie c = new Cookie("carritoAnonimo", "");
        c.setMaxAge(0);
        c.setPath("/");
        response.addCookie(c);
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
