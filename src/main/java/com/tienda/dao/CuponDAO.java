/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.Cupon;
import com.tienda.model.Descuento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CuponDAO {

    // Registrar un cupón
    public boolean registrar(Cupon cupon) {

        String sql = "INSERT INTO cupones "
                + "(codigo, id_descuento, fecha_expiracion, activo) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, cupon.getCodigo());
            consulta.setInt(2, cupon.getDescuento().getIdDescuento());
            consulta.setDate(3, Date.valueOf(cupon.getFechaExpiracion()));
            consulta.setBoolean(4, cupon.isActivo());

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar cupón: " + e.getMessage());
            return false;
        }
    }

    // Buscar cupón por código
    public Cupon buscarPorCodigo(String codigo) {

        String sql = "SELECT c.*, d.nombre, d.tipo, d.valor, "
                + "d.fecha_inicio, d.fecha_fin, d.acumulable, d.activo "
                + "FROM cupones c "
                + "INNER JOIN descuentos d "
                + "ON c.id_descuento = d.id_descuento "
                + "WHERE c.codigo = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, codigo);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {

                    Descuento descuento = new Descuento();

                    descuento.setIdDescuento(
                            resultado.getInt("id_descuento"));
                    descuento.setNombre(resultado.getString("nombre"));
                    descuento.setTipo(resultado.getString("tipo"));
                    descuento.setValor(resultado.getBigDecimal("valor"));

                    Date inicio = resultado.getDate("fecha_inicio");
                    Date fin = resultado.getDate("fecha_fin");

                    if (inicio != null) {
                        descuento.setFechaInicio(inicio.toLocalDate());
                    }

                    if (fin != null) {
                        descuento.setFechaFin(fin.toLocalDate());
                    }

                    descuento.setAcumulable(
                            resultado.getBoolean("acumulable"));
                    descuento.setActivo(
                            resultado.getBoolean("activo"));

                    Cupon cupon = new Cupon();

                    cupon.setIdCupon(resultado.getInt("id_cupon"));
                    cupon.setCodigo(resultado.getString("codigo"));
                    cupon.setDescuento(descuento);
                    cupon.setFechaExpiracion(
                            resultado.getDate("fecha_expiracion").toLocalDate());
                    cupon.setActivo(resultado.getBoolean("activo"));

                    return cupon;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar cupón: " + e.getMessage());
        }

        return null;
    }

    // Consultar todos los cupones
    public List<Cupon> listarTodos() {

        List<Cupon> cupones = new ArrayList<>();

        String sql = "SELECT codigo FROM cupones";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                Cupon cupon = buscarPorCodigo(resultado.getString("codigo"));

                if (cupon != null) {
                    cupones.add(cupon);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar cupones: " + e.getMessage());
        }

        return cupones;
    }
}