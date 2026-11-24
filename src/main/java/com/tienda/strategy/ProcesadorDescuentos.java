/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.strategy;

import java.math.BigDecimal;

public class ProcesadorDescuentos {

    private EstrategiaDescuento estrategia;

    public ProcesadorDescuentos(EstrategiaDescuento estrategia) {
        this.estrategia = estrategia;
    }

    public void setEstrategia(EstrategiaDescuento estrategia) {
        this.estrategia = estrategia;
    }

    public BigDecimal ejecutarDescuento(BigDecimal subtotal) {

        if (estrategia == null) {
            System.out.println("No se ha seleccionado una estrategia de descuento.");
            return BigDecimal.ZERO;
        }

        BigDecimal descuento = estrategia.calcularDescuento(subtotal);

        System.out.println("Descuento aplicado: "
                + estrategia.obtenerNombreDescuento());

        System.out.println("Valor descontado: $" + descuento);

        return descuento;
    }
}
