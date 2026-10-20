/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author linda
 */

import conexion.Conexion;
import model.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    // Registrar una categoria
    public boolean insertar(Categoria categoria) {
        String sql = "INSERT INTO categoria (nombre) VALUES (?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar categoría: "
                    + e.getMessage());
            return false;
        }
    }

    // Consultar todas las categorías
    public List<Categoria> listar() {
        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT * FROM categoria";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")
                );

                categorias.add(categoria);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar categorías: "
                    + e.getMessage());
        }

        return categorias;
    }

    // Actualizar una categoría
    public boolean actualizar(Categoria categoria) {
        String sql = "UPDATE categoria SET nombre = ? "
                + "WHERE id_categoria = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getIdCategoria());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar categoría: "
                    + e.getMessage());
            return false;
        }
    }

    // Eliminar una categoría
    public boolean eliminar(int idCategoria) {
        String sql = "DELETE FROM categoria "
                + "WHERE id_categoria = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar categoría: "
                    + e.getMessage());
            return false;
        }
    }
}
