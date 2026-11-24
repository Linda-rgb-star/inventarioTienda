/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.factory;

import com.tienda.model.Producto;
import com.tienda.model.ProductoAlimento;
import com.tienda.model.Categoria;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FabricaAlimentos implements FabricaProducto {

    private Categoria categoria;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private LocalDate fechaVencimiento;
    private BigDecimal pesoGramos;
    private String ingredientes;

    public FabricaAlimentos(Categoria categoria, String nombre,
            String descripcion, BigDecimal precio, int stock,
            LocalDate fechaVencimiento, BigDecimal pesoGramos,
            String ingredientes) {

        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.fechaVencimiento = fechaVencimiento;
        this.pesoGramos = pesoGramos;
        this.ingredientes = ingredientes;
    }

    @Override
    public Producto crearProducto() {

        return new ProductoAlimento(
                categoria,
                nombre,
                descripcion,
                precio,
                stock,
                fechaVencimiento,
                pesoGramos,
                ingredientes
        );
    }
}
