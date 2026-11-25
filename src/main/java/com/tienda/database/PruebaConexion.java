/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/


package com.tienda.database;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {

        try (Connection conexion = ConexionBD.conectarServidor()) {

            System.out.println("================================");
            System.out.println(" CONEXION CON MYSQL EXITOSA");
            System.out.println("================================");

            System.out.println("Servidor: "
                    + conexion.getMetaData().getDatabaseProductName());

            System.out.println("Version: "
                    + conexion.getMetaData().getDatabaseProductVersion());

        } catch (SQLException e) {

            System.out.println("Error al conectar con MySQL:");
            System.out.println(e.getMessage());

        }
    }
}
