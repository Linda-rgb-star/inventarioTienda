/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {

    private static final String URL_SERVIDOR =
        "jdbc:mysql://localhost:3306/tienda";


private static final String USUARIO = "root";
private static final String CONTRASENA = "";

    public static Connection getConexion() {

        try (Connection conexionServidor =
                     DriverManager.getConnection(
                             URL_SERVIDOR, USUARIO, CONTRASENA);
             Statement sentencia = conexionServidor.createStatement()) {

            sentencia.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS tienda"
                    
            );
                
sentencia.executeUpdate(
    "CREATE TABLE IF NOT EXISTS tienda.categoria ("
    + "id_categoria INT AUTO_INCREMENT PRIMARY KEY, "
    + "nombre VARCHAR(50) NOT NULL UNIQUE)"
);


sentencia.executeUpdate(
    "CREATE TABLE IF NOT EXISTS tienda.producto ("
    + "id_producto INT AUTO_INCREMENT PRIMARY KEY, "
    + "nombre VARCHAR(80) NOT NULL, "
    + "precio DECIMAL(10,2) NOT NULL, "
    + "cantidad INT NOT NULL DEFAULT 0, "
    + "id_categoria INT NOT NULL, "
    + "FOREIGN KEY (id_categoria) "
    + "REFERENCES tienda.categoria(id_categoria))"
);
sentencia.executeUpdate(
    "INSERT IGNORE INTO tienda.categoria (nombre) "
    + "VALUES ('Lacteos'), ('Aseo'), ('Bebidas'), ('Granos')"
);



        } catch (SQLException e) {
            System.out.println(
                    "Error al crear la base de datos: " + e.getMessage()
            );
            return null;
        }

        try {
            return DriverManager.getConnection(
                    URL_SERVIDOR, USUARIO, CONTRASENA
            );

        } catch (SQLException e) {
            System.out.println(
                    "Error al conectar con la base de datos: "
                    + e.getMessage()
            );
            return null;
        }
    }
}
