/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.model;

import java.math.BigDecimal;
import com.tienda.decorator.ProductoComponente;

public class Producto implements ProductoComponente {

    // Atributos
    private int idProducto;
    private Categoria categoria;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private boolean activo;

    // Constructor vacío
    public Producto() {
    }

    // Constructor completo
    public Producto(int idProducto, Categoria categoria,
                    String nombre, String descripcion,
                    BigDecimal precio, int stock, boolean activo) {

        this.idProducto = idProducto;
        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.activo = activo;
    }

    // Constructor para registrar un producto
    public Producto(Categoria categoria, String nombre,
                    String descripcion, BigDecimal precio, int stock) {

        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.activo = true;
    }

    // Getters y Setters

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Verificar disponibilidad
    public boolean hayStock(int cantidad) {
        return cantidad > 0 && stock >= cantidad;
    }

    // Representación del producto
    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }
}
