/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.strategy;

import com.tienda.model.Pago;
import java.math.BigDecimal;

public class PagoContraEntrega implements EstrategiaPago {

    @Override
    public boolean procesarPago(Pago pago) {

        if (pago == null || pago.getMonto() == null
                || pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {

            System.out.println("El monto del pedido no es válido.");
            return false;
        }

        pago.setEstado("PENDIENTE");
        pago.setReferencia("CONTRAENTREGA-" + System.currentTimeMillis());

        System.out.println("Pedido registrado para pago contra entrega.");
        System.out.println("El cliente pagará al recibir el producto.");
        System.out.println("Monto pendiente: $" + pago.getMonto());

        return true;
    }

    @Override
    public String obtenerNombreMetodo() {
        return "Pago contra entrega";
    }
}
