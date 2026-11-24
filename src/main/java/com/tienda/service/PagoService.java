/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



package com.tienda.service;

import com.tienda.dao.PagoDAO;
import com.tienda.model.Pago;
import com.tienda.model.Pedido;
import com.tienda.model.MetodoPago;
import com.tienda.strategy.EstrategiaPago;
import com.tienda.strategy.ProcesadorPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoService {

    private final PagoDAO pagoDAO;

    public PagoService() {
        this.pagoDAO = new PagoDAO();
    }

    // Procesar y registrar un pago
    public boolean procesarPago(
            Pago pago,
            EstrategiaPago estrategia) {

        // 1. Validar información general
        if (pago == null || estrategia == null) {
            System.out.println("Los datos del pago no son válidos.");
            return false;
        }

        // 2. Validar pedido
        Pedido pedido = pago.getPedido();

        if (pedido == null
                || pedido.getIdPedido() <= 0
                || pedido.getTotal() == null
                || pedido.getTotal().signum() <= 0) {

            System.out.println("El pedido asociado no es válido.");
            return false;
        }

        // 3. Validar método de pago
        MetodoPago metodo = pago.getMetodoPago();

        if (metodo == null
                || metodo.getIdMetodoPago() <= 0
                || !metodo.isActivo()) {

            System.out.println("El método de pago no existe o está inactivo.");
            return false;
        }

        // 4. Validar monto
        if (pago.getMonto() == null
                || pago.getMonto().signum() <= 0) {

            System.out.println("El monto del pago no es válido.");
            return false;
        }

        if (pago.getMonto().compareTo(pedido.getTotal()) != 0) {
            System.out.println(
                    "El monto del pago no coincide con el total del pedido.");
            return false;
        }

        // 5. Asignar fecha
        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDateTime.now());
        }

        // 6. Ejecutar estrategia de pago
        ProcesadorPago procesador =
                new ProcesadorPago(estrategia);

        boolean pagoProcesado = procesador.ejecutarPago(pago);

        if (!pagoProcesado) {
            System.out.println("El pago no pudo ser procesado.");
            return false;
        }

        // 7. Registrar pago en la base de datos
        boolean pagoRegistrado = pagoDAO.registrar(pago);

        if (!pagoRegistrado) {
            System.out.println(
                    "El pago fue procesado, pero no pudo registrarse en la base de datos.");
            return false;
        }

        // 8. Mostrar resultado
        System.out.println("================================");
        System.out.println("PAGO REGISTRADO CORRECTAMENTE");
        System.out.println("================================");
        System.out.println("Pedido: " + pedido.getIdPedido());
        System.out.println("Método: " + estrategia.obtenerNombreMetodo());
        System.out.println("Monto: $" + pago.getMonto());
        System.out.println("Estado: " + pago.getEstado());

        return true;
    }
}
