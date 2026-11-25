/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.Carrito;
import com.tienda.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CarritoDAO {

    // Crear un carrito para un cliente
    public boolean crear(Carrito carrito) {

        String sql = "INSERT INTO carritos "
                + "(id_cliente, fecha_creacion, estado) "
                + "VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            consulta.setInt(1, carrito.getCliente().getIdCliente());
            consulta.setTimestamp(2,
                    Timestamp.valueOf(carrito.getFechaCreacion()));
            consulta.setString(3, carrito.getEstado());

            int filas = consulta.executeUpdate();

            if (filas > 0) {
                try (ResultSet resultado = consulta.getGeneratedKeys()) {
                    if (resultado.next()) {
                        carrito.setIdCarrito(resultado.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al crear carrito: " + e.getMessage());
        }

        return false;
    }

    // Consultar el carrito activo de un cliente
    public Carrito buscarActivoPorCliente(int idCliente) {

        String sql = "SELECT * FROM carritos "
                + "WHERE id_cliente = ? AND estado = 'ACTIVO' "
                + "ORDER BY id_carrito DESC LIMIT 1";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idCliente);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {

                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(resultado.getInt("id_cliente"));

                    Carrito carrito = new Carrito();

                    carrito.setIdCarrito(resultado.getInt("id_carrito"));
                    carrito.setCliente(cliente);

                    Timestamp fecha = resultado.getTimestamp("fecha_creacion");

                    if (fecha != null) {
                        carrito.setFechaCreacion(fecha.toLocalDateTime());
                    }

                    carrito.setEstado(resultado.getString("estado"));

                    return carrito;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar carrito: " + e.getMessage());
        }

        return null;
    }

    // Cambiar el estado del carrito
    public boolean actualizarEstado(int idCarrito, String estado) {

        String sql = "UPDATE carritos SET estado = ? "
                + "WHERE id_carrito = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, estado);
            consulta.setInt(2, idCarrito);

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar carrito: " + e.getMessage());
            return false;
        }
    }
}
