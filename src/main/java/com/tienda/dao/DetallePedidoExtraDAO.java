/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.DetallePedidoExtra;
import com.tienda.model.Extra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoExtraDAO {

    // Registrar un extra asociado a un producto del pedido
    public boolean registrar(int idDetallePedido,
            DetallePedidoExtra detalleExtra) {

        try (Connection conexion = ConexionBD.conectarBD()) {
            return registrar(conexion, idDetallePedido, detalleExtra);

        } catch (SQLException e) {
            System.out.println("Error al registrar extra del pedido: "
                    + e.getMessage());
            return false;
        }
    }

    // Registrar un extra utilizando una conexión externa
    public boolean registrar(Connection conexion, int idDetallePedido,
            DetallePedidoExtra detalleExtra) {

        String sql = "INSERT INTO detalle_pedido_extras "
                + "(id_detalle_pedido, id_extra, precio_aplicado) "
                + "VALUES (?, ?, ?)";

        try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idDetallePedido);
            consulta.setInt(2, detalleExtra.getExtra().getIdExtra());
            consulta.setBigDecimal(3, detalleExtra.getPrecioAplicado());

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar extra del pedido: "
                    + e.getMessage());
            return false;
        }
    }

    // Consultar los extras asociados a un producto del pedido
    public List<DetallePedidoExtra> listarPorDetalle(int idDetallePedido) {

        List<DetallePedidoExtra> detalles = new ArrayList<>();

        String sql = "SELECT dpe.*, e.nombre "
                + "FROM detalle_pedido_extras dpe "
                + "INNER JOIN extras e "
                + "ON dpe.id_extra = e.id_extra "
                + "WHERE dpe.id_detalle_pedido = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idDetallePedido);

            try (ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {

                    Extra extra = new Extra();
                    extra.setIdExtra(resultado.getInt("id_extra"));
                    extra.setNombre(resultado.getString("nombre"));

                    DetallePedidoExtra detalle = new DetallePedidoExtra();

                    detalle.setIdDetallePedido(
                            resultado.getInt("id_detalle_pedido"));
                    detalle.setExtra(extra);
                    detalle.setPrecioAplicado(
                            resultado.getBigDecimal("precio_aplicado"));

                    detalles.add(detalle);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar extras del pedido: "
                    + e.getMessage());
        }

        return detalles;
    }
}
