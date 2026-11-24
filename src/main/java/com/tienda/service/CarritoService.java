/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.service;

import com.tienda.dao.DetalleCarritoDAO;
import com.tienda.dao.ProductoDAO;
import com.tienda.model.Carrito;
import com.tienda.model.DetalleCarrito;
import com.tienda.model.Producto;
import java.util.List;

public class CarritoService {

    private final DetalleCarritoDAO detalleCarritoDAO;
    private final ProductoDAO productoDAO;

    public CarritoService() {
        this.detalleCarritoDAO = new DetalleCarritoDAO();
        this.productoDAO = new ProductoDAO();
    }

    public boolean agregarProducto(int idCarrito,
            int idProducto, int cantidad) {

        if (idCarrito <= 0 || idProducto <= 0 || cantidad <= 0) {
            return false;
        }

        Producto producto = productoDAO.buscarPorId(idProducto);

        if (producto == null || !producto.hayStock(cantidad)) {
            return false;
        }

        Carrito carrito = new Carrito();
        carrito.setIdCarrito(idCarrito);

        DetalleCarrito detalle = new DetalleCarrito();
        detalle.setCarrito(carrito);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);

        return detalleCarritoDAO.agregar(detalle);
    }

    public List<DetalleCarrito> consultarCarrito(int idCarrito) {

        if (idCarrito <= 0) {
            return List.of();
        }

        return detalleCarritoDAO.listarPorCarrito(idCarrito);
    }

    public boolean actualizarCantidad(int idDetalle, int cantidad) {

        if (idDetalle <= 0 || cantidad <= 0) {
            return false;
        }

        return detalleCarritoDAO.actualizarCantidad(idDetalle, cantidad);
    }

    public boolean eliminarProducto(int idDetalle) {

        if (idDetalle <= 0) {
            return false;
        }

        return detalleCarritoDAO.eliminar(idDetalle);
    }
}
