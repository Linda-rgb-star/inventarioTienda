/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.tienda.service;

import com.tienda.dao.NotificacionStockDAO;
import com.tienda.dao.ProductoDAO;
import com.tienda.model.NotificacionStock;
import com.tienda.model.Producto;

import java.util.List;

public class NotificacionService {

    private final NotificacionStockDAO notificacionDAO;
    private final ProductoDAO productoDAO;

    public NotificacionService() {
        this.notificacionDAO = new NotificacionStockDAO();
        this.productoDAO = new ProductoDAO();
    }

    // Registrar un cliente interesado en un producto agotado
    public boolean registrarInteres(NotificacionStock notificacion) {

        if (notificacion == null
                || notificacion.getCliente() == null
                || notificacion.getProducto() == null
                || notificacion.getCliente().getIdCliente() <= 0
                || notificacion.getProducto().getIdProducto() <= 0) {

            return false;
        }

        Producto producto = productoDAO.buscarPorId(
                notificacion.getProducto().getIdProducto());

        if (producto == null || producto.getStock() > 0) {
            System.out.println(
                    "El producto no está agotado o no existe.");
            return false;
        }

        return notificacionDAO.registrar(notificacion);
    }

    // Consultar notificaciones pendientes
    public List<NotificacionStock> consultarPendientes(int idProducto) {

        if (idProducto <= 0) {
            return List.of();
        }

        return notificacionDAO.listarPendientesPorProducto(idProducto);
    }

    // Marcar una notificación como enviada
    public boolean marcarNotificado(int idNotificacion) {

        if (idNotificacion <= 0) {
            return false;
        }

        return notificacionDAO.marcarComoNotificado(idNotificacion);
    }

    // Reponer inventario y gestionar notificaciones pendientes
    public boolean reponerInventarioYNotificar(
            int idProducto, int cantidad) {

        if (idProducto <= 0 || cantidad <= 0) {
            System.out.println("Datos de reposición inválidos.");
            return false;
        }

        // Consultar el producto
        Producto producto = productoDAO.buscarPorId(idProducto);

        if (producto == null) {
            System.out.println("El producto no existe.");
            return false;
        }

        // Aumentar las existencias
        boolean repuesto = productoDAO.aumentarStock(
                idProducto, cantidad);

        if (!repuesto) {
            System.out.println("No se pudo reponer el inventario.");
            return false;
        }

        // Consultar clientes pendientes
        List<NotificacionStock> pendientes =
                consultarPendientes(idProducto);

        if (pendientes.isEmpty()) {
            System.out.println(
                    "No hay clientes pendientes de notificación.");
            return true;
        }

        // Simular el envío de notificaciones
        for (NotificacionStock notificacion : pendientes) {

            System.out.println("--------------------------------");
            System.out.println("NOTIFICACIÓN DE REPOSICIÓN");
            System.out.println(
                    "Cliente ID: "
                    + notificacion.getCliente().getIdCliente());
            System.out.println(
                    "Producto: " + producto.getNombre());
            System.out.println(
                    "El producto vuelve a estar disponible.");
            
            // Marcar como notificada
            boolean marcada = marcarNotificado(
                    notificacion.getIdNotificacion());

            if (marcada) {
                System.out.println(
                        "Notificación registrada como enviada.");
            } else {
                System.out.println(
                        "No se pudo actualizar el estado de la notificación.");
            }
        }

        return true;
    }
}
