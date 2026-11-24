/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Cupon {

    private int idCupon;
    private String codigo;
    private Descuento descuento;
    private LocalDate fechaExpiracion;
    private boolean activo;

    public Cupon() {
    }

    public Cupon(int idCupon, String codigo, Descuento descuento,
            LocalDate fechaExpiracion, boolean activo) {
        this.idCupon = idCupon;
        this.codigo = codigo;
        this.descuento = descuento;
        this.fechaExpiracion = fechaExpiracion;
        this.activo = activo;
    }

    public boolean esValido() {
        return activo
                && LocalDate.now().isBefore(fechaExpiracion.plusDays(1))
                && descuento != null
                && descuento.isActivo();
    }

    public int getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(int idCupon) {
        this.idCupon = idCupon;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Descuento getDescuento() {
        return descuento;
    }

    public void setDescuento(Descuento descuento) {
        this.descuento = descuento;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}