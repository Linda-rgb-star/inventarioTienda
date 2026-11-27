/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.model;

import java.math.BigDecimal;

public class DetallePedidoExtra {

    private int idDetallePedido;
    private Extra extra;
    private BigDecimal precioAplicado;

    public DetallePedidoExtra() {
    }

    public DetallePedidoExtra(int idDetallePedido, Extra extra,
            BigDecimal precioAplicado) {
        this.idDetallePedido = idDetallePedido;
        this.extra = extra;
        this.precioAplicado = precioAplicado;
    }

    public DetallePedidoExtra(Extra extra) {
        this.extra = extra;
        this.precioAplicado = extra.getPrecio();
    }

    public int getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(int idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public Extra getExtra() {
        return extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public BigDecimal getPrecioAplicado() {
        return precioAplicado;
    }

    public void setPrecioAplicado(BigDecimal precioAplicado) {
        this.precioAplicado = precioAplicado;
    }

    @Override
    public String toString() {
        return extra.getNombre() + " - $" + precioAplicado;
    }
}