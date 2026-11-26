/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.app;

import com.tienda.dao.CuponDAO;
import com.tienda.model.Cupon;
import com.tienda.strategy.DescuentoPorCupon;
import com.tienda.strategy.ProcesadorDescuentos;
import java.math.BigDecimal;

public class PruebaCupon {

    public static void main(String[] args) {

        // Crear DAO para consultar cupones
        CuponDAO cuponDAO = new CuponDAO();

        // Buscar el cupón en MySQL
        String codigo = "BIENVENIDA10";

        Cupon cupon = cuponDAO.buscarPorCodigo(codigo);

        // Verificar que exista
        if (cupon == null) {
            System.out.println("El cupón no existe.");
            return;
        }

        // Verificar que sea válido
        if (!cupon.esValido()) {
            System.out.println("El cupón no es válido o está vencido.");
            return;
        }

        // Crear estrategia de descuento por cupón
        DescuentoPorCupon estrategia =
                new DescuentoPorCupon(cupon);

        // Crear procesador
        ProcesadorDescuentos procesador =
                new ProcesadorDescuentos(estrategia);

        // Calcular descuento sobre $100.000
        BigDecimal subtotal = new BigDecimal("100000");

        BigDecimal descuento =
                procesador.ejecutarDescuento(subtotal);

        BigDecimal total = subtotal.subtract(descuento);

        // Mostrar resultados
        System.out.println("\n===== PRUEBA DE CUPON =====");
        System.out.println("Codigo utilizado: " + codigo);
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("Descuento aplicado: $" + descuento);
        System.out.println("Total a pagar: $" + total);
    }
}
