/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.MetodoPago;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagoDAO {

    // Consultar todos los métodos de pago activos
    public List<MetodoPago> listarActivos() {

        List<MetodoPago> metodos = new ArrayList<>();

        String sql = "SELECT * FROM metodos_pago WHERE activo = TRUE";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                MetodoPago metodo = new MetodoPago();

                metodo.setIdMetodoPago(
                        resultado.getInt("id_metodo_pago"));

                metodo.setNombre(
                        resultado.getString("nombre"));

                metodo.setDescripcion(
                        resultado.getString("descripcion"));

                metodo.setActivo(
                        resultado.getBoolean("activo"));

                metodos.add(metodo);
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar métodos de pago: "
                    + e.getMessage());
        }

        return metodos;
    }

    // Buscar método de pago por identificador
    public MetodoPago buscarPorId(int idMetodoPago) {

        String sql = "SELECT * FROM metodos_pago "
                + "WHERE id_metodo_pago = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idMetodoPago);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {

                    MetodoPago metodo = new MetodoPago();

                    metodo.setIdMetodoPago(
                            resultado.getInt("id_metodo_pago"));

                    metodo.setNombre(
                            resultado.getString("nombre"));

                    metodo.setDescripcion(
                            resultado.getString("descripcion"));

                    metodo.setActivo(
                            resultado.getBoolean("activo"));

                    return metodo;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar método de pago: "
                    + e.getMessage());
        }

        return null;
    }
}
