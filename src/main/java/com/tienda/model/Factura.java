/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Factura {

    private int idFactura;
    private Pedido pedido;
    private String numeroFactura;
    private LocalDateTime fechaEmision;
    private BigDecimal total;
    private String estado;

    public Factura() {
    }

    public Factura(int idFactura, Pedido pedido,
            String numeroFactura, LocalDateTime fechaEmision,
            BigDecimal total, String estado) {
        this.idFactura = idFactura;
        this.pedido = pedido;
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.estado = estado;
    }

    public Factura(Pedido pedido, String numeroFactura) {
        this.pedido = pedido;
        this.numeroFactura = numeroFactura;
        this.fechaEmision = LocalDateTime.now();
        this.total = pedido.getTotal();
        this.estado = "GENERADA";
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "numeroFactura='" + numeroFactura + '\'' +
                ", fechaEmision=" + fechaEmision +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                '}';
    }
}