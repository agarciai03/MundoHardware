package es.mundohardware.filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

/**
 * Filtro de Servlets (Filter) que intercepta todas las peticiones a la
 * aplicación. Asegura que tanto la petición entrante como la respuesta saliente
 * utilicen la codificación de caracteres UTF-8, previniendo errores con tildes
 * y ñ.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
@WebFilter(filterName = "AUTF8", urlPatterns = {"/*"})
public class AUTF8 implements Filter {

    /**
     * Inicializa el filtro.
     *
     * @param fConfig
     * @throws ServletException Si ocurre un error durante la inicialización
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    /**
     * Procesa la petición aplicando la codificación UTF-8 antes de pasar el
     * control al siguiente elemento en la cadena de filtros.* @param request La petición del cliente
     *
     * @param request
     * @param response La respuesta al cliente
     * @param chain La cadena de filtros para continuar la ejecución
     * @throws IOException Si ocurre un error de Entrada/Salida
     * @throws ServletException Si ocurre un error de Servlet
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }

    /**
     * Destruye el filtro liberando los recursos asociados.
     */
    @Override
    public void destroy() {
    }
}
