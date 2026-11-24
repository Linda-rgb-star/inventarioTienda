/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.service;

import com.tienda.dao.ProductoDAO;
import com.tienda.factory.FabricaProducto;
import com.tienda.model.Producto;
import java.util.List;

public class ProductoService {

    private final ProductoDAO productoDAO;

    public ProductoService() {
        this.productoDAO = new ProductoDAO();
    }

    public boolean crearProducto(FabricaProducto fabrica) {
        if (fabrica == null) {
            return false;
        }

        Producto producto = fabrica.crearProducto();

        if (producto == null || producto.getPrecio() == null
                || producto.getPrecio().compareTo(
                        java.math.BigDecimal.ZERO) <= 0
                || producto.getStock() < 0) {
            return false;
        }

        return productoDAO.registrar(producto);
    }

    public List<Producto> listarProductos() {
        return productoDAO.listarTodos();
    }

    public Producto buscarProducto(int id) {
        if (id <= 0) {
            return null;
        }

        return productoDAO.buscarPorId(id);
    }
}
