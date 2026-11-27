/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.tienda.strategy;

import java.math.BigDecimal;

public interface EstrategiaDescuento {

    BigDecimal calcularDescuento(BigDecimal subtotal);

    String obtenerNombreDescuento();
}
