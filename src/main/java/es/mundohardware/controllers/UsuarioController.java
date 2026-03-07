package es.mundohardware.controllers;

import es.mundohardware.beans.LineaPedidos;
import es.mundohardware.beans.Pedidos;
import es.mundohardware.beans.Productos;
import es.mundohardware.beans.Usuarios;
import es.mundohardware.dao.IProductoDAO;
import es.mundohardware.dao.IUsuarioDAO;
import es.mundohardware.daofactory.DAOFactory;
import es.mundohardware.utils.Util;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Controlador Especializado en Cuentas de Usuario y Seguridad. Modera todos los
 * flujos de autenticación, registro, cierre de sesión y mantenimiento de
 * perfiles personales. Posee MultipartConfig, crucial para la
 * subida y procesado de imágenes al servidor. Coordina de forma
 * inteligente la migración del carrito de cookies a la base de datos cuando un
 * usuario anónimo decide iniciar sesión o registrarse.
 *
 * @author Alberto García Izquierdo
 * @version 1.0
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 10,
        maxFileSize = 1024 * 100,
        maxRequestSize = 1024 * 110
)
@WebServlet(name = "UsuarioController", urlPatterns = {"/UsuarioController"})
public class UsuarioController extends HttpServlet {

    /**
     * Gestor central de eventos para acciones relacionadas con el usuario.
     * Controla el proceso de Login, validando credenciales MD5, sincronizando
     * el carrito temporal con el de Base de Datos y controlando el registro
     * seguro de nuevas cuentas. Finalmente dirige a la vista JSP pertinente
     * basada en el éxito o fracaso de las validaciones de negocio.
     *
     * @param request La solicitud HTTP con los parámetros de formulario
     * enviados por el usuario.
     * @param response La respuesta generada que dirige al JSP destino.
     * @throws ServletException Error interno del ciclo de vida del contenedor
     * Servlet.
     * @throws IOException Problemas de lectura o escritura en el flujo.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "login";
        }

        DAOFactory daof = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        IProductoDAO prodDAO = daof.getProductoDAO();
        IUsuarioDAO userDAO = daof.getUsuarioDAO();

        HttpSession session = request.getSession();
        if (session.getAttribute("carrito") == null) {
            Pedidos carritoCookie = recuperarCarritoDeCookie(request, prodDAO);
            if (carritoCookie != null && !carritoCookie.getLineas().isEmpty()) {
                session.setAttribute("carrito", carritoCookie);
            }
        }

        String url = "index.jsp";

        switch (accion) {
            case "login":
                url = "JSP/usuarios/login.jsp";
                break;

            case "validarLogin":
                String email = request.getParameter("email");
                String pass = Util.getMD5(request.getParameter("password"));

                Usuarios usuarioLogueado = userDAO.login(email, pass);

                if (usuarioLogueado != null) {
                    session.setAttribute("usuario", usuarioLogueado);

                    Pedidos carritoSesion = (Pedidos) session.getAttribute("carrito");
                    es.mundohardware.dao.IPedidoDAO pedDAO = daof.getPedidoDAO();

                    if (usuarioLogueado.getUltimoAcceso() != null) {
                        borrarCookieCarrito(response);
                        session.removeAttribute("carrito");
                        Pedidos carritoBD = pedDAO.getCarritoBaseDatos(usuarioLogueado.getIdUsuario());
                        if (carritoBD != null) {
                            session.setAttribute("carrito", carritoBD);
                        }
                    } else {
                        if (carritoSesion != null && !carritoSesion.getLineas().isEmpty()) {
                            carritoSesion.setIdUsuario(usuarioLogueado.getIdUsuario());
                            pedDAO.guardarCarritoBaseDatos(carritoSesion);
                        }
                    }

                    request.setAttribute("mensaje", "Bienvenido " + usuarioLogueado.getNombre());
                    request.setAttribute("listaProductos", prodDAO.getProductosAleatorios(6));
                    url = "JSP/tienda/catalogo.jsp";
                } else {
                    request.setAttribute("error", "Credenciales incorrectas");
                    url = "JSP/usuarios/login.jsp";
                }
                break;

            case "verPerfil":
                url = "JSP/usuarios/perfil.jsp";
                break;

            case "actualizarPerfil":
                actualizarPerfilConSeguridad(request, userDAO);
                Usuarios uLog = (Usuarios) session.getAttribute("usuario");
                if (uLog != null) {
                    es.mundohardware.dao.IPedidoDAO pDao = daof.getPedidoDAO();
                    request.setAttribute("misPedidos", pDao.getPedidosFinalizados(uLog.getIdUsuario()));
                }
                url = "JSP/usuarios/perfil.jsp";
                break;

            case "logout":
                Usuarios uLogout = (Usuarios) session.getAttribute("usuario");
                if (uLogout != null) {
                    userDAO.registrarUltimoAcceso(uLogout.getIdUsuario());
                }
                session.invalidate();
                response.sendRedirect("FrontController?accion=inicio");
                return;

            case "registro":
                url = "JSP/usuarios/registro.jsp";
                break;

            case "registrarUsuario":
                // lógica del registro
                registrarUsuario(request, userDAO);

                // Comprobaciones
                if (request.getAttribute("error") != null) {
                    url = "JSP/usuarios/registro.jsp";
                } else {
                    String emailReg = request.getParameter("email");
                    String passHashReg = Util.getMD5(request.getParameter("password"));

                    // El Dao logea al usuario
                    Usuarios usuarioNuevo = userDAO.login(emailReg, passHashReg);

                    if (usuarioNuevo != null) {
                        // Lo metemos en sesión
                        session.setAttribute("usuario", usuarioNuevo);

                        // Borramos la cookie del carrito 
                        borrarCookieCarrito(response);

                        // Recuperamos el carrito 
                        es.mundohardware.dao.IPedidoDAO pedDAO = daof.getPedidoDAO();
                        Pedidos carritoBD = pedDAO.getCarritoBaseDatos(usuarioNuevo.getIdUsuario());

                        if (carritoBD != null) {
                            session.setAttribute("carrito", carritoBD);
                        } else {
                            session.setAttribute("carrito", new Pedidos());
                        }

                        request.setAttribute("mensaje", "¡Cuenta creada con éxito! Bienvenido/a " + usuarioNuevo.getNombre());
                        request.setAttribute("listaProductos", prodDAO.getProductosAleatorios(6));
                        url = "JSP/tienda/catalogo.jsp";

                    } else {
                        url = "JSP/usuarios/login.jsp";
                    }
                }
                break;
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * Recibe los datos enviados desde el panel del usuario y efectúa la
     * modificación del perfil garantizando la seguridad. Evita que un
     * usuario manipule los campos bloqueados.
     * Además, administra la lógica para el cambio seguro de
     * contraseña validando el hash MD5 antiguo y gestiona la escritura del
     * archivo físico para el nuevo Avatar.
     *
     * @param request Solicitud web para mapear y validar parámetros como el
     * archivo 'avatar'.
     * @param userDAO Conector con la BD que ejecuta finalmente la orden UPDATE.
     */
    private void actualizarPerfilConSeguridad(HttpServletRequest request, IUsuarioDAO userDAO) {
        HttpSession session = request.getSession();
        Usuarios usuario = (Usuarios) session.getAttribute("usuario");

        if (usuario == null) {
            return;
        }

        String mensaje = "Perfil actualizado correctamente.";
        String error = null;

        String oldEmail = usuario.getEmail();
        String oldNif = usuario.getNif();
        String oldPassword = usuario.getPassword(); // Guardamos el hash viejo

        try {
            // Uso estricto de la API BeanUtils
            BeanUtils.populate(usuario, request.getParameterMap());

            // Restauramos los valores sensibles para que no puedan ser inyectados
            usuario.setEmail(oldEmail);
            usuario.setNif(oldNif);
            usuario.setPassword(oldPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // cambio de contraseña
        String passActual = request.getParameter("passwordActual");
        String passNueva = request.getParameter("passwordNueva");
        String passRepetir = request.getParameter("passwordRepetir");

        if (passActual != null && !passActual.isEmpty()) {
            String passActualHash = Util.getMD5(passActual);

            if (!passActualHash.equals(oldPassword)) {
                error = "La contraseña actual no es correcta. No se han guardado los cambios.";
            } else {
                if (passNueva != null && !passNueva.isEmpty() && passNueva.equals(passRepetir)) {
                    usuario.setPassword(Util.getMD5(passNueva));
                    mensaje += " Contraseña cambiada.";
                } else {
                    error = "Las nuevas contraseñas no coinciden.";
                }
            }
        }

        // subida de avatar
        try {
            Part part = request.getPart("avatar");
            if (part != null && part.getSize() > 0) {
                String nombreFichero = "avatar_" + usuario.getIdUsuario() + ".jpg";
                String rutaFisica = getServletContext().getRealPath("") + File.separator + "imagen" + File.separator + "avatar";
                File carpeta = new File(rutaFisica);
                if (!carpeta.exists()) {
                    carpeta.mkdirs();
                }
                part.write(rutaFisica + File.separator + nombreFichero);
                usuario.setAvatar(nombreFichero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // guardar en bbdd
        if (error == null) {
            userDAO.actualizar(usuario);
            session.setAttribute("usuario", usuario);
            request.setAttribute("mensaje", mensaje);

            es.mundohardware.dao.IPedidoDAO pDao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getPedidoDAO();
            request.setAttribute("misPedidos", pDao.getPedidosFinalizados(usuario.getIdUsuario()));
        } else {
            request.setAttribute("error", error);
        }
    }

    /**
     * Transforma un usuario no registrado en un cliente de base de datos. Se
     * encarga de mapear automáticamente los parámetros del formulario a un
     * Bean, encriptar de manera segura la contraseña, y gestionar la
     * persistencia en BBDD. Tras registrarse, liga los artículos del carrito
     * que el usuario tenía en sesión directamente al nuevo ID generado.
     *
     * @param request La solicitud HTTP que provee los campos del formulario de
     * registro y la imagen de avatar.
     * @param userDAO Objeto DAO responsable del guardado y creación final del
     * perfil de usuario.
     */
    private void registrarUsuario(HttpServletRequest request, IUsuarioDAO userDAO) {
        try {
            Usuarios u = new Usuarios();

            // Uso estricto de la API BeanUtils
            BeanUtils.populate(u, request.getParameterMap());

            // encriptamos la contraseña machacando la que metió BeanUtils en texto plano
            String passRaw = request.getParameter("password");
            u.setPassword(Util.getMD5(passRaw));

            int idGenerado = userDAO.registrar(u);

            if (idGenerado > 0) {

                // carrito anónimo pasa a la bbdd al registrarse
                HttpSession ses = request.getSession();
                es.mundohardware.beans.Pedidos carritoSesion = (es.mundohardware.beans.Pedidos) ses.getAttribute("carrito");
                if (carritoSesion != null && !carritoSesion.getLineas().isEmpty()) {
                    carritoSesion.setIdUsuario(idGenerado);
                    DAOFactory.getDAOFactory(DAOFactory.MYSQL).getPedidoDAO().guardarCarritoBaseDatos(carritoSesion);
                }
                Part part = request.getPart("avatar");
                if (part != null && part.getSize() > 0) {
                    if (part.getSize() > 102400) {
                        request.setAttribute("mensaje", "Usuario registrado, pero la imagen pesaba mucho.");
                    } else {
                        String nombreFichero = "avatar_" + idGenerado + ".jpg";
                        String rutaFisica = getServletContext().getRealPath("") + File.separator + "imagen" + File.separator + "avatar";
                        File carpeta = new File(rutaFisica);
                        if (!carpeta.exists()) {
                            carpeta.mkdirs();
                        }
                        part.write(rutaFisica + File.separator + nombreFichero);
                        userDAO.actualizarAvatar(idGenerado, nombreFichero);
                    }
                }
                request.setAttribute("mensaje", "¡Registro completado! Inicia sesión.");
            } else {
                request.setAttribute("error", "Error al registrar en BBDD.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error técnico: " + e.getMessage());
        }
    }

    /**
     * Purga la cookie temporal "carritoAnonimo" del sistema del cliente.
     * Especialmente útil y ejecutado cuando un visitante anónimo decide
     * registrarse o loguearse, evitando redundancias en la asignación del
     * carrito.
     *
     * @param response La respuesta a la cual se anexa la orden de caducidad de
     * la cookie.
     */
    private void borrarCookieCarrito(HttpServletResponse response) {
        Cookie c = new Cookie("carritoAnonimo", "");
        c.setMaxAge(0);
        c.setPath("/");
        response.addCookie(c);
    }

    /**
     * Reconstituye y monta el objeto carrito buscando el rastro almacenado en
     * las cookies. Este mecanismo de rescate garantiza que, aunque el usuario
     * acceda directamente a vistas dependientes de UsuarioController, no pierda
     * el contenido de su carrito temporal.
     *
     * @param request La solicitud actual conteniendo el array de Cookies.
     * @param prodDAO Objeto encargado de buscar en BD las instancias reales de
     * Producto.
     * @return Una instancia de tipo Pedidos poblada y lista para ser inyectada
     * en sesión.
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