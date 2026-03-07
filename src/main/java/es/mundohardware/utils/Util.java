package es.mundohardware.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase de utilidades generales para la aplicación. Contiene métodos estáticos
 * compartidos como encriptación y validaciones.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public class Util {

    /**
     * Encripta una cadena de texto plana utilizando el algoritmo MD5.
     *
     * @param input La contraseña en texto plano
     * @return La contraseña encriptada en formato hexadecimal
     */
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calcula la letra correspondiente a un número de DNI español.
     *
     * @param dni El número de DNI sin letra
     * @return La letra correspondiente según el módulo 23
     */
    public static char calcularLetraNIF(int dni) {
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        return letras.charAt(dni % 23);
    }
}
