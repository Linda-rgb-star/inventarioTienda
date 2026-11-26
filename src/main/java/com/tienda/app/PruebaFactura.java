/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.app;

import com.tienda.model.Factura;
import com.tienda.model.Pedido;
import com.tienda.service.FacturaService;

import java.math.BigDecimal;

public class PruebaFactura {

    public static void main(String[] args) {

        // 1. Asociar la factura al pedido existente
        Pedido pedido = new Pedido();
        pedido.setIdPedido(2);

        // 2. Crear la factura
        Factura factura = new Factura();

        factura.setPedido(pedido);
        factura.setNumeroFactura("FAC-2026-0002");
        factura.setTotal(new BigDecimal("105000"));
        factura.setEstado("EMITIDA");

        // 3. Generar y registrar la factura
        FacturaService facturaService = new FacturaService();

        boolean resultado =
                facturaService.generarFactura(factura);

        // 4. Mostrar resultado
        if (resultado) {

            System.out.println("FACTURA GENERADA EXITOSAMENTE");
            System.out.println(
                    "ID de factura: " + factura.getIdFactura());
            System.out.println(
                    "Número: " + factura.getNumeroFactura());
            System.out.println(
                    "Total: $" + factura.getTotal());
            System.out.println(
                    "Estado: " + factura.getEstado());

        } else {
            System.out.println("NO SE PUDO GENERAR LA FACTURA");
        }
    }
}
