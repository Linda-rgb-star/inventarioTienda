/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.Pedido;
import com.tienda.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    // Registrar un pedido
    public boolean registrar(Pedido pedido) {

        String sql = "INSERT INTO pedidos "
                + "(id_cliente, fecha_pedido, estado, subtotal, "
                + "total_descuentos, total, direccion_entrega) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            consulta.setInt(1, pedido.getCliente().getIdCliente());
            consulta.setTimestamp(2,
                    Timestamp.valueOf(pedido.getFechaPedido()));
            consulta.setString(3, pedido.getEstado());
            consulta.setBigDecimal(4, pedido.getSubtotal());
            consulta.setBigDecimal(5, pedido.getTotalDescuentos());
            consulta.setBigDecimal(6, pedido.getTotal());
            consulta.setString(7, pedido.getDireccionEntrega());

            int filas = consulta.executeUpdate();

            if (filas > 0) {
                try (ResultSet resultado = consulta.getGeneratedKeys()) {
                    if (resultado.next()) {
                        pedido.setIdPedido(resultado.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar pedido: " + e.getMessage());
        }

        return false;
    }

    // Consultar el historial de pedidos de un cliente
    public List<Pedido> listarPorCliente(int idCliente) {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT * FROM pedidos "
                + "WHERE id_cliente = ? ORDER BY fecha_pedido DESC";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idCliente);

            try (ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {
                    pedidos.add(convertirPedido(resultado));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar pedidos: " + e.getMessage());
        }

        return pedidos;
    }

    // Buscar un pedido por su identificador
    public Pedido buscarPorId(int idPedido) {

        String sql = "SELECT * FROM pedidos WHERE id_pedido = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idPedido);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    return convertirPedido(resultado);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar pedido: " + e.getMessage());
        }

        return null;
    }

    // Actualizar el estado de un pedido
    public boolean actualizarEstado(int idPedido, String estado) {

        String sql = "UPDATE pedidos SET estado = ? "
                + "WHERE id_pedido = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, estado);
            consulta.setInt(2, idPedido);

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar pedido: " + e.getMessage());
            return false;
        }
    }

    // Convertir registro de MySQL en objeto Pedido
    private Pedido convertirPedido(ResultSet resultado)
            throws SQLException {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(resultado.getInt("id_cliente"));

        Pedido pedido = new Pedido();

        pedido.setIdPedido(resultado.getInt("id_pedido"));
        pedido.setCliente(cliente);

        Timestamp fecha = resultado.getTimestamp("fecha_pedido");

        if (fecha != null) {
            pedido.setFechaPedido(fecha.toLocalDateTime());
        }

        pedido.setEstado(resultado.getString("estado"));
        pedido.setSubtotal(resultado.getBigDecimal("subtotal"));
        pedido.setTotalDescuentos(
                resultado.getBigDecimal("total_descuentos"));
        pedido.setTotal(resultado.getBigDecimal("total"));
        pedido.setDireccionEntrega(
                resultado.getString("direccion_entrega"));

        return pedido;
    }
    // Registrar pedido usando una conexión transaccional
public boolean registrar(Connection conexion, Pedido pedido) {

    String sql = "INSERT INTO pedidos "
            + "(id_cliente, fecha_pedido, estado, subtotal, "
            + "total_descuentos, total, direccion_entrega) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement consulta = conexion.prepareStatement(
            sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

        consulta.setInt(1, pedido.getCliente().getIdCliente());
        consulta.setTimestamp(2,
                java.sql.Timestamp.valueOf(pedido.getFechaPedido()));
        consulta.setString(3, pedido.getEstado());
        consulta.setBigDecimal(4, pedido.getSubtotal());
        consulta.setBigDecimal(5, pedido.getTotalDescuentos());
        consulta.setBigDecimal(6, pedido.getTotal());
        consulta.setString(7, pedido.getDireccionEntrega());

        int filas = consulta.executeUpdate();

        if (filas > 0) {
            try (ResultSet resultado = consulta.getGeneratedKeys()) {
                if (resultado.next()) {
                    pedido.setIdPedido(resultado.getInt(1));
                    return true;
                }
            }
        }

    } catch (SQLException e) {
        System.out.println("Error al registrar pedido: "
                + e.getMessage());
    }

    return false;
}
}
