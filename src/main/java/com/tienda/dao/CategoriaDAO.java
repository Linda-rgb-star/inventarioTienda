/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public boolean registrar(Categoria categoria) {

        String sql = "INSERT INTO categorias (nombre, descripcion) "
                + "VALUES (?, ?)";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, categoria.getNombre());
            consulta.setString(2, categoria.getDescripcion());

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar categoría: " + e.getMessage());
            return false;
        }
    }

    public List<Categoria> listarTodas() {

        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT * FROM categorias";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                Categoria categoria = new Categoria();

                categoria.setIdCategoria(
                        resultado.getInt("id_categoria"));

                categoria.setNombre(
                        resultado.getString("nombre"));

                categoria.setDescripcion(
                        resultado.getString("descripcion"));

                categorias.add(categoria);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar categorías: " + e.getMessage());
        }

        return categorias;
    }

    public Categoria buscarPorId(int idCategoria) {

        String sql = "SELECT * FROM categorias WHERE id_categoria = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idCategoria);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {

                    Categoria categoria = new Categoria();

                    categoria.setIdCategoria(
                            resultado.getInt("id_categoria"));

                    categoria.setNombre(
                            resultado.getString("nombre"));

                    categoria.setDescripcion(
                            resultado.getString("descripcion"));

                    return categoria;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar categoría: " + e.getMessage());
        }

        return null;
    }
}