/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.model;

import java.math.BigDecimal;

public class ProductoTecnologia extends Producto {

    // Atributos específicos
    private String marca;
    private String modelo;
    private int garantiaMeses;

    // Constructor vacío
    public ProductoTecnologia() {
        super();
    }

    // Constructor completo
    public ProductoTecnologia(int idProducto, Categoria categoria,
                              String nombre, String descripcion,
                              BigDecimal precio, int stock, boolean activo,
                              String marca, String modelo,
                              int garantiaMeses) {

        super(idProducto, categoria, nombre, descripcion,
              precio, stock, activo);

        this.marca = marca;
        this.modelo = modelo;
        this.garantiaMeses = garantiaMeses;
    }

    // Constructor para registrar un producto tecnológico
    public ProductoTecnologia(Categoria categoria, String nombre,
                              String descripcion, BigDecimal precio,
                              int stock, String marca, String modelo,
                              int garantiaMeses) {

        super(categoria, nombre, descripcion, precio, stock);

        this.marca = marca;
        this.modelo = modelo;
        this.garantiaMeses = garantiaMeses;
    }

    // Getters y Setters

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    public void setGarantiaMeses(int garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Marca: " + marca
                + " | Modelo: " + modelo
                + " | Garantía: " + garantiaMeses + " meses";
    }
}
