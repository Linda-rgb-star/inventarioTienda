/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.service;

import com.tienda.dao.FacturaDAO;
import com.tienda.dao.PagoDAO;

import com.tienda.model.Factura;
import com.tienda.model.Pago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FacturaService {

    private final FacturaDAO facturaDAO;
    private final PagoDAO pagoDAO;

    public FacturaService() {
        this.facturaDAO = new FacturaDAO();
        this.pagoDAO = new PagoDAO();
    }

    // Generar y registrar una factura
    public boolean generarFactura(Factura factura) {

        // 1. Validar información de la factura
        if (factura == null
                || factura.getPedido() == null
                || factura.getPedido().getIdPedido() <= 0
                || factura.getNumeroFactura() == null
                || factura.getNumeroFactura().isBlank()
                || factura.getTotal() == null
                || factura.getTotal().signum() <= 0) {

            System.out.println("Los datos de la factura no son válidos.");
            return false;
        }

        int idPedido = factura.getPedido().getIdPedido();

        // 2. Validar total del pedido
        BigDecimal totalPedido = factura.getPedido().getTotal();

        if (totalPedido == null
                || totalPedido.signum() <= 0
                || factura.getTotal().compareTo(totalPedido) != 0) {

            System.out.println(
                    "El total de la factura no coincide con el pedido.");
            return false;
        }

        // 3. Comprobar que el pedido no tenga factura
        if (facturaDAO.buscarPorPedido(idPedido) != null) {

            System.out.println(
                    "Este pedido ya tiene una factura registrada.");
            return false;
        }

        // 4. Consultar pagos asociados al pedido
        List<Pago> pagos = pagoDAO.listarPorPedido(idPedido);

        boolean pagoAprobado = false;

        for (Pago pago : pagos) {

            if (pago != null
                    && "APROBADO".equalsIgnoreCase(pago.getEstado())
                    && pago.getMonto() != null
                    && pago.getMonto().compareTo(totalPedido) == 0) {

                pagoAprobado = true;
                break;
            }
        }

        if (!pagoAprobado) {

            System.out.println(
                    "No se puede generar la factura: "
                    + "no existe un pago aprobado por el total del pedido.");

            return false;
        }

        // 5. Asignar fecha de emisión
        if (factura.getFechaEmision() == null) {
            factura.setFechaEmision(LocalDateTime.now());
        }

        // 6. Registrar factura en MySQL
        boolean registrada = facturaDAO.registrar(factura);

        if (registrada) {

            System.out.println("================================");
            System.out.println("FACTURA GENERADA CORRECTAMENTE");
            System.out.println("================================");

            System.out.println(
                    "Número de factura: " + factura.getNumeroFactura());

            System.out.println(
                    "ID de factura: " + factura.getIdFactura());

            System.out.println(
                    "Total facturado: $" + factura.getTotal());

            return true;
        }

        System.out.println("No se pudo registrar la factura.");
        return false;
    }

    // Consultar todas las facturas
    public List<Factura> listarFacturas() {
        return facturaDAO.listarTodas();
    }

    // Consultar factura de un pedido
    public Factura buscarPorPedido(int idPedido) {

        if (idPedido <= 0) {
            return null;
        }

        return facturaDAO.buscarPorPedido(idPedido);
    }
}
