/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.NotificacionStock;
import com.tienda.model.Cliente;
import com.tienda.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificacionStockDAO {

    // Registrar un cliente interesado en un producto agotado
    public boolean registrar(NotificacionStock notificacion) {

        String validar = "SELECT stock FROM productos WHERE id_producto = ?";

        String sql = "INSERT INTO notificaciones_stock "
                + "(id_cliente, id_producto, estado) "
                + "VALUES (?, ?, 'PENDIENTE')";

        try (Connection conexion = ConexionBD.conectarBD()) {

            // Comprobar que el producto exista y esté agotado
            try (PreparedStatement consulta =
                    conexion.prepareStatement(validar)) {

                consulta.setInt(1,
                        notificacion.getProducto().getIdProducto());

                try (ResultSet resultado = consulta.executeQuery()) {

                    if (!resultado.next()
                            || resultado.getInt("stock") > 0) {

                        System.out.println(
                                "El producto no está agotado o no existe.");
                        return false;
                    }
                }
            }

            // Registrar la solicitud
            try (PreparedStatement consulta =
                    conexion.prepareStatement(sql)) {

                consulta.setInt(1,
                        notificacion.getCliente().getIdCliente());

                consulta.setInt(2,
                        notificacion.getProducto().getIdProducto());

                return consulta.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al registrar notificación: " + e.getMessage());
            return false;
        }
    }

    // Consultar solicitudes pendientes
    public List<NotificacionStock> listarPendientesPorProducto(
            int idProducto) {

        List<NotificacionStock> notificaciones = new ArrayList<>();

        String sql = "SELECT * FROM notificaciones_stock "
                + "WHERE id_producto = ? AND estado = 'PENDIENTE'";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idProducto);

            try (ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {

                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(
                            resultado.getInt("id_cliente"));

                    Producto producto = new Producto();
                    producto.setIdProducto(
                            resultado.getInt("id_producto"));

                    NotificacionStock notificacion =
                            new NotificacionStock();

                    notificacion.setIdNotificacion(
                            resultado.getInt("id_notificacion"));

                    notificacion.setCliente(cliente);
                    notificacion.setProducto(producto);

                    Timestamp fecha =
                            resultado.getTimestamp("fecha_solicitud");

                    if (fecha != null) {
                        notificacion.setFechaSolicitud(
                                fecha.toLocalDateTime());
                    }

                    notificacion.setNotificado(false);

                    notificaciones.add(notificacion);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al consultar notificaciones: " + e.getMessage());
        }

        return notificaciones;
    }

    // Marcar solicitud como notificada
    public boolean marcarComoNotificado(int idNotificacion) {

        String sql = "UPDATE notificaciones_stock "
                + "SET estado = 'NOTIFICADO', "
                + "fecha_notificacion = CURRENT_TIMESTAMP "
                + "WHERE id_notificacion = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idNotificacion);

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Error al actualizar notificación: " + e.getMessage());
            return false;
        }
    }
}
