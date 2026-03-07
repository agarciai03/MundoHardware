 package es.mundohardware.dao;

import es.mundohardware.beans.Usuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementación de la interfaz IUsuarioDAO para acceso a datos en MySQL.
 * Facilita el inicio de sesión, alta de usuarios y actualización de datos
 * protegiendo contra inyecciones SQL mediante consultas preparadas.
 *
 * * @author Alberto García Izquierdo
 * @version 1.0
 */
public class MySQLUsuarioDAO implements IUsuarioDAO {

    @Override
    public Usuarios login(String email, String password) {
        Usuarios u = null;
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";
        Connection con = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuarios temp = new Usuarios();
                temp.setIdUsuario(rs.getInt("idusuario"));
                temp.setEmail(rs.getString("email"));
                temp.setPassword(rs.getString("password"));
                temp.setNombre(rs.getString("nombre"));
                temp.setApellidos(rs.getString("apellidos"));
                temp.setNif(rs.getString("nif"));
                temp.setTelefono(rs.getString("telefono"));
                temp.setDireccion(rs.getString("direccion"));

                temp.setCodigoPostal(rs.getString("codigo_postal"));
                temp.setLocalidad(rs.getString("localidad"));
                temp.setProvincia(rs.getString("provincia"));
                temp.setAvatar(rs.getString("avatar"));
                temp.setUltimoAcceso(rs.getTimestamp("ultimo_acceso"));

                u = temp; // pasamos los datos
            }
        } catch (SQLException e) {
            System.err.println("ERROR SQL en login: " + e.getMessage());
            u = null;
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return u;
    }

    @Override
    public int registrar(Usuarios u) {
        int idGenerado = -1;
        String sql = "INSERT INTO usuarios (email, password, nombre, apellidos, nif, telefono, direccion, codigo_postal, localidad, provincia, avatar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getNombre());
            ps.setString(4, u.getApellidos());
            ps.setString(5, u.getNif());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getDireccion());
            ps.setString(8, u.getCodigoPostal());
            ps.setString(9, u.getLocalidad());
            ps.setString(10, u.getProvincia());
            ps.setString(11, "default.png"); // Ponemos avatar por defecto 

            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR SQL al registrar: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return idGenerado;
    }

    @Override
    public boolean existeEmail(String email) {
        boolean existe = false;
        String sql = "SELECT idusuario FROM usuarios WHERE email = ?";
        Connection con = ConnectionFactory.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    existe = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return existe;
    }

    @Override
    public void actualizarAvatar(int idUsuario, String nombreArchivo) {
        String sql = "UPDATE usuarios SET avatar = ? WHERE idusuario = ?";
        Connection con = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombreArchivo);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void actualizar(Usuarios u) {
        String sql = "UPDATE usuarios SET nombre=?, apellidos=?, telefono=?, direccion=?, codigo_postal=?, localidad=?, provincia=?, password=?, avatar=? WHERE idusuario=?";
        Connection con = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellidos());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getDireccion());
            ps.setString(5, u.getCodigoPostal());
            ps.setString(6, u.getLocalidad());
            ps.setString(7, u.getProvincia());
            ps.setString(8, u.getPassword());
            ps.setString(9, u.getAvatar());
            ps.setInt(10, u.getIdUsuario());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("ERROR SQL al actualizar: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public boolean borrar(int idUsuario) {
        boolean borrado = false;
        String sql = "DELETE FROM usuarios WHERE idusuario = ?";
        Connection con = ConnectionFactory.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            borrado = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return borrado;
    }

    @Override
    public void registrarUltimoAcceso(int idUsuario) {
        String sql = "UPDATE usuarios SET ultimo_acceso = NOW() WHERE idusuario = ?";
        Connection con = ConnectionFactory.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    private Usuarios mapUsuario(ResultSet rs) throws SQLException {
        Usuarios u = new Usuarios();
        u.setIdUsuario(rs.getInt("idusuario"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setNombre(rs.getString("nombre"));
        u.setApellidos(rs.getString("apellidos"));
        u.setNif(rs.getString("nif"));
        u.setTelefono(rs.getString("telefono"));
        u.setDireccion(rs.getString("direccion"));
        u.setCodigoPostal(rs.getString("codigo_postal"));
        u.setLocalidad(rs.getString("localidad"));
        u.setProvincia(rs.getString("provincia"));
        u.setAvatar(rs.getString("avatar"));
        u.setUltimoAcceso(rs.getTimestamp("ultimo_acceso"));
        return u;
    }
}
