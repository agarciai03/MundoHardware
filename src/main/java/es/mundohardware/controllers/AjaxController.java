package es.mundohardware.controllers;

import com.google.gson.Gson;
import es.mundohardware.beans.LineaPedidos;
import es.mundohardware.daofactory.DAOFactory;
import es.mundohardware.dao.IUsuarioDAO;
import es.mundohardware.utils.Util;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador encargado de gestionar las peticiones asíncronas (AJAX). Procesa
 * la comprobación de email en vivo, el cálculo del NIF y la manipulación de
 * cantidades en el carrito de compra sin recargar la web. Devuelve respuestas
 * estructuradas en formato JSON.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
@WebServlet(name = "AjaxController", urlPatterns = {"/AjaxController"})
public class AjaxController extends HttpServlet {

    /**
     * Procesa las peticiones HTTP (GET y POST) basándose en el parámetro
     * 'accion'.
     *
     * @param request Petición del servlet
     * @param response Respuesta del servlet
     * @throws ServletException Si ocurre un error específico en el servlet
     * @throws IOException Si ocurre un error de Entrada/Salida
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8"); 

        String accion = request.getParameter("accion");
        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        Gson gson = new Gson(); // Objeto que convierte a JSON

        switch (accion) {

            case "checkEmail":
                IUsuarioDAO userDAO = daof.getUsuarioDAO();
                String emailCheck = request.getParameter("email");
                boolean existe = userDAO.existeEmail(emailCheck);

                // Creamos un mapa y Gson lo pasa a JSON automáticamente
                Map<String, Boolean> mapaEmail = new HashMap<>();
                mapaEmail.put("existe", existe);
                response.getWriter().write(gson.toJson(mapaEmail));
                break;

            case "calcNIF":
                String dniStr = request.getParameter("dni");
                String letra = "";
                try {
                    if (dniStr != null && dniStr.length() == 8) {
                        letra = String.valueOf(Util.calcularLetraNIF(Integer.parseInt(dniStr)));
                    }
                } catch (Exception e) {
                }

                Map<String, String> mapaNif = new HashMap<>();
                mapaNif.put("letra", letra);
                response.getWriter().write(gson.toJson(mapaNif));
                break;

            case "cambiarCantidad":
                int idProd = Integer.parseInt(request.getParameter("id"));
                int op = Integer.parseInt(request.getParameter("op"));

                // AHORA EL CARRITO ES UN OBJETO PEDIDOS
                es.mundohardware.beans.Pedidos carrito = (es.mundohardware.beans.Pedidos) request.getSession().getAttribute("carrito");

                int newQty = 0;
                double rowTotal = 0;
                double totalCart = 0;

                if (carrito != null && carrito.getLineas() != null) {
                    // buscamos en carrito.getLineas()
                    for (int i = 0; i < carrito.getLineas().size(); i++) {
                        es.mundohardware.beans.LineaPedidos lp = carrito.getLineas().get(i);

                        if (lp.getProducto().getIdProducto() == idProd) {
                            newQty = lp.getCantidad() + op;

                            if (newQty <= 0) {
                                // Si baja de 1, lo borramos de las líneas
                                carrito.getLineas().remove(i);
                                newQty = 0;
                            } else {
                                // Si no, actualizamos la cantidad y su subtotal
                                lp.setCantidad(newQty);
                                rowTotal = lp.getProducto().getPrecio() * newQty;
                            }
                            break;
                        }
                    }

                    // RECALCULAMOS EL TOTAL COMPLETO DEL CARRITO
                    for (es.mundohardware.beans.LineaPedidos lp : carrito.getLineas()) {
                        totalCart += (lp.getProducto().getPrecio() * lp.getCantidad());
                    }

                    // Guardamos el carrito actualizado en sesión
                    request.getSession().setAttribute("carrito", carrito);
                }

                // CALCULAMOS BASE E IVA
                double base = totalCart / 1.21;
                double iva = totalCart - base;

                // FABRICAMOS EL JSON 
                String json = String.format(java.util.Locale.US,
                        "{\"status\":\"ok\", \"qty\":%d, \"rowTotal\":%.2f, \"total\":%.2f, \"base\":%.2f, \"iva\":%.2f}",
                        newQty, rowTotal, totalCart, base, iva);

                response.setContentType("application/json");
                response.getWriter().write(json);
                break;
        }
    }

    /**
     * Actualiza la cookie del carrito anónimo tras modificaciones AJAX.
     *
     * @param carrito Lista de líneas del pedido actual
     * @param response Respuesta HTTP para añadir la cookie
     */
    private void guardarCarritoEnCookie(List<LineaPedidos> carrito, HttpServletResponse response) {
        if (carrito == null || carrito.isEmpty()) {
            Cookie c = new Cookie("carritoAnonimo", "");
            c.setMaxAge(0);
            c.setPath("/");
            response.addCookie(c);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (LineaPedidos lp : carrito) {
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
