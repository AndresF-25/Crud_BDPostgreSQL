
package Control;

import Datos.DALUsuario;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class BLLUsuario {
    
    DALUsuario dal = new DALUsuario();
    
    public void mostrarLista(DefaultTableModel model,JTable tabla){
        dal.mostrarLista(model, tabla);
    }
    
}
