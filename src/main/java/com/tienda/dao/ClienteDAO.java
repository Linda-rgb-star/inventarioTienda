/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean registrar(Cliente cliente) {

        String sql = "INSERT INTO clientes "
                + "(nombres, apellidos, correo, telefono, direccion) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            consulta.setString(1, cliente.getNombres());
            consulta.setString(2, cliente.getApellidos());
            consulta.setString(3, cliente.getCorreo());
            consulta.setString(4, cliente.getTelefono());
            consulta.setString(5, cliente.getDireccion());

            int filas = consulta.executeUpdate();

            if (filas > 0) {
                try (ResultSet resultado = consulta.getGeneratedKeys()) {
                    if (resultado.next()) {
                        cliente.setIdCliente(resultado.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar cliente: " + e.getMessage());
        }

        return false;
    }

    public Cliente buscarPorId(int idCliente) {

        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idCliente);

            try (ResultSet resultado = consulta.executeQuery()) {
                if (resultado.next()) {
                    return convertirCliente(resultado);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
        }

        return null;
    }

    public List<Cliente> listarTodos() {

        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                clientes.add(convertirCliente(resultado));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }

        return clientes;
    }

    private Cliente convertirCliente(ResultSet resultado)
            throws SQLException {

        Cliente cliente = new Cliente();

        cliente.setIdCliente(resultado.getInt("id_cliente"));
        cliente.setNombres(resultado.getString("nombres"));
        cliente.setApellidos(resultado.getString("apellidos"));
        cliente.setCorreo(resultado.getString("correo"));
        cliente.setTelefono(resultado.getString("telefono"));
        cliente.setDireccion(resultado.getString("direccion"));

        Timestamp fecha = resultado.getTimestamp("fecha_registro");

        if (fecha != null) {
            cliente.setFechaRegistro(fecha.toLocalDateTime());
        }

        return cliente;
    }
}