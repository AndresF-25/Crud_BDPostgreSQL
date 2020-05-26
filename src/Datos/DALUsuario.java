package Datos;

import Presentacion.Main;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DALUsuario {

    Conexion conn = Main.hc;
    cargaDatos c = new cargaDatos();

    public void mostrarLista(DefaultTableModel model, JTable tabla) {
        try {
            String sql = "select idusuario, cedula, nombre, apellido, correo "
                    + "from usuarios where estado=1 order by nombre asc";

            ResultSet rs = conn.ejecutarSQLSelect(sql);
            c.cargarTabla(5, rs, model, tabla);

        } catch (Exception e) {
            System.out.println("Error al cargar tabla dal: " + e);
        }
    }

    public void buscarLista(DefaultTableModel model, JTable tabla, String dato) {
        try {
            String sql = "select idusuario, cedula, nombre, apellido, correo\n"
                    + "from usuarios\n"
                    + "where estado=1 and (nombre like '%" + dato + "%' or apellido like '%" + dato + "%')"
                    + "order by nombre asc";

            ResultSet rs = conn.ejecutarSQLSelect(sql);
            c.cargarTabla(5, rs, model, tabla);

        } catch (Exception e) {
            System.out.println("Error al cargar tabla dal: " + e);
        }
    }

    public void insertarDatos(Usuarios u) {

        try {
            String sql = "INSERT INTO public.usuarios"
                    + "( cedula, nombre, apellido, correo, telefono,usuario, clave, fecha, foto)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, '" + u.getFecha() + "', ?);";

            PreparedStatement ps = conn.conn.prepareStatement(sql);
            ps.setString(1, u.getCedula());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getUsuario());
            ps.setString(7, u.getClave());
            ps.setBinaryStream(8, u.getFis(), u.getLongitudBytes());

            boolean ejecucion = conn.ejecutarSQL(ps);

            if (ejecucion == true) {
                JOptionPane.showMessageDialog(null, "Usuarrio registrado con exito");
            } else if (ejecucion == false) {
                JOptionPane.showMessageDialog(null, "Error al registrar usuario");
            }

        } catch (Exception e) {
            System.out.println("Error al insertar: " + e);
        }

    }

    public void modificarDatosSinFoto(Usuarios u) {

        try {
            String sql = "UPDATE public.usuarios "
                    + "SET  cedula=?, nombre=?, apellido=?, correo=?, telefono=?, usuario=?, clave=?, fecha='" + u.getFecha() + "' \n"
                    + "	WHERE idusuario=?;";

            PreparedStatement ps = conn.conn.prepareStatement(sql);
            ps.setString(1, u.getCedula());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getUsuario());
            ps.setString(7, u.getClave());
//            ps.setBinaryStream(8, u.getFis(), u.getLongitudBytes());
            ps.setInt(8, u.getIdusuario());

            boolean ejecucion = conn.ejecutarSQL(ps);

            if (ejecucion == true) {
                JOptionPane.showMessageDialog(null, "Usuarrio actializado con exito");
            } else if (ejecucion == false) {
                JOptionPane.showMessageDialog(null, "Error al actualizado usuario");
            }

        } catch (Exception e) {
            System.out.println("Error al modificar sin foto: " + e);
        }

    }

    public void modificarDatosConFoto(Usuarios u) {

        try {
            String sql = "UPDATE public.usuarios \n"
                    + "	SET cedula=?, nombre=?, apellido=?, correo=?, telefono=?, usuario=?, clave=?, fecha='" + u.getFecha() + "', foto=?\n"
                    + "	WHERE idusuario=?";

            PreparedStatement ps = conn.conn.prepareStatement(sql);
            ps.setString(1, u.getCedula());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getUsuario());
            ps.setString(7, u.getClave());
            ps.setBinaryStream(8, u.getFis(), u.getLongitudBytes());
            ps.setInt(9, u.getIdusuario());

            boolean ejecucion = conn.ejecutarSQL(ps);

            if (ejecucion == true) {
                JOptionPane.showMessageDialog(null, "Usuarrio actualizado con exito");
            } else if (ejecucion == false) {
                JOptionPane.showMessageDialog(null, "Error al actializado usuario");
            }

        } catch (Exception e) {
            System.out.println("Error al modificar sin foto: " + e);
        }

    }

    public Object[] consultarPorId(int id, JLabel jl_foto) {

        Object[] datos = new Object[9];
        ImageIcon foto;
        InputStream is;

        try {
            String sql = "SELECT cedula, nombre, apellido, correo, telefono, usuario, clave, fecha, foto\n"
                    + "	FROM public.usuarios where idusuario = ?";

            PreparedStatement ps = conn.conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                datos[0] = rs.getString(1);//cedula
                datos[1] = rs.getString(2);//nombre
                datos[2] = rs.getString(3);//apellido
                datos[3] = rs.getString(4);//correo
                datos[4] = rs.getString(5);//telefono
                datos[5] = rs.getString(6);//usuario
                datos[6] = rs.getString(7);//clave
                datos[7] = rs.getDate(8);//fecha
                is = rs.getBinaryStream(9);//foto

                BufferedImage bi = ImageIO.read(is);
                foto = new ImageIcon(bi);
                Image img = foto.getImage();
                Image newimg = img.getScaledInstance(jl_foto.getWidth(), jl_foto.getHeight(),
                        java.awt.Image.SCALE_SMOOTH);
                ImageIcon newico = new ImageIcon(newimg);
                datos[8] = newico;

            }
        } catch (Exception e) {
            System.out.println("Error al coonsultar: " + e);
        }
        return datos;
    }

    public void eliminarDatos(Usuarios u) {

        try {
            String sql = "UPDATE public.usuarios SET  estado=0 WHERE idusuario=?;";

            PreparedStatement ps = conn.conn.prepareStatement(sql);
            ps.setInt(1, u.getIdusuario());
            
            boolean ejecucion = conn.ejecutarSQL(ps);

            if (ejecucion == true) {
                JOptionPane.showMessageDialog(null, "Usuarrio actializado con exito");
            } else if (ejecucion == false) {
                JOptionPane.showMessageDialog(null, "Error al actualizado usuario");
            }

        } catch (Exception e) {
            System.out.println("Error al modificar sin foto: " + e);
        }

    }
}
