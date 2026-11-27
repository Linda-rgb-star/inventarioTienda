/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tienda.strategy;

import com.tienda.model.Pago;

public interface EstrategiaPago {

    boolean procesarPago(Pago pago);

    String obtenerNombreMetodo();
}
