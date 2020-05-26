
package Control;

import Datos.DALUsuario;
import Datos.Usuarios;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class BLLUsuario {
    
    DALUsuario dal = new DALUsuario();
    
    public void mostrarLista(DefaultTableModel model,JTable tabla){
        dal.mostrarLista(model, tabla);
    }
    
    public void insertarDatos(Usuarios u){
        dal.insertarDatos(u);
    }
    
    public void modificarDatosSinFoto(Usuarios u){
        dal.modificarDatosSinFoto(u);
    }
    
    public void modificarDatosConFoto(Usuarios u){
        dal.modificarDatosConFoto(u);
    }
    
    public void buscarLista(DefaultTableModel model, JTable tabla, String dato){
        dal.buscarLista(model, tabla, dato);
    }
    
    public Object[] consultarPorId(int id, JLabel jl_foto){
        return dal.consultarPorId(id, jl_foto);
    }
    
    public void eliminarDatos(Usuarios u){
        dal.eliminarDatos(u);
    }
}
