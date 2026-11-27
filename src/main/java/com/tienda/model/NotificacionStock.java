/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.model;

import java.time.LocalDateTime;

public class NotificacionStock {

    private int idNotificacion;
    private Cliente cliente;
    private Producto producto;
    private LocalDateTime fechaSolicitud;
    private boolean notificado;

    public NotificacionStock() {
    }

    public NotificacionStock(int idNotificacion, Cliente cliente,
            Producto producto, LocalDateTime fechaSolicitud,
            boolean notificado) {
        this.idNotificacion = idNotificacion;
        this.cliente = cliente;
        this.producto = producto;
        this.fechaSolicitud = fechaSolicitud;
        this.notificado = notificado;
    }

    public NotificacionStock(Cliente cliente, Producto producto) {
        this.cliente = cliente;
        this.producto = producto;
        this.fechaSolicitud = LocalDateTime.now();
        this.notificado = false;
    }

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public boolean isNotificado() {
        return notificado;
    }

    public void setNotificado(boolean notificado) {
        this.notificado = notificado;
    }
}