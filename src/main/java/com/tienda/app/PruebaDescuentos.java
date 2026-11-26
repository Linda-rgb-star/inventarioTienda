/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.app;

import com.tienda.strategy.DescuentoPorCantidad;
import com.tienda.strategy.DescuentoTemporada;
import com.tienda.strategy.DescuentoClienteFrecuente;
import com.tienda.strategy.ProcesadorDescuentos;
import java.math.BigDecimal;

public class PruebaDescuentos {

    public static void main(String[] args) {

        BigDecimal subtotal = new BigDecimal("100000");

        // DESCUENTO POR CANTIDAD: 10%
        DescuentoPorCantidad cantidad =
                new DescuentoPorCantidad(new BigDecimal("10"));

        ProcesadorDescuentos procesadorCantidad =
                new ProcesadorDescuentos(cantidad);

        BigDecimal descuentoCantidad =
                procesadorCantidad.ejecutarDescuento(subtotal);

        // DESCUENTO DE TEMPORADA: 15%
        DescuentoTemporada temporada =
                new DescuentoTemporada(new BigDecimal("15"));

        ProcesadorDescuentos procesadorTemporada =
                new ProcesadorDescuentos(temporada);

        BigDecimal descuentoTemporada =
                procesadorTemporada.ejecutarDescuento(subtotal);

        // DESCUENTO CLIENTE FRECUENTE: 20%
        DescuentoClienteFrecuente frecuente =
                new DescuentoClienteFrecuente(new BigDecimal("20"));

        ProcesadorDescuentos procesadorFrecuente =
                new ProcesadorDescuentos(frecuente);

        BigDecimal descuentoFrecuente =
                procesadorFrecuente.ejecutarDescuento(subtotal);

        // Mostrar resultados
        System.out.println("===== PRUEBA DE DESCUENTOS =====");

        System.out.println("Subtotal: $" + subtotal);

        System.out.println("\nDescuento por cantidad: $"
                + descuentoCantidad);

        System.out.println("Descuento de temporada: $"
                + descuentoTemporada);

        System.out.println("Descuento cliente frecuente: $"
                + descuentoFrecuente);
    }
}
