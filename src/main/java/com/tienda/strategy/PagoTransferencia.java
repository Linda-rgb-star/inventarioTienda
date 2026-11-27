/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.strategy;

import com.tienda.model.Pago;
import java.math.BigDecimal;

public class PagoTransferencia implements EstrategiaPago {

    @Override
    public boolean procesarPago(Pago pago) {

        if (pago == null || pago.getMonto() == null
                || pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {

            System.out.println("El monto de la transferencia no es válido.");
            return false;
        }

        pago.setEstado("APROBADO");
        pago.setReferencia("TRANSFERENCIA-" + System.currentTimeMillis());

        System.out.println("Transferencia bancaria aprobada.");
        System.out.println("Monto: $" + pago.getMonto());

        return true;
    }

    @Override
    public String obtenerNombreMetodo() {
        return "Transferencia bancaria";
    }
}
