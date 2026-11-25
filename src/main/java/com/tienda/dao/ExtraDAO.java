/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.Extra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExtraDAO {

    // Consultar todos los extras activos
    public List<Extra> listarActivos() {

        List<Extra> extras = new ArrayList<>();

        String sql = "SELECT * FROM extras WHERE activo = TRUE";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                extras.add(convertirExtra(resultado));
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar extras: " + e.getMessage());
        }

        return extras;
    }

    // Buscar un extra por identificador
    public Extra buscarPorId(int idExtra) {

        String sql = "SELECT * FROM extras WHERE id_extra = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idExtra);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    return convertirExtra(resultado);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar extra: " + e.getMessage());
        }

        return null;
    }

    // Convertir registro de MySQL en objeto Extra
    private Extra convertirExtra(ResultSet resultado)
            throws SQLException {

        Extra extra = new Extra();

        extra.setIdExtra(resultado.getInt("id_extra"));
        extra.setNombre(resultado.getString("nombre"));
        extra.setDescripcion(resultado.getString("descripcion"));
        extra.setPrecio(resultado.getBigDecimal("precio"));
        extra.setActivo(resultado.getBoolean("activo"));

        return extra;
    }
}
