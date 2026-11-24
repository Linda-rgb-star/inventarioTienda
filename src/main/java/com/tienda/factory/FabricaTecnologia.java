/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.factory;

import com.tienda.model.Producto;
import com.tienda.model.ProductoTecnologia;
import com.tienda.model.Categoria;
import java.math.BigDecimal;

public class FabricaTecnologia implements FabricaProducto {

    private Categoria categoria;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private String marca;
    private String modelo;
    private int garantiaMeses;

    public FabricaTecnologia(Categoria categoria, String nombre,
            String descripcion, BigDecimal precio, int stock,
            String marca, String modelo, int garantiaMeses) {

        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.marca = marca;
        this.modelo = modelo;
        this.garantiaMeses = garantiaMeses;
    }

    @Override
    public Producto crearProducto() {

        return new ProductoTecnologia(
                categoria,
                nombre,
                descripcion,
                precio,
                stock,
                marca,
                modelo,
                garantiaMeses
        );
    }
}
