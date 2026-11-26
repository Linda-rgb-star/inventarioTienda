/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.app;

import com.tienda.dao.CategoriaDAO;
import com.tienda.factory.FabricaAlimentos;
import com.tienda.model.Categoria;
import com.tienda.model.Producto;
import com.tienda.service.ProductoService;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PruebaAlimentos {

    public static void main(String[] args) {

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ProductoService productoService = new ProductoService();

        // Buscar la categoría Alimentos
        Categoria categoria = categoriaDAO.buscarPorId(3);

        if (categoria == null) {
            System.out.println("No se encontró la categoría Alimentos.");
            return;
        }

        // Crear producto mediante Factory Method
        FabricaAlimentos fabrica = new FabricaAlimentos(
                categoria,
                "Granola natural",
                "Granola con avena y frutos secos",
                new BigDecimal("18000"),
                20,
                LocalDate.of(2027, 6, 30),
                new BigDecimal("500"),
                "Avena, almendras, nueces y miel"
        );

        // Registrar producto
        boolean registrado = productoService.crearProducto(fabrica);

        if (registrado) {
            System.out.println("Producto alimenticio registrado correctamente.");
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
