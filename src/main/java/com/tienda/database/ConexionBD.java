/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Conexion al servidor MySQL
    public static Connection conectarServidor() throws SQLException {

        return DriverManager.getConnection(
                ConfiguracionBD.URL_SERVIDOR,
                ConfiguracionBD.USUARIO,
                ConfiguracionBD.CONTRASENA
        );
    }

    // Conexion a la base de datos de la tienda
    public static Connection conectarBD() throws SQLException {

        return DriverManager.getConnection(
                ConfiguracionBD.URL_BD,
                ConfiguracionBD.USUARIO,
                ConfiguracionBD.CONTRASENA
        );
    }
    // Obtener una conexión para manejar transacciones
public static Connection conectarBDTransaccional() throws SQLException {
    Connection conexion = conectarBD();
    conexion.setAutoCommit(false);
    return conexion;
}
}
