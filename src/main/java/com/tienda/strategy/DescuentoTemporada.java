/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DescuentoTemporada implements EstrategiaDescuento {

    private BigDecimal porcentaje;

    public DescuentoTemporada(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Override
    public BigDecimal calcularDescuento(BigDecimal subtotal) {

        if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return subtotal.multiply(porcentaje)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    @Override
    public String obtenerNombreDescuento() {
        return "Descuento de temporada";
    }
}
