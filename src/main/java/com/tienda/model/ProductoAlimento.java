/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductoAlimento extends Producto {

    // Atributos específicos
    private LocalDate fechaVencimiento;
    private BigDecimal pesoGramos;
    private String ingredientes;

    // Constructor vacío
    public ProductoAlimento() {
        super();
    }

    // Constructor completo
    public ProductoAlimento(int idProducto, Categoria categoria,
                            String nombre, String descripcion,
                            BigDecimal precio, int stock, boolean activo,
                            LocalDate fechaVencimiento,
                            BigDecimal pesoGramos, String ingredientes) {

        super(idProducto, categoria, nombre, descripcion,
              precio, stock, activo);

        this.fechaVencimiento = fechaVencimiento;
        this.pesoGramos = pesoGramos;
        this.ingredientes = ingredientes;
    }

    // Constructor para registrar alimentos
    public ProductoAlimento(Categoria categoria, String nombre,
                            String descripcion, BigDecimal precio,
                            int stock, LocalDate fechaVencimiento,
                            BigDecimal pesoGramos, String ingredientes) {

        super(categoria, nombre, descripcion, precio, stock);

        this.fechaVencimiento = fechaVencimiento;
        this.pesoGramos = pesoGramos;
        this.ingredientes = ingredientes;
    }

    // Getters y Setters

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getPesoGramos() {
        return pesoGramos;
    }

    public void setPesoGramos(BigDecimal pesoGramos) {
        this.pesoGramos = pesoGramos;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Vencimiento: " + fechaVencimiento
                + " | Peso: " + pesoGramos + " gramos";
    }
}
    