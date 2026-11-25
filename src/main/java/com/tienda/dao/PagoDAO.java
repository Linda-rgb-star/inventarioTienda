/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.Pago;
import com.tienda.model.Pedido;
import com.tienda.model.MetodoPago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    // Registrar un pago con conexión independiente
    public boolean registrar(Pago pago) {

        if (!datosValidos(pago)) {
            System.out.println("Los datos del pago no son válidos.");
            return false;
        }

        try (Connection conexion = ConexionBD.conectarBD()) {
            return registrar(conexion, pago);

        } catch (SQLException e) {
            System.out.println("Error al registrar pago: "
                    + e.getMessage());
            return false;
        }
    }

    // Registrar un pago utilizando una conexión externa
    public boolean registrar(Connection conexion, Pago pago) {

        if (!datosValidos(pago)) {
            return false;
        }

        String sql = "INSERT INTO pagos "
                + "(id_pedido, id_metodo_pago, monto, fecha_pago, "
                + "estado, referencia) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement consulta = conexion.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            consulta.setInt(1, pago.getPedido().getIdPedido());
            consulta.setInt(2, pago.getMetodoPago().getIdMetodoPago());
            consulta.setBigDecimal(3, pago.getMonto());
            consulta.setTimestamp(4,
                    Timestamp.valueOf(pago.getFechaPago()));
            consulta.setString(5, pago.getEstado());
            consulta.setString(6, pago.getReferencia());

            int filas = consulta.executeUpdate();

            if (filas > 0) {

                try (ResultSet resultado = consulta.getGeneratedKeys()) {

                    if (resultado.next()) {
                        pago.setIdPago(resultado.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar pago: "
                    + e.getMessage());
        }

        return false;
    }

    // Validar información del pago
    private boolean datosValidos(Pago pago) {

        return pago != null
                && pago.getPedido() != null
                && pago.getPedido().getIdPedido() > 0
                && pago.getMetodoPago() != null
                && pago.getMetodoPago().getIdMetodoPago() > 0
                && pago.getMonto() != null
                && pago.getMonto().signum() > 0
                && pago.getFechaPago() != null
                && pago.getEstado() != null;
    }

    // Consultar los pagos de un pedido
    public List<Pago> listarPorPedido(int idPedido) {

        List<Pago> pagos = new ArrayList<>();

        String sql = "SELECT * FROM pagos WHERE id_pedido = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idPedido);

            try (ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {
                    pagos.add(convertirPago(resultado));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar pagos: "
                    + e.getMessage());
        }

        return pagos;
    }

    // Actualizar el estado de un pago
    public boolean actualizarEstado(int idPago, String estado) {

        if (idPago <= 0 || estado == null || estado.isBlank()) {
            return false;
        }

        String sql = "UPDATE pagos SET estado = ? WHERE id_pago = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, estado);
            consulta.setInt(2, idPago);

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar pago: "
                    + e.getMessage());
            return false;
        }
    }

    // Convertir registro de MySQL en objeto Pago
    private Pago convertirPago(ResultSet resultado) throws SQLException {

        Pedido pedido = new Pedido();
        pedido.setIdPedido(resultado.getInt("id_pedido"));

        MetodoPago metodo = new MetodoPago();
        metodo.setIdMetodoPago(
                resultado.getInt("id_metodo_pago"));

        Pago pago = new Pago();

        pago.setIdPago(resultado.getInt("id_pago"));
        pago.setPedido(pedido);
        pago.setMetodoPago(metodo);
        pago.setMonto(resultado.getBigDecimal("monto"));

        Timestamp fecha = resultado.getTimestamp("fecha_pago");

        if (fecha != null) {
            pago.setFechaPago(fecha.toLocalDateTime());
        }

        pago.setEstado(resultado.getString("estado"));
        pago.setReferencia(resultado.getString("referencia"));

        return pago;
    }
}
