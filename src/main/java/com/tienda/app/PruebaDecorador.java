/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.app;

import com.tienda.dao.ProductoDAO;
import com.tienda.decorator.ProductoComponente;
import com.tienda.decorator.GarantiaExtendida;
import com.tienda.decorator.EmpaqueRegalo;
import com.tienda.decorator.EnvioExpress;
import com.tienda.model.Producto;
import java.math.BigDecimal;

public class PruebaDecorador {

    public static void main(String[] args) {

        ProductoDAO productoDAO = new ProductoDAO();

        // Buscar los audífonos registrados anteriormente
        Producto producto = productoDAO.buscarPorId(2);

        if (producto == null) {
            System.out.println("No se encontró el producto.");
            return;
        }

        // Producto original
        ProductoComponente productoDecorado = producto;

        System.out.println("PRODUCTO ORIGINAL");
        System.out.println("Nombre: " + productoDecorado.getNombre());
        System.out.println("Precio: $" + productoDecorado.getPrecio());

        // Agregar garantía extendida
        productoDecorado = new GarantiaExtendida(
                productoDecorado,
                new BigDecimal("15000"),
                12
        );

        // Agregar empaque de regalo
        productoDecorado = new EmpaqueRegalo(
                productoDecorado,
                new BigDecimal("5000")
        );

        // Agregar envío express
        productoDecorado = new EnvioExpress(
                productoDecorado,
                new BigDecimal("10000")
        );

        // Mostrar producto con todos los extras
        System.out.println("\nPRODUCTO CON EXTRAS");
        System.out.println("Nombre: " + productoDecorado.getNombre());
        System.out.println("Precio final: $" + productoDecorado.getPrecio());
    }
}
