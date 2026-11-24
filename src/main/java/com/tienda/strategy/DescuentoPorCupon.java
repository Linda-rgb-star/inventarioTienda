/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.strategy;

import com.tienda.model.Cupon;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DescuentoPorCupon implements EstrategiaDescuento {

    private Cupon cupon;

    public DescuentoPorCupon(Cupon cupon) {
        this.cupon = cupon;
    }

    @Override
    public BigDecimal calcularDescuento(BigDecimal subtotal) {

        if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) <= 0
                || cupon == null || !cupon.esValido()
                || !cupon.getDescuento().estaVigente()) {

            return BigDecimal.ZERO;
        }

        BigDecimal porcentaje = cupon.getDescuento().getValor();

        return subtotal.multiply(porcentaje)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    @Override
    public String obtenerNombreDescuento() {
        return "Descuento por cupón";
    }
}
