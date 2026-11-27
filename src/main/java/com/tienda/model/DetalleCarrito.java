/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.model;

import java.math.BigDecimal;

public class DetalleCarrito {

    private int idDetalle;
    private Carrito carrito;
    private Producto producto;
    private int cantidad;

    public DetalleCarrito() {
    }

    public DetalleCarrito(int idDetalle, Carrito carrito,
            Producto producto, int cantidad) {
        this.idDetalle = idDetalle;
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public DetalleCarrito(Carrito carrito, Producto producto, int cantidad) {
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public BigDecimal calcularSubtotal() {
        return producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "DetalleCarrito{" +
                "producto=" + producto.getNombre() +
                ", cantidad=" + cantidad +
                ", subtotal=" + calcularSubtotal() +
                '}';
    }
}