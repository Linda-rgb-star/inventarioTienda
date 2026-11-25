/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */




package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.DetallePedido;
import com.tienda.model.Producto;
import com.tienda.model.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoDAO {

    // Registrar un producto dentro de un pedido
    public boolean registrar(DetallePedido detalle) {

        try (Connection conexion = ConexionBD.conectarBD()) {
            return registrar(conexion, detalle);

        } catch (SQLException e) {
            System.out.println(
                    "Error al registrar detalle del pedido: "
                    + e.getMessage()
            );
            return false;
        }
    }

    // Registrar un detalle utilizando una conexión externa
    // Permite trabajar dentro de una transacción
    public boolean registrar(
            Connection conexion,
            DetallePedido detalle) {

        String sql = "INSERT INTO detalle_pedido "
                + "(id_pedido, id_producto, nombre_producto, cantidad, "
                + "precio_unitario, subtotal) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        if (conexion == null || detalle == null
                || detalle.getPedido() == null
                || detalle.getProducto() == null) {
            return false;
        }

        try (PreparedStatement consulta = conexion.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            consulta.setInt(
                    1, detalle.getPedido().getIdPedido()
            );
            consulta.setInt(
                    2, detalle.getProducto().getIdProducto()
            );
            consulta.setString(
                    3, detalle.getNombreProducto()
            );
            consulta.setInt(
                    4, detalle.getCantidad()
            );
            consulta.setBigDecimal(
                    5, detalle.getPrecioUnitario()
            );
            consulta.setBigDecimal(
                    6, detalle.getSubtotal()
            );

            int filas = consulta.executeUpdate();

            if (filas > 0) {

                try (ResultSet resultado = consulta.getGeneratedKeys()) {

                    if (resultado.next()) {
                        detalle.setIdDetalle(resultado.getInt(1));
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al registrar detalle del pedido: "
                    + e.getMessage()
            );
        }

        return false;
    }

    // Consultar los productos asociados a un pedido
    public List<DetallePedido> listarPorPedido(int idPedido) {

        List<DetallePedido> detalles = new ArrayList<>();

        String sql = "SELECT * FROM detalle_pedido "
                + "WHERE id_pedido = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idPedido);

            try (ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {

                    Pedido pedido = new Pedido();
                    pedido.setIdPedido(
                            resultado.getInt("id_pedido")
                    );

                    Producto producto = new Producto();
                    producto.setIdProducto(
                            resultado.getInt("id_producto")
                    );

                    DetallePedido detalle = new DetallePedido();

                    detalle.setIdDetalle(
                            resultado.getInt("id_detalle_pedido")
                    );
                    detalle.setPedido(pedido);
                    detalle.setProducto(producto);
                    detalle.setNombreProducto(
                            resultado.getString("nombre_producto")
                    );
                    detalle.setCantidad(
                            resultado.getInt("cantidad")
                    );
                    detalle.setPrecioUnitario(
                            resultado.getBigDecimal("precio_unitario")
                    );
                    detalle.setSubtotal(
                            resultado.getBigDecimal("subtotal")
                    );

                    detalles.add(detalle);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al consultar detalles del pedido: "
                    + e.getMessage()
            );
        }

        return detalles;
    }
}
