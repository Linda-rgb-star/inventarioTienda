/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.decorator;

import java.math.BigDecimal;

public abstract class ProductoDecorador implements ProductoComponente {

    protected ProductoComponente producto;

    public ProductoDecorador(ProductoComponente producto) {
        this.producto = producto;
    }

    @Override
    public String getNombre() {
        return producto.getNombre();
    }

    @Override
    public BigDecimal getPrecio() {
        return producto.getPrecio();
    }
}
