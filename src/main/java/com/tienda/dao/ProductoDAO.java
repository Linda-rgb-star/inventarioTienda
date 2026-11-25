/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.dao;

import com.tienda.database.ConexionBD;
import com.tienda.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // Registrar un producto y sus atributos específicos
    public boolean registrar(Producto producto) {

        String sqlProducto = "INSERT INTO productos "
                + "(id_categoria, nombre, descripcion, precio, stock, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conexion = null;

        try {
            conexion = ConexionBD.conectarBD();
            conexion.setAutoCommit(false);

            int idProducto;

            try (PreparedStatement consulta = conexion.prepareStatement(
                    sqlProducto, Statement.RETURN_GENERATED_KEYS)) {

                consulta.setInt(1, producto.getCategoria().getIdCategoria());
                consulta.setString(2, producto.getNombre());
                consulta.setString(3, producto.getDescripcion());
                consulta.setBigDecimal(4, producto.getPrecio());
                consulta.setInt(5, producto.getStock());
                consulta.setBoolean(6, producto.isActivo());

                consulta.executeUpdate();

                try (ResultSet resultado = consulta.getGeneratedKeys()) {
                    if (!resultado.next()) {
                        throw new SQLException("No se generó el ID del producto.");
                    }

                    idProducto = resultado.getInt(1);
                    producto.setIdProducto(idProducto);
                }
            }

            // Guardar atributos de acuerdo con el tipo de producto
            if (producto instanceof ProductoRopa) {

                ProductoRopa ropa = (ProductoRopa) producto;

                String sql = "INSERT INTO producto_ropa "
                        + "(id_producto, talla, color, material) "
                        + "VALUES (?, ?, ?, ?)";

                try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
                    consulta.setInt(1, idProducto);
                    consulta.setString(2, ropa.getTalla());
                    consulta.setString(3, ropa.getColor());
                    consulta.setString(4, ropa.getMaterial());
                    consulta.executeUpdate();
                }

            } else if (producto instanceof ProductoTecnologia) {

                ProductoTecnologia tecnologia = (ProductoTecnologia) producto;

                String sql = "INSERT INTO producto_tecnologia "
                        + "(id_producto, marca, modelo, garantia_meses) "
                        + "VALUES (?, ?, ?, ?)";

                try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
                    consulta.setInt(1, idProducto);
                    consulta.setString(2, tecnologia.getMarca());
                    consulta.setString(3, tecnologia.getModelo());
                    consulta.setInt(4, tecnologia.getGarantiaMeses());
                    consulta.executeUpdate();
                }

            } else if (producto instanceof ProductoAlimento) {

                ProductoAlimento alimento = (ProductoAlimento) producto;

                String sql = "INSERT INTO producto_alimento "
                        + "(id_producto, fecha_vencimiento, peso_gramos, ingredientes) "
                        + "VALUES (?, ?, ?, ?)";

                try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
                    consulta.setInt(1, idProducto);

                    if (alimento.getFechaVencimiento() != null) {
                        consulta.setDate(2, java.sql.Date.valueOf(
                                alimento.getFechaVencimiento()));
                    } else {
                        consulta.setNull(2, java.sql.Types.DATE);
                    }

                    consulta.setBigDecimal(3, alimento.getPesoGramos());
                    consulta.setString(4, alimento.getIngredientes());
                    consulta.executeUpdate();
                }
            }

            conexion.commit();
            return true;

        } catch (SQLException e) {

            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException error) {
                    System.out.println("Error al revertir: " + error.getMessage());
                }
            }

            System.out.println("Error al registrar producto: " + e.getMessage());
            return false;

        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    // Consultar todos los productos
    public List<Producto> listarTodos() {

        List<Producto> productos = new ArrayList<>();

        String sql = """
            SELECT p.*, c.nombre AS nombre_categoria,
                   r.id_producto AS id_ropa, r.talla, r.color, r.material,
                   t.id_producto AS id_tecnologia, t.marca, t.modelo,
                   t.garantia_meses,
                   a.id_producto AS id_alimento, a.fecha_vencimiento,
                   a.peso_gramos, a.ingredientes
            FROM productos p
            INNER JOIN categorias c ON p.id_categoria = c.id_categoria
            LEFT JOIN producto_ropa r ON p.id_producto = r.id_producto
            LEFT JOIN producto_tecnologia t ON p.id_producto = t.id_producto
            LEFT JOIN producto_alimento a ON p.id_producto = a.id_producto
            ORDER BY p.id_producto
            """;

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                productos.add(convertirProducto(resultado));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }

        return productos;
    }

    // Buscar un producto por su identificador
    public Producto buscarPorId(int idProducto) {

        String sql = """
            SELECT p.*, c.nombre AS nombre_categoria,
                   r.id_producto AS id_ropa, r.talla, r.color, r.material,
                   t.id_producto AS id_tecnologia, t.marca, t.modelo,
                   t.garantia_meses,
                   a.id_producto AS id_alimento, a.fecha_vencimiento,
                   a.peso_gramos, a.ingredientes
            FROM productos p
            INNER JOIN categorias c ON p.id_categoria = c.id_categoria
            LEFT JOIN producto_ropa r ON p.id_producto = r.id_producto
            LEFT JOIN producto_tecnologia t ON p.id_producto = t.id_producto
            LEFT JOIN producto_alimento a ON p.id_producto = a.id_producto
            WHERE p.id_producto = ?
            """;

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idProducto);

            try (ResultSet resultado = consulta.executeQuery()) {
                if (resultado.next()) {
                    return convertirProducto(resultado);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }

        return null;
    }

    // Convertir un registro de MySQL en un objeto Java
    private Producto convertirProducto(ResultSet resultado)
            throws SQLException {

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(resultado.getInt("id_categoria"));
        categoria.setNombre(resultado.getString("nombre_categoria"));

        Producto producto;

        if (resultado.getObject("id_ropa") != null) {

            ProductoRopa ropa = new ProductoRopa();
            ropa.setTalla(resultado.getString("talla"));
            ropa.setColor(resultado.getString("color"));
            ropa.setMaterial(resultado.getString("material"));
            producto = ropa;

        } else if (resultado.getObject("id_tecnologia") != null) {

            ProductoTecnologia tecnologia = new ProductoTecnologia();
            tecnologia.setMarca(resultado.getString("marca"));
            tecnologia.setModelo(resultado.getString("modelo"));
            tecnologia.setGarantiaMeses(resultado.getInt("garantia_meses"));
            producto = tecnologia;

        } else if (resultado.getObject("id_alimento") != null) {

            ProductoAlimento alimento = new ProductoAlimento();

            java.sql.Date fecha = resultado.getDate("fecha_vencimiento");

            if (fecha != null) {
                alimento.setFechaVencimiento(fecha.toLocalDate());
            }

            alimento.setPesoGramos(resultado.getBigDecimal("peso_gramos"));
            alimento.setIngredientes(resultado.getString("ingredientes"));
            producto = alimento;

        } else {
            producto = new Producto();
        }

        producto.setIdProducto(resultado.getInt("id_producto"));
        producto.setCategoria(categoria);
        producto.setNombre(resultado.getString("nombre"));
        producto.setDescripcion(resultado.getString("descripcion"));
        producto.setPrecio(resultado.getBigDecimal("precio"));
        producto.setStock(resultado.getInt("stock"));
        producto.setActivo(resultado.getBoolean("activo"));

        return producto;
    }
    // Descontar unidades del inventario
