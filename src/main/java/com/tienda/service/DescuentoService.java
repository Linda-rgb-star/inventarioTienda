/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.service;

import com.tienda.dao.CuponDAO;
import com.tienda.model.Cupon;
import com.tienda.strategy.EstrategiaDescuento;
import com.tienda.strategy.ProcesadorDescuentos;
import java.math.BigDecimal;

public class DescuentoService {

    private final CuponDAO cuponDAO;

    public DescuentoService() {
        this.cuponDAO = new CuponDAO();
    }

    public BigDecimal calcularDescuento(
            BigDecimal subtotal,
            EstrategiaDescuento estrategia) {

        if (subtotal == null || subtotal.signum() <= 0
                || estrategia == null) {
            return BigDecimal.ZERO;
        }

        ProcesadorDescuentos procesador =
                new ProcesadorDescuentos(estrategia);

        BigDecimal descuento = procesador.ejecutarDescuento(subtotal);

        if (descuento == null || descuento.signum() < 0) {
            return BigDecimal.ZERO;
        }

        return descuento.min(subtotal);
    }

    public Cupon buscarCupon(String codigo) {

        if (codigo == null || codigo.isBlank()) {
            return null;
        }

        return cuponDAO.buscarPorCodigo(codigo);
    }
}
