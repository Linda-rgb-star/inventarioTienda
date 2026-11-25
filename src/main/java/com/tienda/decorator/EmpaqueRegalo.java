/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.decorator;

import java.math.BigDecimal;

public class EmpaqueRegalo extends ProductoDecorador {

    private BigDecimal costoEmpaque;

    public EmpaqueRegalo(
            ProductoComponente producto,
            BigDecimal costoEmpaque) {

        super(producto);
        this.costoEmpaque = costoEmpaque;
    }

    @Override
    public String getNombre() {
        return producto.getNombre() + " + Empaque de regalo";
    }

    @Override
    public BigDecimal getPrecio() {
        return producto.getPrecio().add(costoEmpaque);
    }
}
