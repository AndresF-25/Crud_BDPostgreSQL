package Datos;

import Presentacion.Main;
import java.sql.ResultSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DALUsuario {

    Conexion conn = Main.hc;
    cargaDatos c = new cargaDatos();
    
    public void mostrarLista(DefaultTableModel model,JTable tabla){
        try {
            String sql="select idusuario, cedula, nombre, apellido, correo from usuarios where estado=1 order by nombre asc";
            ResultSet rs = conn.ejecutarSQLSelect(sql);
            c.cargarTabla(5, rs, model, tabla);
        } catch (Exception e) {
            System.out.println("Error al cargar tabla dal: "+e);
        }
    }

}
