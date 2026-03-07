package es.mundohardware.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que representa a un Usuario registrado en el sistema. Contiene todos
 * los datos personales, de contacto y de acceso.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public class Usuarios implements Serializable {

    private int idUsuario;
    private String email;
    private String password;
    private String nombre;
    private String apellidos;
    private String nif;
    private String telefono;
    private String direccion;
    private String codigoPostal;
    private String localidad;
    private String provincia;
    private Date ultimoAcceso;
    private String avatar;

    /**
     * Constructor por defecto de Usuarios.
     */
    public Usuarios() {
    }

    /**
     * Obtiene el ID único del usuario.
     *
     * @return El identificador del usuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el ID único del usuario.
     *
     * @param idUsuario El ID a asignar
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return El email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email El email a asignar
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña encriptada del usuario.
     *
     * @return La contraseña del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password La contraseña a asignar (encriptada)
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre El nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del usuario.
     *
     * @return Los apellidos del usuario
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del usuario.
     *
     * @param apellidos Los apellidos a asignar
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el Documento Nacional de Identidad del usuario.
     *
     * @return El NIF del usuario
     */
    public String getNif() {
        return nif;
    }

    /**
     * Establece el NIF del usuario.
     *
     * @param nif El NIF a asignar
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * Obtiene el teléfono de contacto del usuario.
     *
     * @return El teléfono del usuario
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono de contacto del usuario.
     *
     * @param telefono El teléfono a asignar
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la dirección postal del usuario.
     *
     * @return La dirección del usuario
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección postal del usuario.
     *
     * @param direccion La dirección a asignar
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene el código postal del usuario.
     *
     * @return El código postal
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Establece el código postal del usuario.
     *
     * @param codigoPostal El código postal a asignar
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Obtiene la localidad de residencia del usuario.
     *
     * @return La localidad
     */
    public String getLocalidad() {
        return localidad;
    }

    /**
     * Establece la localidad de residencia del usuario.
     *
     * @param localidad La localidad a asignar
     */
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    /**
     * Obtiene la provincia de residencia del usuario.
     *
     * @return La provincia
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Establece la provincia de residencia del usuario.
     *
     * @param provincia La provincia a asignar
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Obtiene la fecha del último inicio de sesión del usuario.
     *
     * @return La fecha del último acceso
     */
    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }

    /**
     * Establece la fecha del último inicio de sesión.
     *
     * @param ultimoAcceso La fecha a asignar
     */
    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    /**
     * Obtiene el nombre del archivo de imagen de perfil (avatar).
     *
     * @return El nombre del archivo del avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Establece el nombre del archivo de imagen de perfil.
     *
     * @param avatar El nombre del archivo a asignar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
