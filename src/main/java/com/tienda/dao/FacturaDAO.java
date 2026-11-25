/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.Factura;
import com.tienda.model.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    // Registrar una factura
    public boolean registrar(Factura factura) {

        String sql = "INSERT INTO facturas "
                + "(id_pedido, numero_factura, fecha_emision, total) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            consulta.setInt(1, factura.getPedido().getIdPedido());
            consulta.setString(2, factura.getNumeroFactura());
            consulta.setTimestamp(3,
                    Timestamp.valueOf(factura.getFechaEmision()));
            consulta.setBigDecimal(4, factura.getTotal());

            int filas = consulta.executeUpdate();

            if (filas > 0) {

                try (ResultSet resultado = consulta.getGeneratedKeys()) {

                    if (resultado.next()) {
                        factura.setIdFactura(resultado.getInt(1));
                    }
                }

                // El estado se maneja en Java, no en MySQL
                factura.setEstado("EMITIDA");

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar factura: "
                    + e.getMessage());
        }

        return false;
    }

    // Buscar factura por pedido
    public Factura buscarPorPedido(int idPedido) {

        String sql = "SELECT * FROM facturas WHERE id_pedido = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idPedido);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    return convertirFactura(resultado);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar factura: "
                    + e.getMessage());
        }

        return null;
    }

    // Consultar todas las facturas
    public List<Factura> listarTodas() {

        List<Factura> facturas = new ArrayList<>();

        String sql = "SELECT * FROM facturas ORDER BY fecha_emision DESC";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                facturas.add(convertirFactura(resultado));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar facturas: "
                    + e.getMessage());
        }

        return facturas;
    }

    // Convertir registro de MySQL en objeto Factura
    private Factura convertirFactura(ResultSet resultado)
            throws SQLException {

        Pedido pedido = new Pedido();
        pedido.setIdPedido(resultado.getInt("id_pedido"));

        Factura factura = new Factura();

        factura.setIdFactura(resultado.getInt("id_factura"));
        factura.setPedido(pedido);
        factura.setNumeroFactura(
                resultado.getString("numero_factura"));

        Timestamp fecha = resultado.getTimestamp("fecha_emision");

        if (fecha != null) {
            factura.setFechaEmision(fecha.toLocalDateTime());
        }

        factura.setTotal(resultado.getBigDecimal("total"));

        // La tabla no almacena el estado.
        factura.setEstado("EMITIDA");

        return factura;
    }
}
