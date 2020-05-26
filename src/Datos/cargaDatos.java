package Datos;

import java.sql.ResultSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class cargaDatos {

    public void cargarTabla(int columnas, ResultSet rs, DefaultTableModel model, JTable tabla) {

        try {
            Object[] filas = new Object[columnas];
            //Recorremos filas
            while (rs.next()) {
                //Recorremos columnas
                for (int i = 0; i < columnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }
                model.addRow(filas);
            }
            tabla.updateUI();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error al cargar tabla: " + e);
        }

    }

}
