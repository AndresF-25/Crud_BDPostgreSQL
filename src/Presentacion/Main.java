package Presentacion;

import Datos.Conexion;

/**
 *
 * @author omard
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static Conexion hc;

    public static void main(String[] args) {
        // TODO code application logic here

        try {
            hc = new Conexion();
            System.out.println("Conectado!");
            UI_Principal principal = new UI_Principal();
            principal.setVisible(true);

        } catch (Exception e) {
            System.out.println("Error al iniciar: " + e);
        }

    }

}
