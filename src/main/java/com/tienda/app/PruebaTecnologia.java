/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.app;

import com.tienda.dao.CategoriaDAO;
import com.tienda.factory.FabricaTecnologia;
import com.tienda.model.Categoria;
import com.tienda.model.Producto;
import com.tienda.service.ProductoService;
import java.math.BigDecimal;

public class PruebaTecnologia {

    public static void main(String[] args) {

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ProductoService productoService = new ProductoService();

        // Buscar la categoría Tecnología
        Categoria categoria = categoriaDAO.buscarPorId(2);

        if (categoria == null) {
            System.out.println("No se encontró la categoría Tecnología.");
            return;
        }

        // Crear producto mediante Factory Method
        FabricaTecnologia fabrica = new FabricaTecnologia(
                categoria,
                "Audífonos Bluetooth",
                "Audífonos inalámbricos con conexión Bluetooth",
                new BigDecimal("85000"),
                15,
                "Sony",
                "WH-CH520",
                12
        );

        // Registrar producto
        boolean registrado = productoService.crearProducto(fabrica);

        if (registrado) {
            System.out.println("Producto de tecnología registrado correctamente.");
        } else {
            System.out.println("No se pudo registrar el producto.");
        }

        // Consultar productos
        System.out.println("\nLISTA DE PRODUCTOS:");

        for (Producto producto : productoService.listarProductos()) {
            System.out.println(producto);
        }
    }
}
