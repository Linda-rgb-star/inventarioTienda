/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.tienda.service;

import com.tienda.dao.DetalleCarritoDAO;
import com.tienda.dao.ProductoDAO;
import com.tienda.dao.PedidoDAO;
import com.tienda.dao.DetallePedidoDAO;
import com.tienda.dao.DetallePedidoExtraDAO;

import com.tienda.database.ConexionBD;

import com.tienda.model.DetalleCarrito;
import com.tienda.model.Producto;
import com.tienda.model.Extra;
import com.tienda.model.Pedido;
import com.tienda.model.DetallePedido;
import com.tienda.model.DetallePedidoExtra;
import com.tienda.model.Pago;
import com.tienda.model.Factura;

import com.tienda.strategy.EstrategiaDescuento;
import com.tienda.strategy.EstrategiaPago;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CompraService {

    private final DetalleCarritoDAO detalleCarritoDAO;
    private final ProductoDAO productoDAO;
    private final DescuentoService descuentoService;
    private final PedidoDAO pedidoDAO;
    private final DetallePedidoDAO detallePedidoDAO;
    private final DetallePedidoExtraDAO detallePedidoExtraDAO;
    private final PagoService pagoService;
    private final FacturaService facturaService;

    public CompraService() {

        this.detalleCarritoDAO = new DetalleCarritoDAO();
        this.productoDAO = new ProductoDAO();
        this.descuentoService = new DescuentoService();
        this.pedidoDAO = new PedidoDAO();
        this.detallePedidoDAO = new DetallePedidoDAO();
        this.detallePedidoExtraDAO = new DetallePedidoExtraDAO();

        this.pagoService = new PagoService();
        this.facturaService = new FacturaService();
    }

    // ==========================================
    // REGISTRAR PEDIDO SIN EXTRAS
    // ==========================================

    public boolean registrarPedido(
            Pedido pedido,
            List<DetallePedido> detalles) {

        return registrarPedido(pedido, detalles, null);
    }

    // ==========================================
    // REGISTRAR PEDIDO CON EXTRAS E INVENTARIO
    // ==========================================

    public boolean registrarPedido(
            Pedido pedido,
            List<DetallePedido> detalles,
            List<List<Extra>> extrasPorDetalle) {

        // 1. Validaciones generales
        if (pedido == null || detalles == null || detalles.isEmpty()) {
            System.out.println("El pedido no contiene productos.");
            return false;
        }

        if (extrasPorDetalle != null
                && extrasPorDetalle.size() != detalles.size()) {

            System.out.println(
                    "La cantidad de listas de extras no coincide con los productos.");
            return false;
        }

        // 2. Validar y calcular subtotal
        BigDecimal subtotalCalculado = BigDecimal.ZERO;

        for (DetallePedido detalle : detalles) {

            if (detalle == null
                    || detalle.getProducto() == null
                    || detalle.getProducto().getIdProducto() <= 0
                    || detalle.getCantidad() <= 0
                    || detalle.getPrecioUnitario() == null
                    || detalle.getPrecioUnitario().signum() < 0) {

                System.out.println("Detalle de pedido inválido.");
                return false;
            }

            BigDecimal importe = detalle.getPrecioUnitario().multiply(
                    BigDecimal.valueOf(detalle.getCantidad())
            );

            detalle.setSubtotal(importe);
            subtotalCalculado = subtotalCalculado.add(importe);
        }

        // 3. Validar descuentos
        BigDecimal descuentos = pedido.getTotalDescuentos();

        if (descuentos == null) {
            descuentos = BigDecimal.ZERO;
        }

        if (descuentos.signum() < 0
                || descuentos.compareTo(subtotalCalculado) > 0) {

            System.out.println("El valor de los descuentos no es válido.");
            return false;
        }

        // 4. Validar y calcular extras
        BigDecimal totalExtras = BigDecimal.ZERO;

        if (extrasPorDetalle != null) {

            for (List<Extra> extras : extrasPorDetalle) {

                if (extras == null) {
                    continue;
                }

                for (Extra extra : extras) {

                    if (extra == null
                            || extra.getIdExtra() <= 0
                            || !extra.isActivo()
                            || extra.getPrecio() == null
                            || extra.getPrecio().signum() < 0) {

                        System.out.println("Extra inválido.");
                        return false;
                    }

                    totalExtras = totalExtras.add(extra.getPrecio());
                }
            }
        }

        // 5. Calcular total definitivo
        BigDecimal totalCalculado = subtotalCalculado
                .subtract(descuentos)
                .add(totalExtras);

        pedido.setSubtotal(subtotalCalculado);
        pedido.setTotalDescuentos(descuentos);
        pedido.setTotal(totalCalculado);

        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDateTime.now());
        }

        if (pedido.getEstado() == null || pedido.getEstado().isBlank()) {
            pedido.setEstado("PENDIENTE");
        }

        // 6. Iniciar transacción
        try (Connection conexion = ConexionBD.conectarBDTransaccional()) {

            try {

                // Registrar encabezado
                if (!pedidoDAO.registrar(conexion, pedido)) {
                    throw new SQLException("No se pudo registrar el pedido.");
                }

                // Registrar detalles, extras y actualizar inventario
                for (int i = 0; i < detalles.size(); i++) {

                    DetallePedido detalle = detalles.get(i);
                    detalle.setPedido(pedido);

                    // Registrar detalle
                    if (!detallePedidoDAO.registrar(conexion, detalle)) {
                        throw new SQLException(
                                "No se pudo registrar un detalle del pedido.");
                    }

                    // Registrar extras del detalle
                    if (extrasPorDetalle != null) {

                        List<Extra> extras = extrasPorDetalle.get(i);

                        if (extras != null) {

                            for (Extra extra : extras) {

                                DetallePedidoExtra detalleExtra =
                                        new DetallePedidoExtra(extra);

                                detalleExtra.setPrecioAplicado(
                                        extra.getPrecio());

                                boolean registrado =
                                        detallePedidoExtraDAO.registrar(
                                                conexion,
                                                detalle.getIdDetalle(),
                                                detalleExtra
                                        );

                                if (!registrado) {
                                    throw new SQLException(
                                            "No se pudo registrar un extra.");
                                }
                            }
                        }
                    }

                    // Descontar inventario
                    boolean actualizado = productoDAO.actualizarStock(
                            conexion,
                            detalle.getProducto().getIdProducto(),
                            detalle.getCantidad()
                    );

                    if (!actualizado) {
                        throw new SQLException(
                                "Stock insuficiente o error al actualizar inventario.");
                    }
                }

                // Confirmar transacción
                conexion.commit();

                System.out.println("================================");
                System.out.println("PEDIDO REGISTRADO");
                System.out.println("================================");
                System.out.println("ID pedido: " + pedido.getIdPedido());
                System.out.println("Subtotal: $" + subtotalCalculado);
                System.out.println("Descuentos: $" + descuentos);
                System.out.println("Extras: $" + totalExtras);
                System.out.println("Total: $" + totalCalculado);

                return true;

            } catch (SQLException e) {

                conexion.rollback();

                System.out.println(
                        "Compra cancelada: " + e.getMessage());

                return false;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error en la transacción: " + e.getMessage());

            return false;
        }
    }

    // ==========================================
    // REGISTRAR COMPRA COMPLETA
    // PEDIDO + PAGO + FACTURA
    // ==========================================

    public boolean registrarCompraCompleta(
            Pedido pedido,
            List<DetallePedido> detalles,
            List<List<Extra>> extrasPorDetalle,
            Pago pago,
            EstrategiaPago estrategia,
            Factura factura) {

        // 1. Validar información
        if (pedido == null
                || pago == null
                || estrategia == null
                || factura == null) {

            System.out.println(
                    "Los datos de la compra están incompletos.");

            return false;
        }

        // 2. Registrar pedido, detalles, extras e inventario
        boolean pedidoRegistrado = registrarPedido(
                pedido,
                detalles,
                extrasPorDetalle
        );

        if (!pedidoRegistrado) {
            return false;
        }

        // 3. Asociar pago al pedido
        pago.setPedido(pedido);
        pago.setMonto(pedido.getTotal());

        // 4. Procesar pago
        boolean pagoRegistrado = pagoService.procesarPago(
                pago,
                estrategia
        );

        if (!pagoRegistrado) {

            System.out.println(
                    "El pedido quedó registrado, pero el pago no pudo registrarse.");

            return false;
        }

        // 5. Si el pago está pendiente, no generar factura todavía
        if (!"APROBADO".equalsIgnoreCase(pago.getEstado())) {

            System.out.println(
                    "El pago está pendiente. No se generó la factura.");

            return true;
        }

        // 6. Asociar factura al pedido
        factura.setPedido(pedido);
        factura.setTotal(pedido.getTotal());

        // 7. Generar factura
        boolean facturaRegistrada =
                facturaService.generarFactura(factura);

        if (!facturaRegistrada) {

            System.out.println(
                    "Pedido y pago registrados, pero la factura no pudo generarse.");

            return false;
        }

        System.out.println("================================");
        System.out.println("COMPRA COMPLETADA");
        System.out.println("================================");

        return true;
    }

    // ==========================================
    // CALCULAR SUBTOTAL DEL CARRITO
    // ==========================================

    public BigDecimal calcularSubtotal(int idCarrito) {

        if (idCarrito <= 0) {
            return BigDecimal.ZERO;
        }

        List<DetalleCarrito> detalles =
                detalleCarritoDAO.listarPorCarrito(idCarrito);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (DetalleCarrito detalle : detalles) {

            if (detalle == null
                    || detalle.getProducto() == null
                    || detalle.getCantidad() <= 0) {
                continue;
            }

            Producto producto = productoDAO.buscarPorId(
                    detalle.getProducto().getIdProducto()
            );

            if (producto == null || producto.getPrecio() == null) {
                continue;
            }

            BigDecimal importe = producto.getPrecio().multiply(
                    BigDecimal.valueOf(detalle.getCantidad())
            );

            subtotal = subtotal.add(importe);
        }

        return subtotal;
    }

    // ==========================================
    // CALCULAR TOTAL CON DESCUENTO
    // ==========================================

    public BigDecimal calcularTotalConDescuento(
            int idCarrito,
            EstrategiaDescuento estrategia) {

        BigDecimal subtotal = calcularSubtotal(idCarrito);

        if (subtotal.signum() <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal descuento = descuentoService.calcularDescuento(
                subtotal,
                estrategia
        );

        if (descuento == null || descuento.signum() < 0) {
            descuento = BigDecimal.ZERO;
        }

        if (descuento.compareTo(subtotal) > 0) {
            descuento = subtotal;
        }

        return subtotal.subtract(descuento);
    }

    // ==========================================
    // CALCULAR TOTAL DE EXTRAS
    // ==========================================

    public BigDecimal calcularTotalExtras(List<Extra> extras) {

        BigDecimal totalExtras = BigDecimal.ZERO;

        if (extras == null || extras.isEmpty()) {
            return totalExtras;
        }

        for (Extra extra : extras) {

            if (extra == null
                    || !extra.isActivo()
                    || extra.getPrecio() == null
                    || extra.getPrecio().signum() < 0) {
                continue;
            }

            totalExtras = totalExtras.add(extra.getPrecio());
        }

        return totalExtras;
    }

    // ==========================================
    // CALCULAR TOTAL DEFINITIVO
    // ==========================================

    public BigDecimal calcularTotalCompra(
            int idCarrito,
            EstrategiaDescuento estrategia,
            List<Extra> extras) {

        BigDecimal totalConDescuento =
                calcularTotalConDescuento(idCarrito, estrategia);

        BigDecimal totalExtras =
                calcularTotalExtras(extras);

        return totalConDescuento.add(totalExtras);
    }
}
