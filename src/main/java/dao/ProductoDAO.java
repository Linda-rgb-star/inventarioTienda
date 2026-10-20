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
import model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // Registrar un producto
    
    public boolean insertar(Producto producto) {
    String sql = "INSERT INTO producto "
            + "(nombre, precio, cantidad, id_categoria) "
            + "VALUES (?, ?, ?, ?)";

    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, producto.getNombre());
        ps.setDouble(2, producto.getPrecio());
        ps.setInt(3, producto.getCantidad());
        ps.setInt(4, producto.getCategoria().getIdCategoria());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Error al insertar producto: " + e.getMessage());
        return false;
    }
}

    // Consultar todos los productos
    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT p.id_producto, p.nombre, p.precio, "
                + "p.cantidad, c.id_categoria, c.nombre AS categoria "
                + "FROM producto p "
                + "INNER JOIN categoria c "
                + "ON p.id_categoria = c.id_categoria";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return productos;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Categoria categoria = new Categoria(
                            rs.getInt("id_categoria"),
                            rs.getString("categoria")
                    );

                    Producto producto = new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getInt("cantidad"),
                            categoria
                    );

                    productos.add(producto);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: "
                    + e.getMessage());
        }

        return productos;
    }

    // Actualizar un producto
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, "
                + "cantidad = ?, id_categoria = ? "
                + "WHERE id_producto = ?";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, producto.getNombre());
                ps.setDouble(2, producto.getPrecio());
                ps.setInt(3, producto.getCantidad());
                ps.setInt(4, producto.getCategoria().getIdCategoria());
                ps.setInt(5, producto.getIdProducto());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: "
                    + e.getMessage());
            return false;
        }
    }

    // Eliminar un producto
    public boolean eliminar(int idProducto) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idProducto);
                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: "
                    + e.getMessage());
            return false;
        }
    }
}
