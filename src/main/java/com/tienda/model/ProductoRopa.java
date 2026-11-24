/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.model;

import java.math.BigDecimal;

public class ProductoRopa extends Producto {

    // Atributos específicos de ropa
    private String talla;
    private String color;
    private String material;

    // Constructor vacío
    public ProductoRopa() {
        super();
    }

    // Constructor completo
    public ProductoRopa(int idProducto, Categoria categoria,
                        String nombre, String descripcion,
                        BigDecimal precio, int stock, boolean activo,
                        String talla, String color, String material) {

        super(idProducto, categoria, nombre, descripcion,
              precio, stock, activo);

        this.talla = talla;
        this.color = color;
        this.material = material;
    }

    // Constructor para registrar ropa
    public ProductoRopa(Categoria categoria, String nombre,
                        String descripcion, BigDecimal precio,
                        int stock, String talla, String color,
                        String material) {

        super(categoria, nombre, descripcion, precio, stock);

        this.talla = talla;
        this.color = color;
        this.material = material;
    }

    // Getters y Setters

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Talla: " + talla
                + " | Color: " + color;
    }
}
