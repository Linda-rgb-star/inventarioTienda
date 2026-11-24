/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Descuento {

    private int idDescuento;
    private String nombre;
    private String tipo;
    private BigDecimal valor;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean acumulable;
    private boolean activo;

    public Descuento() {
    }

    public Descuento(int idDescuento, String nombre, String tipo,
            BigDecimal valor, LocalDate fechaInicio, LocalDate fechaFin,
            boolean acumulable, boolean activo) {
        this.idDescuento = idDescuento;
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.acumulable = acumulable;
        this.activo = activo;
    }

    public boolean estaVigente() {
        LocalDate hoy = LocalDate.now();

        return activo
                && !hoy.isBefore(fechaInicio)
                && !hoy.isAfter(fechaFin);
    }

    // Getters y setters

    public int getIdDescuento() {
        return idDescuento;
    }

    public void setIdDescuento(int idDescuento) {
        this.idDescuento = idDescuento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isAcumulable() {
        return acumulable;
    }

    public void setAcumulable(boolean acumulable) {
        this.acumulable = acumulable;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}