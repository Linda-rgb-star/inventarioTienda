/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.decorator;

import java.math.BigDecimal;

public class GarantiaExtendida extends ProductoDecorador {

    private BigDecimal costoGarantia;
    private int mesesAdicionales;

    public GarantiaExtendida(
            ProductoComponente producto,
            BigDecimal costoGarantia,
            int mesesAdicionales) {

        super(producto);
        this.costoGarantia = costoGarantia;
        this.mesesAdicionales = mesesAdicionales;
    }

    @Override
    public String getNombre() {
        return producto.getNombre()
                + " + Garantía extendida (" 
                + mesesAdicionales + " meses)";
    }

    @Override
    public BigDecimal getPrecio() {
        return producto.getPrecio().add(costoGarantia);
    }
}
