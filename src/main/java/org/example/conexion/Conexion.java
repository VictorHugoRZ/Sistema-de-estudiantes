package org.example.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    //Definimos la funcion que realizara la conexion a la base de datos
    public static Connection getConnection() {
        //Definimos la conexion
        Connection conexion = null;
        //Declaramos los datos que vamos a usar
        var baseDatos = "estudiantes_db";
        var url = "jdbc:mysql://localhost:3306/" + baseDatos;
        var usuario = "root";
        var password = "admin";

        try {
            //Cargamos la clase del driver de MySQL en memoria
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Realizamos la conexion con la base de datos
            conexion = DriverManager.getConnection(url, usuario, password);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Ocurrio un error en la conexion a la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    //Definimos el metodo main para comprobar que la conexion con la base de datos se
    //realiza de manera correcta
    public static void main(String[] args) {
        //Accedemos a la clase Conexion y a su metodo getConnection, lineas 7 y 10
        var conexion = Conexion.getConnection();
        //Comprobamos si la conexion existe
        if(conexion != null) {
            System.out.println("Conexion exitosa con la base de datos: " + conexion);
        } else {
            System.out.println("Error al conectarse a la base de datos");
        }
    }
}
