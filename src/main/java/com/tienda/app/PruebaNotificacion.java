/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda;

import com.tienda.model.NotificacionStock;
import com.tienda.model.Cliente;
import com.tienda.model.Producto;
import com.tienda.service.NotificacionService;
import java.util.List;

public class PruebaNotificacion {

    public static void main(String[] args) {

        NotificacionService servicio = new NotificacionService();

        // 1. Identificar cliente y producto
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1);

        Producto producto = new Producto();
        producto.setIdProducto(3);

        // 2. Registrar el interés del cliente
        NotificacionStock notificacion = new NotificacionStock();
        notificacion.setCliente(cliente);
        notificacion.setProducto(producto);
        notificacion.setNotificado(false);

        boolean registrado = servicio.registrarInteres(notificacion);

        if (registrado) {
            System.out.println("Interés registrado correctamente.");
        } else {
            System.out.println("No se pudo registrar el interés.");
            return;
        }

        // 3. Consultar notificaciones pendientes
        List<NotificacionStock> pendientes =
                servicio.consultarPendientes(3);

        System.out.println("Notificaciones pendientes: "
                + pendientes.size());

        // 4. Marcar la primera notificación como enviada
        if (!pendientes.isEmpty()) {

            int id = pendientes.get(0).getIdNotificacion();

            boolean actualizado = servicio.marcarNotificado(id);

            if (actualizado) {
                System.out.println("Notificación marcada como NOTIFICADO.");
            } else {
                System.out.println("No se pudo actualizar la notificación.");
            }
        }
    }
}