public boolean actualizarStock(int idProducto, int cantidad) {

    String sql = "UPDATE productos "
            + "SET stock = stock - ? "
            + "WHERE id_producto = ? AND stock >= ?";

    try (Connection conexion = ConexionBD.conectarBD();
         PreparedStatement consulta = conexion.prepareStatement(sql)) {

        consulta.setInt(1, cantidad);
        consulta.setInt(2, idProducto);
        consulta.setInt(3, cantidad);

        return consulta.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Error al actualizar el stock: "
                + e.getMessage());
        return false;
    }
}
// Actualizar inventario usando una conexión transaccional
public boolean actualizarStock(
        Connection conexion,
        int idProducto,
        int cantidad) {

    String sql = "UPDATE productos "
            + "SET stock = stock - ? "
            + "WHERE id_producto = ? AND stock >= ?";

    try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

        consulta.setInt(1, cantidad);
        consulta.setInt(2, idProducto);
        consulta.setInt(3, cantidad);

        return consulta.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Error al actualizar el stock: "
                + e.getMessage());
        return false;
    }
}

    // Aumentar las existencias de un producto
    public boolean aumentarStock(int idProducto, int cantidad) {

        if (idProducto <= 0 || cantidad <= 0) {
            System.out.println("Datos de reposición inválidos.");
            return false;
        }

        String sql = "UPDATE productos "
                + "SET stock = stock + ? "
                + "WHERE id_producto = ?";

        try (Connection conexion = ConexionBD.conectarBD();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, cantidad);
            consulta.setInt(2, idProducto);

            boolean actualizado = consulta.executeUpdate() > 0;

            if (actualizado) {
                System.out.println("Inventario repuesto correctamente.");
            }

            return actualizado;

        } catch (SQLException e) {
            System.out.println("Error al reponer inventario: "
                    + e.getMessage());
            return false;
        }
    }

}
