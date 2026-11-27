/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pedido {

    private int idPedido;
    private Cliente cliente;
    private LocalDateTime fechaPedido;
    private String estado;
    private BigDecimal subtotal;
    private BigDecimal totalDescuentos;
    private BigDecimal total;
    private String direccionEntrega;

    public Pedido() {
    }

    public Pedido(int idPedido, Cliente cliente,
            LocalDateTime fechaPedido, String estado,
            BigDecimal subtotal, BigDecimal totalDescuentos,
            BigDecimal total, String direccionEntrega) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.subtotal = subtotal;
        this.totalDescuentos = totalDescuentos;
        this.total = total;
        this.direccionEntrega = direccionEntrega;
    }

    public Pedido(Cliente cliente, BigDecimal subtotal,
            BigDecimal totalDescuentos, BigDecimal total,
            String direccionEntrega) {
        this.cliente = cliente;
        this.fechaPedido = LocalDateTime.now();
        this.estado = "PENDIENTE";
        this.subtotal = subtotal;
        this.totalDescuentos = totalDescuentos;
        this.total = total;
        this.direccionEntrega = direccionEntrega;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(BigDecimal totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", fechaPedido=" + fechaPedido +
                ", estado='" + estado + '\'' +
                ", subtotal=" + subtotal +
                ", totalDescuentos=" + totalDescuentos +
                ", total=" + total +
                '}';
    }
}