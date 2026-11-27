/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pago {

    private int idPago;
    private Pedido pedido;
    private MetodoPago metodoPago;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private String estado;
    private String referencia;

    public Pago() {
    }

    public Pago(int idPago, Pedido pedido, MetodoPago metodoPago,
            BigDecimal monto, LocalDateTime fechaPago,
            String estado, String referencia) {
        this.idPago = idPago;
        this.pedido = pedido;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.estado = estado;
        this.referencia = referencia;
    }

    public Pago(Pedido pedido, MetodoPago metodoPago, BigDecimal monto) {
        this.pedido = pedido;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.fechaPago = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "idPago=" + idPago +
                ", metodoPago=" + metodoPago +
                ", monto=" + monto +
                ", estado='" + estado + '\'' +
                '}';
    }
}
