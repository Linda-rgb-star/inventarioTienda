/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.factory;

import com.tienda.model.Producto;
import com.tienda.model.ProductoRopa;
import com.tienda.model.Categoria;
import java.math.BigDecimal;

public class FabricaRopa implements FabricaProducto {

    private Categoria categoria;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private String talla;
    private String color;
    private String material;

    public FabricaRopa(Categoria categoria, String nombre,
            String descripcion, BigDecimal precio, int stock,
            String talla, String color, String material) {

        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.talla = talla;
        this.color = color;
        this.material = material;
    }

    @Override
    public Producto crearProducto() {

        return new ProductoRopa(
                categoria,
                nombre,
                descripcion,
                precio,
                stock,
                talla,
                color,
                material
        );
    }
}
