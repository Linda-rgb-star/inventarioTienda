/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.app;

import com.tienda.model.Pago;
import com.tienda.strategy.PagoContraEntrega;
import com.tienda.strategy.ProcesadorPago;
import java.math.BigDecimal;

public class PruebaPagos {

    public static void main(String[] args) {

        // Crear un pago de prueba
        Pago pago = new Pago();
        pago.setMonto(new BigDecimal("85000"));

        // Seleccionar estrategia contra entrega
        PagoContraEntrega contraEntrega =
                new PagoContraEntrega();

        // Crear procesador
        ProcesadorPago procesador =
                new ProcesadorPago(contraEntrega);

        // Ejecutar pago
        boolean resultado = procesador.ejecutarPago(pago);

        // Mostrar resultados
        System.out.println("\nRESULTADO DEL PAGO");

        if (resultado) {
            System.out.println("Pago procesado correctamente.");
            System.out.println("Estado: " + pago.getEstado());
            System.out.println("Referencia: " + pago.getReferencia());
            System.out.println("Monto: $" + pago.getMonto());
        } else {
            System.out.println("El pago no pudo procesarse.");
        }
    }
}
