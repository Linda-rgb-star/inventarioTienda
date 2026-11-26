/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.app;

import com.tienda.model.Pago;
import com.tienda.model.Pedido;
import com.tienda.model.MetodoPago;
import com.tienda.service.PagoService;
import com.tienda.strategy.PagoTarjeta;

import java.math.BigDecimal;

public class PruebaPago {

    public static void main(String[] args) {

        // 1. Recuperar el pedido registrado
        Pedido pedido = new Pedido();
        pedido.setIdPedido(2);

        // 2. Seleccionar el método de pago
        MetodoPago metodo = new MetodoPago();
        metodo.setIdMetodoPago(1);
        metodo.setNombre("Tarjeta");

        // 3. Crear el pago
        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMetodoPago(metodo);
        pago.setMonto(new BigDecimal("105000"));

        // 4. Seleccionar la estrategia de pago
        PagoTarjeta estrategia = new PagoTarjeta();

        // 5. Procesar y registrar el pago
        PagoService pagoService = new PagoService();

        boolean resultado = pagoService.procesarPago(
                pago,
                estrategia
        );

        // 6. Mostrar resultado
        if (resultado) {
            System.out.println("PAGO EXITOSO");
            System.out.println("ID del pago: " + pago.getIdPago());
            System.out.println("Monto: $105000");
            System.out.println("Estado: " + pago.getEstado());
            System.out.println("Referencia: " + pago.getReferencia());
        } else {
            System.out.println("NO SE PUDO REGISTRAR EL PAGO");
        }
    }
}
