/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.decorator;

import java.math.BigDecimal;

public class EnvioExpress extends ProductoDecorador {

    private BigDecimal costoEnvio;

    public EnvioExpress(
            ProductoComponente producto,
            BigDecimal costoEnvio) {

        super(producto);
        this.costoEnvio = costoEnvio;
    }

    @Override
    public String getNombre() {
        return producto.getNombre() + " + Envío exprés";
    }

    @Override
    public BigDecimal getPrecio() {
        return producto.getPrecio().add(costoEnvio);
    }
}
