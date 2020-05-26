package Datos;

import java.io.Serializable;
import java.sql.*;

public class Conexion implements Serializable {

    public Connection conn = null;

    public Conexion() {

        conn = Conexion.realizaConexion();

    }

    public Connection getConn() {
        return conn;
    }

    public static Connection realizaConexion() {

        Connection c = null;

        try {

            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Crud_BDPostgreSQL", "postgres", "Desarrollo");
            System.out.println("Conexion del driver correcta!");
        } catch (SQLException e) {
            System.out.println("Error 1: " + e);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error 2: " + ex);
        }

        return c;
    }

    public boolean ejecutarSQL(PreparedStatement sentencia) {

        try {
            sentencia.execute();
            sentencia.close();
            return true;

        } catch (Exception e) {
            System.out.println("Error al ejecutar: " + e);
            return false;
        }
    }

    public ResultSet ejecutarSQLSelect(String sql) {

        ResultSet resultado;
        try {
            PreparedStatement sentensia = conn.prepareStatement(sql);
            resultado = sentensia.executeQuery();
            return resultado;
        } catch (SQLException e) {
            System.out.println("Error al ejecutar consulta: " + e);
            return null;
        }
    }

}
