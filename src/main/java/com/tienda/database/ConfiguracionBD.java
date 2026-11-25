/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.database;

public class ConfiguracionBD {

    // Datos del servidor MySQL
    public static final String SERVIDOR = "localhost";
    public static final String PUERTO = "3306";

    // Credenciales de acceso
    public static final String USUARIO = "root";
    public static final String CONTRASENA = "";

    // Nombre de nuestra base de datos
    public static final String NOMBRE_BD = "tienda_online";

    // Conexion al servidor sin seleccionar una base de datos
    public static final String URL_SERVIDOR =
            "jdbc:mysql://" + SERVIDOR + ":" + PUERTO
            + "/?serverTimezone=UTC";

    // Conexion a nuestra base de datos
    public static final String URL_BD =
            "jdbc:mysql://" + SERVIDOR + ":" + PUERTO
            + "/" + NOMBRE_BD + "?serverTimezone=UTC";
}
