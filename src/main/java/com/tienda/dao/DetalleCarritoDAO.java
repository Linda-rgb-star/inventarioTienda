/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.DetalleCarrito;
import com.tienda.model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleCarritoDAO {

    // Agregar un producto al carrito
    public boolean agregar(DetalleCarrito detalle) {

        String sql = "INSERT INTO detalle_carrito "
                + "(id_carrito, id_producto, cantidad) "
                + "VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, detalle.getCarrito().getIdCarrito());
            consulta.setInt(2, detalle.getProducto().getIdProducto());
            consulta.setInt(3, detalle.getCantidad());

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
            return false;
        }
    }

    // Consultar los productos de un carrito
    public List<DetalleCarrito> listarPorCarrito(int idCarrito) {

        List<DetalleCarrito> detalles = new ArrayList<>();

        String sql = "SELECT * FROM detalle_carrito "
                + "WHERE id_carrito = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idCarrito);

            try (ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {

                    DetalleCarrito detalle = new DetalleCarrito();

                    detalle.setIdDetalle(
                            resultado.getInt("id_detalle"));

                    detalle.setCantidad(
                            resultado.getInt("cantidad"));

                    Producto producto = new Producto();
                    producto.setIdProducto(
                            resultado.getInt("id_producto"));

                    detalle.setProducto(producto);

                    detalles.add(detalle);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar carrito: " + e.getMessage());
        }

        return detalles;
    }

    // Modificar la cantidad de un producto
    public boolean actualizarCantidad(int idDetalle, int cantidad) {

        if (cantidad <= 0) {
            return false;
        }

        String sql = "UPDATE detalle_carrito SET cantidad = ? "
                + "WHERE id_detalle = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, cantidad);
            consulta.setInt(2, idDetalle);

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar cantidad: " + e.getMessage());
            return false;
        }
    }

    // Eliminar un producto del carrito
    public boolean eliminar(int idDetalle) {

        String sql = "DELETE FROM detalle_carrito "
                + "WHERE id_detalle = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idDetalle);

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
}
