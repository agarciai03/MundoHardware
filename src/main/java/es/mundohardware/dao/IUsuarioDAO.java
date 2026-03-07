package es.mundohardware.dao;

import es.mundohardware.beans.Usuarios;

/**
 * Interfaz que define las operaciones de acceso a datos relacionadas con los
 * usuarios, su autenticación y su gestión de perfiles.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public interface IUsuarioDAO {

    /**
     * Valida las credenciales de un usuario.
     *
     * @param email El correo electrónico del usuario
     * @param password La contraseña encriptada
     * @return El objeto Usuarios con sus datos si las credenciales son
     * correctas, null en caso contrario
     */
    public Usuarios login(String email, String password);

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param u Objeto Usuarios con los datos del formulario de registro
     * @return El ID generado para el nuevo usuario, o -1 si falla
     */
    public int registrar(Usuarios u);

    /**
     * Comprueba si un correo electrónico ya está registrado en la base de
     * datos.
     *
     * @param email El correo electrónico a comprobar
     * @return true si el email ya existe, false si está disponible
     */
    public boolean existeEmail(String email);

    /**
     * Actualiza el nombre del archivo del avatar de un usuario específico.
     *
     * @param idUsuario El ID del usuario
     * @param nombreArchivo El nuevo nombre del archivo de imagen
     */
    public void actualizarAvatar(int idUsuario, String nombreArchivo);

    /**
     * Actualiza los datos generales del perfil de un usuario.
     *
     * @param u El objeto Usuarios con los datos actualizados
     */
    public void actualizar(Usuarios u);

    /**
     * Borra físicamente a un usuario de la base de datos.
     *
     * @param idUsuario El ID del usuario a eliminar
     * @return true si el borrado ha sido exitoso, false en caso contrario
     */
    public boolean borrar(int idUsuario);

    /**
     * Registra la fecha y hora actual como el último acceso del usuario al
     * sistema.
     *
     * @param idUsuario El ID del usuario
     */
    public void registrarUltimoAcceso(int idUsuario);
}
