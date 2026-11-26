/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.tienda.app;

import com.tienda.model.Cliente;
import com.tienda.model.Producto;
import com.tienda.model.Extra;
import com.tienda.model.Pedido;
import com.tienda.model.DetallePedido;
import com.tienda.model.MetodoPago;
import com.tienda.model.Pago;
import com.tienda.model.Factura;

import com.tienda.service.CompraService;
import com.tienda.strategy.PagoTarjeta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PruebaCompra {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("PRUEBA DE COMPRA COMPLETA");
        System.out.println("================================");

        // 1. Crear cliente
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1);

        // 2. Seleccionar producto
        Producto producto = new Producto();
        producto.setIdProducto(2);

        // 3. Crear pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        pedido.setDireccionEntrega("Bogota, Colombia");
        pedido.setSubtotal(new BigDecimal("85000"));
        pedido.setTotalDescuentos(BigDecimal.ZERO);
        pedido.setTotal(new BigDecimal("105000"));

        // 4. Crear detalle del pedido
        DetallePedido detalle = new DetallePedido();

        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setNombreProducto("Audifonos Bluetooth");
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(new BigDecimal("85000"));
        detalle.setSubtotal(new BigDecimal("85000"));

        List<DetallePedido> detalles = new ArrayList<>();
        detalles.add(detalle);

        // 5. Crear garantía extendida
        Extra garantia = new Extra();
        garantia.setIdExtra(1);
        garantia.setNombre("Garantia extendida");
        garantia.setPrecio(new BigDecimal("15000"));
        garantia.setActivo(true);

        // 6. Crear empaque de regalo
        Extra empaque = new Extra();
        empaque.setIdExtra(2);
        empaque.setNombre("Empaque de regalo");
        empaque.setPrecio(new BigDecimal("5000"));
        empaque.setActivo(true);

        List<Extra> extrasProducto = new ArrayList<>();
        extrasProducto.add(garantia);
        extrasProducto.add(empaque);

        List<List<Extra>> extrasPorDetalle = new ArrayList<>();
        extrasPorDetalle.add(extrasProducto);

        // 7. Seleccionar método de pago
        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setIdMetodoPago(1);
        metodoPago.setNombre("Tarjeta");
        metodoPago.setActivo(true);

        // 8. Crear pago
        Pago pago = new Pago(
                pedido,
                metodoPago,
                new BigDecimal("105000")
        );

        // 9. Seleccionar estrategia de pago
        PagoTarjeta estrategia = new PagoTarjeta();

        // 10. Crear factura
        String numeroFactura =
                "FAC-PRUEBA-" + System.currentTimeMillis();

        Factura factura = new Factura(
                pedido,
                numeroFactura
        );

        // 11. Ejecutar compra completa
        CompraService compraService = new CompraService();

        boolean resultado = compraService.registrarCompraCompleta(
                pedido,
                detalles,
                extrasPorDetalle,
                pago,
                estrategia,
                factura
        );

        // 12. Mostrar resultado
        System.out.println("\n================================");

        if (resultado) {

            System.out.println("COMPRA COMPLETADA EXITOSAMENTE");
            System.out.println("ID del pedido: " + pedido.getIdPedido());
            System.out.println("ID del pago: " + pago.getIdPago());
            System.out.println("Estado del pago: " + pago.getEstado());
            System.out.println("Factura: " + factura.getNumeroFactura());
            System.out.println("Total: $" + pedido.getTotal());

        } else {

            System.out.println("NO SE COMPLETÓ LA COMPRA");
        }

        System.out.println("================================");
    }
}
