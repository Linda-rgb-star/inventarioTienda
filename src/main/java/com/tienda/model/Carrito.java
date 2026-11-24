/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.model;

import java.time.LocalDateTime;

public class Carrito {

    private int idCarrito;
    private Cliente cliente;
    private LocalDateTime fechaCreacion;
    private String estado;

    public Carrito() {
    }

    public Carrito(int idCarrito, Cliente cliente,
            LocalDateTime fechaCreacion, String estado) {
        this.idCarrito = idCarrito;
        this.cliente = cliente;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Carrito(Cliente cliente) {
        this.cliente = cliente;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = "ACTIVO";
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Carrito{" +
                "idCarrito=" + idCarrito +
                ", cliente=" + cliente +
                ", fechaCreacion=" + fechaCreacion +
                ", estado='" + estado + '\'' +
                '}';
    }
}
