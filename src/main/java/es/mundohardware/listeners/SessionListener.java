package es.mundohardware.listeners;

import es.mundohardware.beans.Pedidos;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Escuchador (Listener) del ciclo de vida de la sesión HTTP. Se encarga de
 * preparar el entorno del usuario al acceder a la aplicación.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    /**
     * Captura el evento de creación de una nueva sesión.Inicializa un carrito 
     * vacío en la sesión para prevenir errores de nulos. * @param se Evento de la sesión
     * @param se
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setAttribute("carrito", new Pedidos());
        System.out.println("Nueva sesión creada y carrito inicializado.");
    }

    /**
     * Captura el evento de destrucción de la sesión (caducidad o cierre
     * manual).
     *
     * * @param se Evento de la sesión
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Sesión finalizada.");
    }
}
