/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.Descuento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DescuentoDAO {

    // Registrar un descuento
    public boolean registrar(Descuento descuento) {

        String sql = "INSERT INTO descuentos "
                + "(nombre, tipo, valor, fecha_inicio, fecha_fin, "
                + "acumulable, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, descuento.getNombre());
            consulta.setString(2, descuento.getTipo());
            consulta.setBigDecimal(3, descuento.getValor());
            consulta.setDate(4, Date.valueOf(descuento.getFechaInicio()));
            consulta.setDate(5, Date.valueOf(descuento.getFechaFin()));
            consulta.setBoolean(6, descuento.isAcumulable());
            consulta.setBoolean(7, descuento.isActivo());

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar descuento: " + e.getMessage());
            return false;
        }
    }

    // Consultar todos los descuentos
    public List<Descuento> listarTodos() {

        List<Descuento> descuentos = new ArrayList<>();

        String sql = "SELECT * FROM descuentos";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                descuentos.add(convertirDescuento(resultado));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar descuentos: " + e.getMessage());
        }

        return descuentos;
    }

    // Consultar descuentos activos
    public List<Descuento> listarActivos() {

        List<Descuento> descuentos = new ArrayList<>();

        String sql = "SELECT * FROM descuentos WHERE activo = TRUE";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                descuentos.add(convertirDescuento(resultado));
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar descuentos activos: "
                    + e.getMessage());
        }

        return descuentos;
    }

    // Buscar descuento por identificador
    public Descuento buscarPorId(int idDescuento) {

        String sql = "SELECT * FROM descuentos WHERE id_descuento = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idDescuento);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    return convertirDescuento(resultado);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar descuento: " + e.getMessage());
        }

        return null;
    }

    // Convertir registro de MySQL en objeto Java
    private Descuento convertirDescuento(ResultSet resultado)
            throws SQLException {

        Descuento descuento = new Descuento();

        descuento.setIdDescuento(resultado.getInt("id_descuento"));
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

        descuento.setAcumulable(resultado.getBoolean("acumulable"));
        descuento.setActivo(resultado.getBoolean("activo"));

        return descuento;
    }
}
