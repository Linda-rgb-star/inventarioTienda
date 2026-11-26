/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.app;

import com.tienda.dao.CategoriaDAO;
import com.tienda.factory.FabricaRopa;
import com.tienda.model.Categoria;
import com.tienda.model.Producto;
import com.tienda.service.ProductoService;
import java.math.BigDecimal;

public class PruebaProductos {

    public static void main(String[] args) {

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ProductoService productoService = new ProductoService();

        // Buscar la categoría Ropa
        Categoria categoria = categoriaDAO.buscarPorId(1);

        if (categoria == null) {
            System.out.println("No se encontró la categoría.");
            return;
        }

        // Crear el producto mediante Factory Method
        FabricaRopa fabrica = new FabricaRopa(
                categoria,
                "Camiseta de prueba",
                "Camiseta de algodón",
                new BigDecimal("45000"),
                10,
                "M",
                "Negro",
                "Algodón"
        );

        // Registrar el producto
        boolean registrado = productoService.crearProducto(fabrica);

        if (registrado) {
            System.out.println("Producto registrado correctamente.");
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
